# SIC-UTEQ: Sistema distribuido de gestión de la Unidad de Integración Curricular

Proyecto Fin de Curso de Aplicaciones Distribuidas (UTEQ, 7.º B). Entrega 1: definición, requisitos y arquitectura base.

## Integrantes y roles

| Integrante | Rol |
|---|---|
| Bueno Troya Erwin Daniel | Arquitecto |
| Mendoza Villarroel Elian Jossue | Líder de Desarrollo |
| Zambrano Triviño Leonel Leonidas | Responsable de Calidad |
| Rivas Piloso Dayver Yahir | Responsable de Documentación |

## Levantamiento de información

Entrevista con la Ing. Jessica Ponce, Coordinadora de la Carrera de Ingeniería de Software de la UTEQ, el jueves 19 de noviembre de 2026, grabada en video. Además se hizo una consulta complementaria a docentes de la carrera familiarizados con el proceso de titulación. Las fotografías están en `evidencias/levantamiento/` y la relación entre requisitos y fuentes en `docs/entrevista-hallazgos.md`.

Enlace al video de la entrevista: https://drive.google.com/drive/folders/1HLHANzWTiUYHDHOlk-jD68KpwNUQuwF_?usp=sharing

Al inicio de la grabación, la Ing. Jessica Ponce da su consentimiento expreso para ser grabada.

## Estructura

```
/src/              servicios del esqueleto
/docs/             main.tex, referencias.bib, figuras y PDF compilado
/docs/adr/         registros de decisiones de arquitectura
/docs/diagramas/   archivos editables (draw.io / Structurizr)
/evidencias/       acta o encuesta, fotografías y capturas
BITACORA.md
README.md
```

## Ejecución del esqueleto

Dos servicios Spring Boot 4.0.2 con Java 17 y datos en memoria. ms-propuestas consulta a ms-usuarios por REST y devuelve una respuesta combinada (ADR-002).

| Servicio | Carpeta | Puerto por defecto | Variable para cambiarlo |
|---|---|---|---|
| ms-usuarios | `src/ms-usuarios` | 8081 | `USUARIOS_PORT` |
| ms-propuestas | `src/ms-propuestas` | 8082 | `PROPUESTAS_PORT` |

ms-propuestas busca a ms-usuarios en `USUARIOS_URL` (por defecto `http://localhost:8081`) y espera como máximo `USUARIOS_TIMEOUT_MS` milisegundos (por defecto 2000).

### Requisitos

- JDK 17 o superior (`java -version`).
- No hace falta instalar Maven. Cada servicio trae Maven Wrapper (`mvnw` y `mvnw.cmd`), que descarga Maven 3.9.12 la primera vez.

### Arrancar los servicios

Windows, en dos ventanas de PowerShell abiertas en la raíz del repositorio:

```powershell
cd src\ms-usuarios
.\mvnw.cmd spring-boot:run
```

```powershell
cd src\ms-propuestas
.\mvnw.cmd spring-boot:run
```

Linux o macOS: lo mismo con `./mvnw spring-boot:run`. Si responde "Permission denied", ejecutar antes `chmod +x mvnw`.

Cada servicio está listo cuando su log muestra `ms-usuarios listo en http://localhost:8081/api/usuarios` o `ms-propuestas listo en http://localhost:8082/api/propuestas`.

### Probar la comunicación

En una tercera ventana:

```powershell
Invoke-RestMethod http://localhost:8082/api/propuestas/1 | ConvertTo-Json -Depth 5
```

o con curl en cualquier sistema:

```bash
curl http://localhost:8082/api/propuestas/1
```

Respuesta: el título, el estado y la fecha salen de ms-propuestas; `estudiante` y `tutor` los pidió ms-propuestas a ms-usuarios.

```json
{
  "id": 1,
  "titulo": "Aplicación móvil para el seguimiento de prácticas preprofesionales",
  "estado": "EN_REVISION",
  "fechaEnvio": "2026-11-10",
  "estudiante": { "id": 1, "nombres": "Andrea Paola Cedeño Mora", "correo": "acedeno@uteq.example", "rol": "ESTUDIANTE" },
  "tutor": { "id": 10, "nombres": "Carlos Alberto Ruiz León", "correo": "cruiz@uteq.example", "rol": "DOCENTE" },
  "usuariosConsultadosEn": "http://localhost:8081"
}
```

En el log de ms-propuestas aparece cada llamada con su tiempo, por ejemplo `GET http://localhost:8081/api/usuarios/1 -> 200 en 3 ms`.

### Endpoints

| Método y ruta | Servicio | Qué devuelve |
|---|---|---|
| `GET /api/usuarios` | ms-usuarios | Lista de usuarios. Filtro opcional `?rol=ESTUDIANTE`, `DOCENTE` o `COORDINADOR` |
| `GET /api/usuarios/{id}` | ms-usuarios | Un usuario, o 404 si no existe |
| `GET /api/propuestas` | ms-propuestas | Todas las propuestas con su estudiante y su tutor |
| `GET /api/propuestas/{id}` | ms-propuestas | Una propuesta combinada; 404 si no existe; 503 si ms-usuarios no responde |
| `GET /actuator/health` | los dos | Estado del servicio (`UP`) |

### Cambiar los puertos

PowerShell:

```powershell
$env:USUARIOS_PORT = "9081"; .\mvnw.cmd spring-boot:run
```

```powershell
$env:PROPUESTAS_PORT = "9082"; $env:USUARIOS_URL = "http://localhost:9081"; .\mvnw.cmd spring-boot:run
```

Linux o macOS:

```bash
USUARIOS_PORT=9081 ./mvnw spring-boot:run
PROPUESTAS_PORT=9082 USUARIOS_URL=http://localhost:9081 ./mvnw spring-boot:run
```

### Si ms-usuarios se cae

Con ms-propuestas en marcha, detener ms-usuarios (Ctrl+C) y repetir la llamada:

```powershell
curl.exe -i http://localhost:8082/api/propuestas/1
```

Responde `503 Service Unavailable` con `{"error":"ms-usuarios no está disponible", ...}` en lugar de quedarse esperando.

### Pruebas automáticas

En cada carpeta de servicio:

```powershell
.\mvnw.cmd test
```

ms-usuarios tiene 4 pruebas y ms-propuestas 5. Las de ms-propuestas no necesitan a ms-usuarios encendido: `MockRestServiceServer` responde en su lugar, incluido el caso del 503.

### Empaquetar

```powershell
.\mvnw.cmd package
java -jar target\ms-usuarios-0.1.0.jar
```

Los datos son ficticios: 6 usuarios (3 estudiantes, 2 docentes y 1 coordinador) y 3 propuestas, con correos del dominio reservado `uteq.example`. No hay base de datos hasta E3.

## Video de presentación

Pendiente.

## Compilación del documento

- Compilador: pdflatex (TeX Live o MiKTeX) con biber
- Archivo principal: `docs/main.tex`
- Dependencias: babel (spanish), biblatex (style=ieee), csquotes, booktabs, xurl, hyperref, graphicx, geometry, lmodern

```bash
cd docs
pdflatex main.tex
biber main
pdflatex main.tex
pdflatex main.tex
```

El PDF de una página para el SGA se genera con `pdflatex sga.tex` dentro de `docs/`.
