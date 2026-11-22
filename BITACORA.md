# Bitácora del equipo

<!-- Una fila por sesión de trabajo, la más reciente al final. Fecha en formato AAAA-MM-DD.
     Integrantes presentes: apellidos de quienes estuvieron. Decisiones y problemas: frases cortas y concretas,
     con el archivo o el issue cuando exista (por ejemplo, "ADR-002", "#12"). -->

| Fecha | Integrantes presentes | Decisiones tomadas | Problemas resueltos |
|---|---|---|---|
| 2026-11-17 | | Se crea el repositorio y la estructura base. Se define la portada. | |
| 2026-11-20 | (completar) | Se forma el grupo BMRZ y se reparten los roles: Bueno (Arquitecto), Mendoza (Líder de Desarrollo), Zambrano (Responsable de Calidad) y Rivas (Responsable de Documentación). Se decide partir del sistema de titulación existente (uteq-export) y separarlo en microservicios por subdominio (ADR-001), con REST como mecanismo principal y RabbitMQ para los avisos (ADR-002). El esqueleto tendrá dos servicios en Spring Boot 4.0.2 con Java 17 y datos en memoria: ms-usuarios (puerto 8081) y ms-propuestas (puerto 8082), que consulta al primero. Cada integrante hace sus commits en cuatro días distintos entre el 21 y el 28 de noviembre. | La portada y el README tenían integrantes y roles de otro grupo; se actualizaron `docs/datos.tex` y el README con los cuatro del grupo BMRZ. En la prueba del esqueleto, PowerShell 5.1 mostraba mal las tildes de la respuesta JSON porque Spring Boot 4 no incluía el charset en el Content-Type; se corrigió con `spring.servlet.encoding.force-response=true` (en Spring Boot 4 la propiedad cambió de `server.servlet.encoding.*` a `spring.servlet.encoding.*`). |
