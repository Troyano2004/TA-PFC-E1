# ADR-001: Estilo de arquitectura

- Estado: aceptada
- Fecha: 2026-11-21
- Responsable: Bueno Troya Erwin Daniel (Arquitecto)
- Relacionado con: ADR-002, `docs/secciones/06-requisitos-no-funcionales.tex`, `docs/diagramas/c4-nivel2-contenedores.drawio`

## Contexto

Partimos de uteq-export, el sistema de gestión de titulación que ya tenemos construido. Es un monolito: un backend Spring Boot 4.0.2 con Java 17 (551 archivos Java, 64 controladores REST, 99 entidades JPA y 19 migraciones Flyway sobre una sola base PostgreSQL) y un frontend Angular 21. Todo se compila y se despliega junto.

El monolito cubre propuestas de tema, anteproyecto, documento de titulación con revisión del director, tutorías, examen complexivo, sustentación, actas, respaldos, reportes e importación de datos del SGA. Se conecta con Google Drive para guardar archivos, con un servidor de correo SMTP, con Zoom y con servicios de IA para revisar propuestas y detectar texto generado.

Lo que nos obliga a tomar esta decisión:

1. La carga no es pareja. Se junta en las fechas límite de propuestas y de entrega del documento, cuando muchos estudiantes suben archivos a la vez; el resto del periodo el uso es bajo. Ver RNF-01, RNF-02 y RNF-03.
2. En el monolito todo corre en el mismo proceso. Si la generación de un PDF o un respaldo se queda sin memoria, también se cae la recepción de documentos, justo lo que no puede fallar en una fecha límite. Ver RNF-04 y RNF-05.
3. Las reglas cambian con el Reglamento de la UIC y con cada periodo (plazos, requisitos, rúbricas). Hoy un cambio pequeño en la revisión de propuestas obliga a redesplegar el sistema entero. Ver RNF-09.
4. La asignatura pide microservicios, API y contenedores en E2 y una base de datos distribuida en E3. Es un requisito del curso y lo dejamos escrito como tal, no como una necesidad de la UTEQ.
5. Somos cuatro personas con un periodo académico. Lo que elijamos tiene que poder construirse, probarse y explicarse en ese tiempo.

## Opciones consideradas

### Opción A: mantener el monolito y escalarlo verticalmente

Seguir con un solo despliegue y darle más CPU y memoria al servidor en las fechas de entrega.

- A favor: no hay que reescribir nada, no aparecen llamadas de red entre módulos y las transacciones siguen siendo locales. Blinowski et al. [blinowski2022monolithic] compararon las dos arquitecturas con la misma aplicación y encontraron que en una sola máquina el monolito rinde mejor y que, en Azure, escalar en vertical les salió más barato que escalar en horizontal.
- En contra: no resuelve los puntos 2 y 3 del contexto. Un fallo en cualquier módulo sigue tumbando todo y cada cambio exige redesplegar el sistema completo. Tampoco cumple lo que pide E2.

### Opción B: monolito modular

Reordenar uteq-export en módulos con límites claros (usuarios, propuestas, documentos, evaluación), con un esquema de base de datos por módulo dentro de la misma instancia de PostgreSQL, y levantar dos o tres réplicas iguales detrás de un balanceador.

- A favor: mejora la organización del código y la disponibilidad con poco riesgo. Velepucha y Flores [velepucha2023survey] relacionan esta opción con el principio de ocultamiento de información de Parnas: módulos bien separados ya dan buena parte de la flexibilidad que se busca con los microservicios.
- En contra: el despliegue sigue siendo uno solo, así que el punto 3 queda igual. Las réplicas son copias completas: para atender el pico de subidas hay que replicar también reportes, complexivo y respaldos, que en ese momento no se usan. Un error que agota la memoria se repite en todas las réplicas a la vez.

### Opción C: microservicios por subdominio, extraídos de forma gradual

Separar los subdominios del flujo del proyecto de titulación en servicios con su propia base de datos:

| Servicio | Qué tiene | Cuándo |
|---|---|---|
| ms-usuarios | Usuarios, roles y emisión de tokens JWT | Esqueleto en E1, completo en E2 |
| ms-propuestas | Propuestas de tema, decisión de la comisión, asignación de tutor | Esqueleto en E1, completo en E2 |
| ms-documentos | Versiones del documento de titulación, observaciones del director, antiplagio | E2 |
| ms-evaluacion | Tribunal, sustentación, calificaciones y actas | E2 |
| ms-notificaciones | Avisos por correo y por WebSocket a partir de eventos | E2 |
| API Gateway | Punto de entrada único, validación del JWT y enrutamiento | E2 |

Lo que no se migra en el periodo (examen complexivo, banco de preguntas, respaldos, reportes institucionales e importación del SGA) sigue en uteq-export, y el gateway le envía esas rutas. Es el patrón de extracción gradual: el monolito se va vaciando a medida que aparecen servicios, sin una migración de golpe.

- A favor: cada servicio se escala por separado (en el pico solo se replica ms-documentos), un fallo queda aislado en su servicio y cada uno se despliega sin detener a los demás. La división sigue los subdominios que ya existen en el código actual. Abgaz et al. [abgaz2023decomposition] revisaron 35 estudios sobre cómo partir un monolito y concluyen que todavía no hay métodos maduros ni métricas estándar para hacerlo, así que preferimos un criterio simple que podamos explicar.
- En contra: aparecen la latencia de red, los fallos parciales y la consistencia entre bases distintas, y hay más piezas que operar (gateway, broker, cinco bases de datos). Söylemez et al. [soylemez2022challenges] revisaron 85 estudios y agruparon los problemas de adoptar microservicios en nueve categorías; su recomendación es contrastarlos con los requisitos del proyecto antes de decidir, que es lo que hacemos en la sección siguiente.

## Decisión

Elegimos la opción C: microservicios por subdominio, extraídos de uteq-export de forma gradual.

No la elegimos por rendimiento. Con el tamaño de la UTEQ, los resultados de Blinowski et al. [blinowski2022monolithic] sugieren que un monolito bien dimensionado podría atender la carga, y ellos mismos observaron que agregar instancias más allá de cierto número empeora el rendimiento. La elegimos porque los requisitos que más pesan en este sistema son el aislamiento de fallos en las fechas de entrega (RNF-05), el escalado de una sola parte (RNF-03) y el despliegue independiente cuando cambian las reglas (RNF-09), y esos tres solo los resuelve la opción C. El requisito del curso para E2 apunta en la misma dirección.

Para no decidir a ciegas, como recomiendan Auer et al. [auer2021monolithic], en E2 medimos el monolito actual con la misma prueba de carga de RNF-01 y RNF-02 y la repetimos sobre los microservicios en E4. Si los números salen peor sin una ganancia en disponibilidad, lo vamos a reportar.

Para mantener el tamaño manejable fijamos dos reglas:

1. Un servicio por subdominio, no por entidad. No habrá un servicio para "observaciones" ni otro para "tutorías".
2. Ningún servicio lee la base de datos de otro. Si necesita un dato ajeno, lo pide por la API (ADR-002) o lo recibe en un evento.

## Consecuencias

Positivas:

- En la semana de entrega del documento se pueden levantar más instancias de ms-documentos sin tocar el resto (RNF-03).
- Si ms-evaluacion o ms-notificaciones fallan, la subida de documentos y la consulta de propuestas siguen funcionando (RNF-05).
- Un cambio de reglas en propuestas se despliega solo en ms-propuestas (RNF-09).
- Cada integrante puede trabajar en un servicio con menos conflictos en el repositorio.

Negativas y cómo las vamos a manejar:

- Una consulta que antes era un JOIN ahora puede ser una o varias llamadas HTTP. En el esqueleto, ms-propuestas hace dos llamadas a ms-usuarios por cada propuesta. Lo manejamos con tiempos límite y, desde E2, con caché de datos de usuario que cambian poco.
- No hay transacciones entre bases distintas. Los cambios que afectan a dos servicios (por ejemplo, propuesta aprobada que habilita el primer documento) se resuelven con estados y eventos, no con una transacción. El análisis CAP/PACELC de `docs/secciones/10-cap-pacelc.tex` dice qué datos necesitan consistencia fuerte.
- Hay más infraestructura que levantar. En E2 todo corre con Docker Compose; sin eso, probar el sistema completo en una laptop sería muy lento.
- Depurar un error que cruza servicios es más difícil. Desde E2 cada petición lleva un identificador de correlación que se escribe en los logs de todos los servicios.
- Mientras dure la migración conviven el monolito y los servicios, y el gateway tiene que saber qué ruta va a cada lado.

## Referencias

Las claves corresponden a `docs/referencias.bib`.

- [blinowski2022monolithic] Blinowski, Ojdowska y Przybylek, IEEE Access, 2022.
- [auer2021monolithic] Auer, Lenarduzzi, Felderer y Taibi, Information and Software Technology, 2021.
- [abgaz2023decomposition] Abgaz et al., IEEE Transactions on Software Engineering, 2023.
- [velepucha2023survey] Velepucha y Flores, IEEE Access, 2023.
- [soylemez2022challenges] Söylemez, Tekinerdogan y Kolukısa Tarhan, Applied Sciences, 2022.
