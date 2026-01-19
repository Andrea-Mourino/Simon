---
description: 'Mejores prácticas y convenciones de scripting de shell para bash, sh, zsh y otros shells'
applyTo: '**/*.sh'
---

# Directrices de Scripting de Shell

Instrucciones para escribir scripts de shell limpios, seguros y mantenibles para bash, sh, zsh y otros shells.

## Principios Generales

- Genera código que sea limpio, simple y conciso
- Asegúrate de que los scripts sean fácilmente legibles y comprensibles
- Añade comentarios donde sean útiles para entender cómo funciona el script
- Genera salidas de `echo` concisas y simples para proporcionar el estado de la ejecución
- Evita salidas de `echo` innecesarias y el registro excesivo
- Usa `shellcheck` para análisis estático cuando esté disponible
- Asume que los scripts son para automatización y pruebas en lugar de sistemas de producción, a menos que se especifique lo contrario
- Prefiere expansiones seguras: comillas dobles en las referencias a variables (`"$var"`), usa `${var}` para mayor claridad y evita `eval`
- Usa características modernas de Bash (`[[ ]]`, `local`, arrays) cuando los requisitos de portabilidad lo permitan; recurre a construcciones POSIX solo cuando sea necesario
- Elige analizadores fiables para datos estructurados en lugar de procesamiento de texto ad-hoc

## Manejo de Errores y Seguridad

- Habilita siempre `set -euo pipefail` para fallar rápidamente en caso de errores, capturar variables no establecidas y mostrar fallos en las tuberías
- Valida todos los parámetros requeridos antes de la ejecución
- Proporciona mensajes de error claros con contexto
- Usa `trap` para limpiar recursos temporales o manejar salidas inesperadas cuando el script termina
- Declara valores inmutables con `readonly` (o `declare -r`) para evitar reasignaciones accidentales
- Usa `mktemp` para crear archivos o directorios temporales de forma segura y asegúrate de que se eliminen en tu manejador de limpieza

## Estructura del Script

- Comienza con un shebang claro: `#!/bin/bash` a menos que se especifique lo contrario
- Incluye un comentario de encabezado que explique el propósito del script
- Define los valores predeterminados para todas las variables en la parte superior
- Usa funciones para bloques de código reutilizables
- Crea funciones reutilizables en lugar de repetir bloques de código similares
- Mantén el flujo de ejecución principal limpio y legible

## Trabajar con JSON y YAML

- Prefiere analizadores dedicados (`jq` para JSON, `yq` para YAML, o `jq` sobre JSON convertido a través de `yq`) sobre el procesamiento de texto ad-hoc con `grep`, `awk` o la división de cadenas de shell
- Cuando `jq`/`yq` no estén disponibles o no sean apropiados, elige el siguiente analizador más fiable disponible en tu entorno y sé explícito sobre cómo debe usarse de forma segura
- Valida que los campos requeridos existan y maneja explícitamente las rutas de datos faltantes/inválidas (p. ej., comprobando el estado de salida de `jq` o usando `// empty`)
- Cita los filtros de jq/yq para evitar la expansión del shell y prefiere `--raw-output` cuando necesites cadenas de texto sin formato
- Trata los errores del analizador como fatales: combínalo con `set -euo pipefail` o comprueba el éxito del comando antes de usar los resultados
- Documenta las dependencias del analizador en la parte superior del script y falla rápidamente con un mensaje útil si `jq`/`yq` (u otras herramientas alternativas) son necesarias pero no están instaladas

```bash
#!/bin/bash

# ============================================================================
# Descripción del Script Aquí
# ============================================================================

set -euo pipefail

cleanup() {
    # Elimina recursos temporales o realiza otros pasos de limpieza según sea necesario
    if [[ -n "${TEMP_DIR:-}" && -d "$TEMP_DIR" ]]; then
        rm -rf "$TEMP_DIR"
    fi
}

trap cleanup EXIT

# Valores predeterminados
RESOURCE_GROUP=""
REQUIRED_PARAM=""
OPTIONAL_PARAM="default-value"
readonly SCRIPT_NAME="$(basename "$0")"

TEMP_DIR=""

# Funciones
usage() {
    echo "Uso: $SCRIPT_NAME [OPCIONES]"
    echo "Opciones:"
    echo "  -g, --resource-group   Grupo de recursos (requerido)"
    echo "  -h, --help            Muestra esta ayuda"
    exit 0
}

validate_requirements() {
    if [[ -z "$RESOURCE_GROUP" ]]; then
        echo "Error: El grupo de recursos es requerido"
        exit 1
    fi
}

main() {
    validate_requirements

    TEMP_DIR="$(mktemp -d)"
    if [[ ! -d "$TEMP_DIR" ]]; then
        echo "Error: no se pudo crear el directorio temporal" >&2
        exit 1
    fi
    
    echo "============================================================================"
    echo "Ejecución del Script Iniciada"
    echo "============================================================================"
    
    # Lógica principal aquí
    
    echo "============================================================================"
    echo "Ejecución del Script Completada"
    echo "============================================================================"
}

# Analizar argumentos
while [[ $# -gt 0 ]]; do
    case $1 in
        -g|--resource-group)
            RESOURCE_GROUP="$2"
            shift 2
            ;;
        -h|--help)
            usage
            ;;
        *)
            echo "Opción desconocida: $1"
            exit 1
            ;;
    esac
done

# Ejecutar función principal
main "$@"

```