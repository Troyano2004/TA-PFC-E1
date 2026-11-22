# Guion del video de presentación (E1)

Duración objetivo: 5 min 40 s (el enunciado pide entre 5 y 7). Cada integrante aparece con cámara y voz mientras explica su parte. No es un texto para leer: son los puntos que cada uno tiene que decir con sus palabras.

Grabación: reunión de Meet o Teams grabada, o OBS. Cada uno comparte su pantalla cuando le toca; el PDF compilado (`docs/main.pdf`) sirve para mostrar las secciones. Antes de empezar, Elian deja compilados los dos servicios para que la demo no espere a Maven. Se graba el viernes 27 de noviembre y Dayver lo sube el sábado 28 (YouTube como "No listado" o Drive con acceso para cualquiera con el enlace).

| Tiempo | Quién | Qué se ve en pantalla | Qué dice |
|---|---|---|---|
| 0:00 a 0:40 | Dayver (Documentación) | Cámara; luego la carpeta `evidencias/levantamiento/` | Presenta al equipo y los roles. El problema: el proceso de titulación de la UIC en la UTEQ y lo que nos contaron en las entrevistas (a quién entrevistamos y qué nos dijeron, en dos frases). Nombra los tres trabajos relacionados en una frase cada uno. |
| 0:40 a 2:10 | Daniel (Arquitecto) | `c4-nivel1-contexto.drawio` y `c4-nivel2-contenedores.drawio` abiertos en draw.io | Por qué un sistema distribuido: picos en fechas de entrega, una falla no debe tumbar la recepción de documentos, las reglas cambian con el reglamento. Recorre el diagrama de contexto y el de contenedores. ADR-001: por qué microservicios y no monolito modular. ADR-002: REST para lo que espera respuesta y RabbitMQ para los avisos. Un ejemplo de PACELC: calificaciones PC/EC frente a archivos del documento PA/EL. |
| 2:10 a 3:40 | Elian (Líder de Desarrollo) | Terminales de PowerShell y el tablero de GitHub Projects | Estructura de `src/`. Demo en vivo (ver abajo). Explica que ms-propuestas solo guarda los id y le pide a ms-usuarios los datos del estudiante y del tutor, con un tiempo límite de 2 s. Muestra el 503 cuando ms-usuarios está apagado. Cierra mostrando el tablero con los issues de E1 cerrados y los de E2 asignados. |
| 3:40 a 4:50 | Leonel (Calidad) | Las secciones de requisitos no funcionales y de riesgos en el PDF | Dos o tres requisitos no funcionales con sus números y cómo se van a medir (por ejemplo RNF-01 y RNF-05, que ya se ve en la demo). La característica de ISO/IEC 25010 de cada uno. Dos riesgos técnicos y los riesgos que sacamos del incidente de FORM-AC-01. Cómo verificamos las referencias en Crossref. |
| 4:50 a 5:40 | Dayver (Documentación) | `BITACORA.md` y la tabla de aportes en el PDF compilado | La bitácora y cómo registramos las sesiones. La tabla de aportes con los enlaces a commits e issues. La declaración de uso de IA del documento. Cierre: qué viene en E2 (gateway, contenedores, ms-documentos). |

## Demo del esqueleto (parte de Elian)

Tres ventanas de PowerShell, con la fecha y la hora del sistema visibles en la barra de tareas.

Ventana 1:

```
cd src\ms-usuarios
.\mvnw.cmd spring-boot:run
```

Ventana 2:

```
cd src\ms-propuestas
.\mvnw.cmd spring-boot:run
```

Ventana 3, en este orden:

```
whoami
hostname
Get-Date
Invoke-RestMethod http://localhost:8082/api/propuestas/1 | ConvertTo-Json -Depth 5
```

Se señala en la respuesta qué parte viene de ms-propuestas (título, estado) y qué parte de ms-usuarios (estudiante y tutor), y en la ventana 2 la línea del log `GET http://localhost:8081/api/usuarios/1 -> 200`.

Después se detiene ms-usuarios con Ctrl+C en la ventana 1 y se repite la llamada:

```
curl.exe -i http://localhost:8082/api/propuestas/1
```

Debe responder `HTTP/1.1 503` con `"error":"ms-usuarios no está disponible"`. Con eso se ve el acoplamiento temporal que explica Daniel en ADR-002.
