# ADR-002: Mecanismo de comunicación entre servicios

- Estado: aceptada
- Fecha: 2026-11-21
- Responsable: Bueno Troya Erwin Daniel (Arquitecto)
- Relacionado con: ADR-001, `docs/secciones/09-comunicacion.tex`, `src/ms-propuestas`, `src/ms-usuarios`

## Contexto

Con la decisión de ADR-001 el sistema queda repartido en varios servicios que tienen que hablar entre sí. Las interacciones que aparecen en el diagrama de contenedores son de tres clases:

1. Consultas y órdenes del usuario. El estudiante abre su propuesta, el director registra una observación, el tribunal pone una nota. La persona está esperando en la pantalla y necesita la respuesta en ese momento.
2. Validaciones entre servicios. Antes de aceptar el primer documento, ms-documentos tiene que saber si la propuesta del estudiante está aprobada; ms-propuestas necesita el nombre y el rol de un usuario que vive en ms-usuarios. También son preguntas que esperan respuesta.
3. Avisos de que algo ya pasó. "Tu documento fue observado", "se agendó tu sustentación". Nadie espera esa respuesta: el cambio de estado ya quedó guardado y el aviso puede llegar unos segundos después. uteq-export ya hace esto hoy con WebSocket y STOMP para los cambios de estado del documento.

Además:

- El frontend es Angular y corre en el navegador. Lo que use el navegador tiene que funcionar sin proxies especiales.
- Todo el equipo conoce Spring MVC, `RestClient`, Postman y `curl`; el monolito ya expone su API en REST con JSON.
- En PE-U1 el equipo comparó sockets TCP y gRPC entre tres nodos en la misma máquina. Al cerrar E1 los resultados finales de esa práctica no estaban publicados en su repositorio, así que esta decisión no usa sus cifras: se apoya en el tamaño de las llamadas y en el presupuesto de 500 ms de RNF-01. La prueba de carga de E2 dirá si hay que revisarla.

## Opciones consideradas

### Opción A: REST síncrono (HTTP con JSON) para todo

- A favor: es lo que ya usa el monolito y lo que entiende el navegador sin nada extra. Se prueba con `curl` o Postman y los errores se leen en texto plano.
- En contra: los avisos también tendrían que ser llamadas directas. Si ms-documentos llamara a ms-notificaciones al guardar una observación y ms-notificaciones estuviera caído, la observación fallaría o habría que reintentar a mano.

### Opción B: gRPC entre servicios y REST solo hacia el navegador

- A favor: contrato tipado con Protocol Buffers y mensajes binarios más pequeños. En PE-U1 lo usamos y lo medimos.
- En contra: el navegador no habla gRPC directo; haría falta gRPC-Web y un proxy, o mantener dos estilos de API. Nuestras llamadas son pocas y pequeñas (un usuario, una propuesta), así que la ventaja de latencia de gRPC pesa poco frente al presupuesto de 500 ms de RNF-01. Para depurar ya no basta con `curl`.

### Opción C: mensajería asíncrona para todo

Cada servicio publica y consume mensajes en un broker, incluso para las consultas.

- A favor: desacopla en el tiempo; si un servicio está caído, los mensajes esperan en la cola.
- En contra: una consulta del usuario se vuelve un par de mensajes de pedido y respuesta con correlación, más difícil de programar y de probar. El usuario igual tiene que esperar la respuesta, así que el desacople no le sirve de nada en ese caso.

### Opción D: REST síncrono como mecanismo principal y mensajería para los avisos

REST para las interacciones de las clases 1 y 2 y un broker de mensajes (RabbitMQ) para los eventos de la clase 3.

- A favor: cada interacción usa el mecanismo que corresponde a si alguien espera o no la respuesta. La parte de mensajería es pequeña y se concentra en ms-notificaciones.
- En contra: hay dos mecanismos que mantener y un componente más (el broker).

## Decisión

Elegimos la opción D.

1. REST con JSON sobre HTTP es el mecanismo principal. Lo usan el navegador hacia el API Gateway, el gateway hacia los servicios y los servicios entre sí cuando necesitan una respuesta para seguir.
2. Toda llamada REST entre servicios tiene un tiempo límite configurable. En el esqueleto es `USUARIOS_TIMEOUT_MS`, de 2000 ms por defecto. Si el servicio llamado no responde, el que llama devuelve 503 con un mensaje claro y no se queda colgado (RNF-05).
3. Solo se reintentan automáticamente las peticiones GET, que no cambian nada. Desde E2 se agrega un cortocircuito (circuit breaker) con Resilience4j para no insistir sobre un servicio caído.
4. Los avisos van por RabbitMQ desde E2. ms-propuestas, ms-documentos y ms-evaluacion publican eventos cuando cambia un estado (`propuesta.decidida`, `documento.observado`, `documento.aprobado`, `sustentacion.agendada`, `acta.generada`) y ms-notificaciones los consume para mandar el correo y el aviso por WebSocket.
5. Cada evento lleva un identificador único. ms-notificaciones guarda los identificadores ya procesados para no mandar dos veces el mismo aviso, porque RabbitMQ entrega al menos una vez y puede repetir mensajes.

Preferimos RabbitMQ a Kafka porque solo lo necesitamos como cola de avisos, no como registro de eventos para reprocesar, y porque nuestros servicios son Spring Boot: Kamiński et al. [kaminski2025rabbitmq] compararon Kafka 4.0 y RabbitMQ 4.1 y encontraron que con Spring Boot RabbitMQ usó cerca de 29 % menos memoria y respondió alrededor de 25 % más rápido.

## Consecuencias

Positivas:

- El esqueleto de E1 ya usa este mecanismo: ms-propuestas consulta a ms-usuarios con `RestClient` y arma la respuesta combinada. Se prueba con un `curl` o con `Invoke-RestMethod`.
- Si ms-notificaciones o el servidor de correo fallan, guardar una observación o una nota no falla; los avisos esperan en la cola.
- El contrato de cada servicio se puede documentar con OpenAPI en E2 y probar desde el navegador.

Negativas y cómo las vamos a manejar:

- Acoplamiento temporal en las llamadas REST: si ms-usuarios cae, ms-propuestas no puede armar su respuesta. Lo comprobamos en el esqueleto: con ms-usuarios apagado, `GET /api/propuestas/1` responde 503 en lugar de quedarse esperando. En E2 los datos de usuario que cambian poco (nombre, rol) se guardan en caché en ms-propuestas para seguir respondiendo aunque ms-usuarios no esté.
- Las cadenas de llamadas suman latencia. Regla: una petición del usuario no debe atravesar más de dos saltos entre servicios después del gateway. Si un caso necesita más, se revisa el diseño.
- Los eventos pueden llegar repetidos o con retraso. Por eso los consumidores son idempotentes (punto 5 de la decisión) y ningún dato crítico depende de un evento; los avisos son solo avisos.
- El broker es una pieza más que puede fallar. Si RabbitMQ no está disponible, los servicios guardan el cambio de estado igual y registran el evento pendiente para publicarlo después (patrón outbox, en E3).

## Referencias

Las claves corresponden a `docs/referencias.bib`.

- [kaminski2025rabbitmq] Kamiński, Kłonica y Pańczyk, Journal of Computer Sciences Institute, 2025.
