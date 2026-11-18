# SIC-UTEQ: Sistema distribuido de gestión de la Unidad de Integración Curricular

Proyecto Fin de Curso de Aplicaciones Distribuidas (UTEQ, 7.º B). Entrega 1: definición, requisitos y arquitectura base.

## Integrantes y roles

| Integrante | Rol |
|---|---|
| Bueno Troya Erwin Daniel | Arquitecto y Líder de Desarrollo |
| Castro Coello Mario Sebastián | Responsable de Calidad |
| Macías Vásquez César Abrahán | Responsable de Documentación |

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

Pendiente.

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
