#!/bin/bash

################################################################################
# Script: find-last-modified-files-simple.sh
# Descripción: Versión simple que busca los últimos ficheros modificados
# Autor: Damián Nogueiras
# Fecha: Xaneiro 2026
################################################################################

set -euo pipefail

# Número de ficheros a mostrar (por defecto 3)
NUM_FILES=${1:-3}

# Verificar que estamos en un repositorio Git
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo "Error: No es un repositorio Git válido."
    exit 1
fi

echo "Los ${NUM_FILES} últimos ficheros modificados en el repositorio:"
echo "=============================================================="
echo ""

# Obtener los últimos ficheros modificados de forma simple
git log --pretty=format: --name-only | grep -v '^$' | awk '!a[$0]++' | head -n "${NUM_FILES}"

echo ""
echo "=============================================================="

