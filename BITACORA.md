# Bitácora del equipo

<!-- Una fila por sesión de trabajo, la más reciente al final. Fecha en formato AAAA-MM-DD.
     Integrantes presentes: apellidos de quienes estuvieron. Decisiones y problemas: frases cortas y concretas,
     con el archivo o el issue cuando exista (por ejemplo, "ADR-002", "#12"). -->

| Fecha | Integrantes presentes | Decisiones tomadas | Problemas resueltos |
|---|---|---|---|
| 2026-11-17 | Bueno | Bueno crea el repositorio, la estructura de carpetas (`src/`, `docs/`, `docs/adr/`, `docs/diagramas/`, `evidencias/`), el README, el `.gitignore` y el esqueleto LaTeX del documento (`main.tex`, `datos.tex`, portada, `referencias.bib` y `sga.tex`). | |
| 2026-11-18 | Bueno, Mendoza, Zambrano, Rivas | Reunión por Discord. Revisión del enunciado de TA-PFC-E1 y de la estructura del repositorio. Se acuerda partir del sistema de titulación existente (uteq-export) y entrevistar a la coordinación de la carrera. | |
| 2026-11-19 | Bueno, Mendoza, Zambrano, Rivas | Entrevista a la Ing. Jessica Ponce, Coordinadora de la Carrera de Ingeniería de Software, grabada en video. Después, reunión por Discord para revisar lo que dijo y relacionarlo con los requisitos. | |
| 2026-11-20 | Bueno, Mendoza, Zambrano, Rivas | Reunión por Discord. Reparto de roles (Bueno, Arquitecto; Mendoza, Líder de Desarrollo; Zambrano, Responsable de Calidad; Rivas, Responsable de Documentación). Se parte del sistema de titulación existente (uteq-export) y se separa en microservicios por subdominio (ADR-001), con REST como mecanismo principal y RabbitMQ para los avisos (ADR-002). El esqueleto tendrá ms-usuarios (puerto 8081) y ms-propuestas (puerto 8082), en Spring Boot 4.0.2 con Java 17 y datos en memoria. | La portada y el README tenían integrantes de otro grupo; se corrigieron `docs/datos.tex` y el README. |
| 2026-11-22 | Bueno | Sube el documento LaTeX con sus secciones, los ADR-001 y ADR-002, los diagramas C4 de contexto y de contenedores, la relación de requisitos con la entrevista y el PDF del SGA. Crea el tablero en GitHub Projects y compila el documento completo. | |
| 2026-11-22 | Mendoza | Sube los servicios ms-usuarios y ms-propuestas con sus pruebas, el script de issues, la sección del plan, la sección del esqueleto, las capturas de ejecución y las instrucciones del README. Crea los issues de E1 y E2. | En la prueba del esqueleto, PowerShell 5.1 mostraba mal las tildes de la respuesta JSON porque Spring Boot 4 no incluía el charset en el Content-Type; se corrigió con `spring.servlet.encoding.force-response=true`. |
| 2026-11-22 | Zambrano | Sube los requisitos no funcionales con ISO/IEC 25010, el análisis de riesgos con los tres derivados de FORM-AC-01, las referencias en biblatex y el apéndice de verificación de referencias. | |
| 2026-11-22 | Rivas | Sube los trabajos relacionados, la tabla de aportes, las fotografías de las entrevistas y de las sesiones con usuarios, la bitácora y el guion del video. | |
