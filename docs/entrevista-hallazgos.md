# Hallazgos del levantamiento y trazabilidad de los requisitos

## Levantamiento de esta entrega

- Entrevista con la Ing. Jessica Ponce, Coordinadora de la Carrera de Ingeniería de Software de la UTEQ, el jueves 19 de noviembre de 2026. Se grabó en video.
- Enlace al video de la entrevista: \pendiente{enlace}
- Consulta complementaria a docentes de la carrera familiarizados con el proceso de titulación. Queda como respaldo la fotografía `evidencias/levantamiento/entrevista-docentes-01.png`.
- Las demás fotografías de las entrevistas están en `evidencias/levantamiento/`.

Este archivo no transcribe la entrevista ni atribuye frases a la entrevistada ni a los docentes. Cada requisito indica su fuente escrita.

## Fuentes escritas

- Documento de requisitos "Gestión de documentación para la titulación" (proyecto de Aplicaciones Web, enero de 2026), de Bueno, Mendoza y Rivas. Se leyó completo en TeXPage el 22 de noviembre de 2026.
- Reglamento de la Unidad de Integración Curricular de la UTEQ, aprobado por el Consejo Universitario el 27 de septiembre de 2024 (`docs/normativa/Reglamento_UIC_UTEQ_2024.pdf` del repositorio uteq-export).

Según el documento de Aplicaciones Web, sus requisitos salieron de entrevistas con los actores del proceso, de la revisión del reglamento y del análisis del flujo actual (sección "Requisitos"). Ese documento tiene una fotografía de una entrevista con la coordinación de carrera (sección "Evidencia de la entrevista"), pero no transcribe lo que se dijo. En las tablas, la columna "Fuente según el documento" copia el campo "Fuente" de cada requisito tal como está escrito.

Lo único que ese documento atribuye directamente a la Coordinadora de la carrera es una observación de la reunión de validación de requisitos: RF-05 estaba redactado de forma muy general y debía incluir lugar, fecha y hora, participantes, objetivos, acuerdos y firmas digitales (sección "Validación de Requisitos"). Esa observación se usa en HU-04 y HU-13.

## Requisitos funcionales

| Requisito (AppWeb) | Qué pide | Fuente según el documento | Sección del documento | Historia en SIC-UTEQ |
|---|---|---|---|---|
| RF-01 | Registrar la propuesta de anteproyecto verificando archivo y formato | Equipo de análisis | Requisitos Funcionales; Historias de Usuario (HU-01) | HU-01 |
| RF-02 | Revisión de la comisión formativa con observaciones obligatorias | Comisión formativa | Requisitos Funcionales; Historias de Usuario (HU-02); CU02 | HU-02 |
| RF-03 | Asignar director y tribunal, automática o manualmente, por disponibilidad y especialidad | Coordinación | Requisitos Funcionales; Tabla de Trazabilidad | HU-09 |
| RF-04 | Registrar el anteproyecto final y su calificación con observaciones y fecha | Docente Titulación I | Requisitos Funcionales; CU03 | HU-10 |
| RF-05 | Actas digitales de reuniones con firmas electrónicas | Director | Requisitos Funcionales; Historias de Usuario (HU-04, HU-05); Validación de Requisitos | HU-04, HU-05 |
| RF-06 | Observaciones y calificaciones del tribunal por etapa | Coordinación | Requisitos Funcionales; Historias de Usuario (HU-06) | HU-06 |
| RF-07 | Carga de documentos habilitantes en PDF | Coordinación | Requisitos Funcionales; Historias de Usuario (HU-05); CU09 | HU-05 |
| RF-08 | Acta de calificación generada automáticamente con firmas digitales | Coordinación | Requisitos Funcionales; Historias de Usuario (HU-07) | HU-07 |
| RF-09 | Validar la autenticidad de documentos legales y guardarlos de forma segura | Coordinación | Requisitos Funcionales; Historias de Usuario (HU-08); CU10; CU13 | HU-08, HU-11 |
| RF-10 | Notificaciones automáticas por correo institucional | Equipo de análisis | Requisitos Funcionales; Historias de Usuario (HU-03) | HU-03 |
| RF-11 | Aprobar o rechazar el tema antes de la carga formal | Comisión formativa | Requisitos Funcionales; Tabla de Trazabilidad; CU01; CU02 | HU-12 |
| RF-12 | Planificar reuniones con fecha, hora, modalidad y objetivo | Coordinación | Requisitos Funcionales; CU04 | HU-13 |

## Requisitos no funcionales y de negocio

| Requisito (AppWeb) | Qué pide | Fuente según el documento | Sección del documento | Relación con SIC-UTEQ |
|---|---|---|---|---|
| RNF-01 | Respuesta en menos de 3 s para al menos el 95 % de las solicitudes | Equipo de análisis | Requisitos de Calidad No Funcionales | Origen de RNF-01 de SIC-UTEQ, más exigente y con 150 usuarios simultáneos |
| RNF-02 | Disponibilidad de al menos 95 % durante el semestre | Equipo de análisis | Requisitos de Calidad No Funcionales; Restricciones (disponibilidad operativa) | Origen de RNF-04 de SIC-UTEQ (99,5 % en las 72 horas previas a una fecha límite) |
| RNF-03 | Interfaz comprensible para usuarios sin conocimientos técnicos | Entrevistas a usuarios | Requisitos de Calidad No Funcionales | Sin RNF en SIC-UTEQ; la usabilidad para estudiantes queda fuera de los objetivos |
| RNF-04 | TLS 1.3, cifrado de documentos y datos personales, control por roles | Buenas prácticas de seguridad | Requisitos de Calidad No Funcionales; Restricciones (seguridad de la información) | Origen de RNF-07 (integridad) y RNF-08 (autenticidad) de SIC-UTEQ |
| RNF-05 | Acceso desde computadoras y móviles | Equipo de análisis | Requisitos de Calidad No Funcionales | Sin RNF en SIC-UTEQ |
| RNF-06 | Restauración con copias de seguridad diarias | Coordinación de carrera | Requisitos de Calidad No Funcionales | RNF-06 de SIC-UTEQ (recuperación) |
| RB-01 | Cumplir el reglamento de titulación vigente | Reglamento de titulación | Requisitos de Negocio | Regla general de todas las historias |
| RB-02 | Calificar solo si hay actas de reuniones previas | Coordinación de carrera | Requisitos de Negocio | Condición previa de HU-06 y HU-07 |
| RB-03 | Controlar fechas de presentación, tutorías y sustentación según el cronograma | Coordinación de carrera | Requisitos de Negocio | HU-12 y el escenario de fechas límite de RNF-01 |
| RIE-01 | Integración con el SGA | Normativa institucional | Requisitos de Interfaz Externa | Fuera del alcance; el documento la deja como trabajo futuro en "Recomendaciones" |

RNF-02 de SIC-UTEQ (capacidad de recepción de documentos) no tiene un requisito equivalente en el documento de Aplicaciones Web. Su origen es el análisis del equipo a partir de los plazos del Reglamento de la UIC; el límite de 10 MB por archivo viene de HU-01.

## Lo que se tomó del reglamento

| Artículo | Qué dice | Dónde se usa |
|---|---|---|
| Art. 49, literal a | La propuesta se presenta en PDF a la Comisión de Investigación Formativa entre las semanas 1 y 4 del penúltimo nivel | Contexto (plazos) |
| Art. 49, literal b | La comisión traslada la propuesta en 5 días hábiles; el Consejo Directivo la analiza en 10 días hábiles, emite la resolución de aprobación y designa director y tribunal en los primeros 15 días hábiles del último nivel | Contexto (plazos e interesados); HU-09 |
| Art. 52 y 55 | Perfil del director y de los integrantes del tribunal; no fijan un número máximo de dirigidos por docente | HU-09 (el cupo queda como parámetro opcional de la coordinación) |
| Art. 54 | El director se reúne con el estudiante al menos dos veces al mes | Contexto (plazos) |
| Art. 57, numeral 1 | Mínimo cinco asesorías por corte evaluativo, con informes y actas enviados en la última semana del corte | Contexto (plazos); HU-13 |
| Art. 57, numeral 2 | Menos de 10 % de coincidencia en COMPILATIO antes de la predefensa | Contexto (plazos) |

## Notas

- uteq-export no está en uso en la carrera; es un prototipo que solo se probó con usuarios en la evaluación SUS (confirmado por el equipo el 22 de noviembre de 2026).
- El documento de Aplicaciones Web describe la pérdida de información, los retrasos y los errores de control sin cifras. Se revisaron sus 3858 líneas. El contexto describe esas consecuencias sin números y las apoya en los plazos del reglamento.

## Inconsistencias encontradas en el documento de Aplicaciones Web

- La Tabla de Trazabilidad asocia RF-03 a "CU08 – Asignar Director y Tribunal (ajuste: nuevo caso de uso)", pero en "Casos de uso principales" CU08 es "Registrar notas parciales". El caso de uso de asignación de director y tribunal no está descrito.
- HU-08 dice "(→ CU-11 / RF-09)", pero la validación de documentos habilitantes es CU10; CU11 es "Validar acta de calificación".
- RF-03 dice que el coordinador asigna director y tribunal, pero según el reglamento los designa el Consejo Directivo de Facultad (Art. 49, literal b). HU-09 se redactó así: la coordinación prepara la propuesta y registra la designación.
