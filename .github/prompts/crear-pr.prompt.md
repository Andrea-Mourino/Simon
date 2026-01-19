---
agent: 'agent'
description: 'Crear Pull Request en GitHub para solicitud de funcionalidad desde archivo de especificación usando template pull_request_template.md.'
tools: ['search/codebase', 'search', 'github', 'create_pull_request', 'update_pull_request', 'get_pull_request_diff']
---
# Crear Pull Request en GitHub desde Especificación

Crear Pull Request en GitHub para la especificación en `${workspaceFolder}/.github/pr-template.md` .

## Proceso

1. Analizar archivo template de especificación desde '${workspaceFolder}/.github/pull_request_template.md' para extraer requisitos usando la herramienta 'search'.
2. Crear template de borrador de pull request usando la herramienta 'create_pull_request' en `${input:targetBranch}`. Verificar que no exista ningún pull request de la rama actual usando `get_pull_request`. Si existe, continúa al paso 4 y omite el paso 3.
3. Obtener cambios en el pull request usando la herramienta 'get_pull_request_diff' para analizar la información que se cambió en el Pull Request.
4. Actualizar el cuerpo y título del pull request creado en el paso anterior usando la herramienta 'update_pull_request'. Incorporar la información del template obtenido en el primer paso para actualizar el cuerpo y título según sea necesario.
5. Cambiar de borrador a listo para revisión usando la herramienta 'update_pull_request'. Para actualizar el estado del pull request.
6. Usar 'get_me' para obtener el nombre de usuario de la persona que creó el pull request y asignarlo usando la herramienta `update_issue`. Para asignar el pull request.
7. Responder con la URL del Pull Request creado al usuario.

## Requisitos
- Un único pull request para la especificación completa
- Título claro/pull_request_template.md que identifique la especificación
- Llenar suficiente información en el pull_request_template.md
- Verificar contra pull requests existentes antes de la creación