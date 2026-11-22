#!/usr/bin/env bash
# Crea las etiquetas y los issues de E1 y E2 (tablas de docs/secciones/13-plan.tex) con GitHub CLI (gh).
#
# Requisitos: gh instalado y con sesión iniciada (gh auth login), y los cuatro integrantes
# ya aceptaron la invitación de colaborador (si no, el issue se crea pero no se puede asignar).
#
# Uso, desde la raíz del repositorio en Git Bash:
#   LEONEL=<usuario-github> DAYVER=<usuario-github> bash docs/plan/crear-issues.sh
# Para agregarlos también al tablero de GitHub Projects (número que sale en la URL del tablero):
#   gh auth refresh -s project
#   PROYECTO_NUM=1 LEONEL=<usuario> DAYVER=<usuario> bash docs/plan/crear-issues.sh
# Para ver qué haría sin crear nada:
#   DRY_RUN=1 LEONEL=x DAYVER=y bash docs/plan/crear-issues.sh
#
# Se puede correr más de una vez: si ya existe un issue con el mismo título, no lo repite.

set -euo pipefail

REPO="Troyano2004/TA-PFC-E1"
DUENO="Troyano2004"
DANIEL="${DANIEL:-Troyano2004}"
ELIAN="${ELIAN:-Elianmen123}"
LEONEL="${LEONEL:?Falta el usuario de GitHub de Leonel. Uso: LEONEL=usuario DAYVER=usuario bash docs/plan/crear-issues.sh}"
DAYVER="${DAYVER:?Falta el usuario de GitHub de Dayver. Uso: LEONEL=usuario DAYVER=usuario bash docs/plan/crear-issues.sh}"
PROYECTO_NUM="${PROYECTO_NUM:-}"
DRY_RUN="${DRY_RUN:-0}"

gh_() {
  if [ "$DRY_RUN" = "1" ]; then
    echo "  [simulado] gh $*" >&2
    if [ "$1 $2" = "issue create" ]; then echo "https://github.com/$REPO/issues/0"; fi
    return 0
  fi
  gh "$@"
}

if [ "$DRY_RUN" != "1" ]; then
  gh auth status >/dev/null 2>&1 || { echo "Primero inicia sesión: gh auth login"; exit 1; }
fi

gh_ label create E1 --repo "$REPO" --color 1D76DB --description "Entrega 1" --force >/dev/null
gh_ label create E2 --repo "$REPO" --color 5319E7 --description "Entrega 2" --force >/dev/null

EXISTENTES=""
if [ "$DRY_RUN" != "1" ]; then
  EXISTENTES="$(gh issue list --repo "$REPO" --state all --limit 500 --json title --jq '.[].title')"
fi

CUERPO="$(mktemp)"
trap 'rm -f "$CUERPO"' EXIT

crear() {
  local titulo="$1" etiqueta="$2" asignado="$3" descripcion="$4" cierre="$5"
  if grep -Fxq -- "$titulo" <<< "$EXISTENTES"; then
    echo "Ya existe, se omite: $titulo"
    return 0
  fi
  printf '%s\n\nCriterio de cierre: %s\n' "$descripcion" "$cierre" > "$CUERPO"
  local url
  url="$(gh_ issue create --repo "$REPO" --title "$titulo" --label "$etiqueta" --body-file "$CUERPO")"
  echo "$url  $titulo"
  gh_ issue edit "$url" --add-assignee "$asignado" >/dev/null \
    || echo "  No se pudo asignar a $asignado. ¿Ya aceptó la invitación? Asígnalo a mano en GitHub."
  if [ -n "$PROYECTO_NUM" ]; then
    gh_ project item-add "$PROYECTO_NUM" --owner "$DUENO" --url "$url" >/dev/null \
      || echo "  No se pudo agregar al tablero $PROYECTO_NUM. Revisa 'gh auth refresh -s project' y tu acceso al tablero."
  fi
}

# ---------- E1 ----------
crear "[E1] Tablero e issues de E1 y E2" E1 "$ELIAN" \
  "Crear las etiquetas y los issues de E1 y E2 con docs/plan/crear-issues.sh, asignarlos y agregarlos al tablero de GitHub Projects. Sección Plan del proyecto en docs/secciones/13-plan.tex." \
  "todos los issues de la sección Plan del proyecto están en el tablero con su responsable."
crear "[E1] Servicio ms-usuarios con datos en memoria" E1 "$ELIAN" \
  "Spring Boot 4.0.2 con Java 17 y Maven Wrapper en src/ms-usuarios. GET /api/usuarios (filtro opcional por rol) y GET /api/usuarios/{id} con 404 si no existe. Puerto configurable con USUARIOS_PORT." \
  "compila, pasan sus pruebas (mvnw test) y responde en el puerto 8081."
crear "[E1] Servicio ms-propuestas que consulta a ms-usuarios por REST" E1 "$ELIAN" \
  "src/ms-propuestas: GET /api/propuestas y GET /api/propuestas/{id} con el estudiante y el tutor pedidos a ms-usuarios con RestClient. Tiempo límite configurable (USUARIOS_TIMEOUT_MS) y respuesta 503 si ms-usuarios no responde." \
  "devuelve la respuesta combinada con los dos servicios en marcha y 503 con ms-usuarios apagado."
crear "[E1] Instrucciones de ejecución y capturas del esqueleto" E1 "$ELIAN" \
  "Sección Ejecución del esqueleto del README, sección del documento en docs/secciones/14-esqueleto.tex y las 3 capturas en evidencias/capturas/, con usuario, máquina, fecha y hora visibles." \
  "otra persona puede levantar los dos servicios siguiendo solo el README."
crear "[E1] ADR-001: estilo de arquitectura" E1 "$DANIEL" \
  "docs/adr/ADR-001-estilo-arquitectura.md con contexto, opciones consideradas, decisión y consecuencias, y la justificación de la arquitectura distribuida en docs/secciones/07-justificacion.tex." \
  "el ADR está en el repositorio y resumido en el documento."
crear "[E1] ADR-002: mecanismo de comunicación" E1 "$DANIEL" \
  "docs/adr/ADR-002-comunicacion.md: REST como mecanismo principal y mensajería para los avisos, con los valores medidos en PE-U1. Resumen de los dos ADR en docs/secciones/11-adr.tex." \
  "el ADR está en el repositorio, sin pendientes, y resumido en el documento."
crear "[E1] Diagramas C4 de contexto y de contenedores" E1 "$DANIEL" \
  "docs/diagramas/c4-nivel1-contexto.drawio y docs/diagramas/c4-nivel2-contenedores.drawio, editables en draw.io, exportados a docs/figuras/ y descritos en docs/secciones/08-arquitectura-c4.tex." \
  "los dos diagramas abren en draw.io y aparecen en el PDF."
crear "[E1] Modelo de comunicación y análisis CAP/PACELC" E1 "$DANIEL" \
  "docs/secciones/09-comunicacion.tex: tipo, mecanismo y justificación de cada interacción del diagrama de contenedores. docs/secciones/10-cap-pacelc.tex: decisión CAP/PACELC por dato crítico." \
  "cada flecha del diagrama de contenedores tiene su fila en la tabla."
crear "[E1] Documento LaTeX y PDF para el SGA" E1 "$DANIEL" \
  "Secciones del documento que faltan (resumen, contexto, objetivos, requisitos funcionales y declaración de IA), PDF compilado en docs/ y PDF de una página para el SGA." \
  "main.pdf y sga.pdf compilan sin errores con pdflatex, biber, pdflatex, pdflatex."
crear "[E1] Requisitos no funcionales con ISO/IEC 25010" E1 "$LEONEL" \
  "docs/secciones/06-requisitos-no-funcionales.tex: al menos 6 requisitos cuantificados, cada uno con su característica de ISO/IEC 25010 y cómo se mide." \
  "ningún requisito queda sin número ni sin forma de medirlo."
crear "[E1] Análisis de riesgos técnicos" E1 "$LEONEL" \
  "docs/secciones/12-riesgos.tex: al menos 5 riesgos técnicos con probabilidad, impacto, nivel y mitigación." \
  "cada riesgo tiene una mitigación concreta y el RNF que protege."
crear "[E1] Referencias verificadas" E1 "$LEONEL" \
  "docs/referencias.bib con al menos 8 referencias de 2021 en adelante, 4 de revistas indexadas con DOI, y el apéndice docs/secciones/A1-verificacion-referencias.tex con la fuente de cada verificación." \
  "cada DOI resuelve y sus datos coinciden con Crossref."
crear "[E1] Riesgos derivados del incidente de FORM-AC-01" E1 "$LEONEL" \
  "R-08, R-09 y R-10 en docs/secciones/12-riesgos.tex, adaptados a microservicios a partir de las lecciones del incidente analizado en FORM-AC-01 (caída de Facebook del 4 de octubre de 2021)." \
  "los tres riesgos tienen la lección del incidente, probabilidad, impacto y mitigación."
crear "[E1] Evidencias de levantamiento y fotografías" E1 "$DAYVER" \
  "Fotografías de las entrevistas en evidencias/levantamiento/ y de las sesiones de trabajo en evidencias/fotos/, sin editar." \
  "las imágenes están en el repositorio tal como se tomaron."
crear "[E1] Trabajos relacionados" E1 "$DAYVER" \
  "docs/secciones/04-trabajos-relacionados.tex: 3 trabajos publicados con DOI, qué tomamos de cada uno y en qué nos diferenciamos." \
  "los tres trabajos están en referencias.bib y en el documento."
crear "[E1] Bitácora y plantilla de la tabla de aportes" E1 "$DAYVER" \
  "BITACORA.md al día con las sesiones del equipo y docs/secciones/15-aportes.tex con la estructura de la tabla de aportes." \
  "cada sesión de trabajo de E1 tiene su fila en la bitácora."
crear "[E1] Guion, video y tabla de aportes completa" E1 "$DAYVER" \
  "docs/guion-video.md, enlace del video en el README y docs/secciones/15-aportes.tex con los enlaces reales a commits e issues de cada integrante." \
  "el video dura entre 5 y 7 minutos y su enlace abre sin pedir permisos."

# ---------- E2 ----------
crear "[E2] Contratos OpenAPI de los servicios" E2 "$DANIEL" \
  "Contrato /api/v1 de ms-usuarios, ms-propuestas, ms-documentos y ms-evaluacion antes de programarlos." \
  "los contratos están en el repositorio y el gateway los usa para enrutar."
crear "[E2] Eventos de RabbitMQ y ms-notificaciones" E2 "$DANIEL" \
  "Nombres y contenido de los eventos, colas durables y ms-notificaciones que los consume sin repetir avisos." \
  "un cambio de estado del documento genera un solo aviso aunque el evento llegue dos veces."
crear "[E2] API Gateway con validación del JWT" E2 "$ELIAN" \
  "Spring Cloud Gateway con las rutas a cada servicio y a uteq-export para lo que no se ha migrado." \
  "una petición sin token recibe 401 y una con token llega al servicio correcto."
crear "[E2] Dockerfile por servicio y docker-compose.yml" E2 "$ELIAN" \
  "Imagen de cada servicio, RabbitMQ y health checks." \
  "todo el sistema arranca con docker compose up en una máquina limpia."
crear "[E2] ms-documentos: versiones del documento" E2 "$ELIAN" \
  "Subida de versiones, observaciones del director y validación de la propuesta aprobada contra ms-propuestas." \
  "no se acepta un documento si la propuesta no está APROBADA."
crear "[E2] Prueba de carga base (RNF-01 y RNF-02)" E2 "$LEONEL" \
  "Script de k6 y primera medición sobre el monolito y sobre los servicios de E2." \
  "los resultados (p95, errores, subidas por minuto) están en el repositorio."
crear "[E2] Pruebas de tolerancia a fallos (RNF-05)" E2 "$LEONEL" \
  "Detener cada servicio durante una prueba de carga y registrar qué responde el resto." \
  "hay un registro por servicio detenido con los códigos de respuesta de los demás."
crear "[E2] Documentación de la API y README de E2" E2 "$DAYVER" \
  "Swagger UI de cada servicio enlazado en el README e instrucciones de ejecución con Docker Compose." \
  "el README de E2 permite levantar y probar el sistema sin ayuda."
crear "[E2] Bitácora, evidencias y video de E2" E2 "$DAYVER" \
  "Mismo proceso de E1 para la segunda entrega." \
  "bitácora, capturas, tabla de aportes y video de E2 en el repositorio."

echo "Listo."
