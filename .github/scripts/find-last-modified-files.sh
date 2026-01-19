#!/bin/bash

################################################################################
# Script: find-last-modified-files.sh
# Descripción: Busca los tres últimos ficheros modificados en el repositorio Git
# Autor: Damián Nogueiras
# Fecha: Xaneiro 2026
################################################################################

set -euo pipefail

# Colores para la salida
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Número de ficheros a mostrar (por defecto 3)
NUM_FILES=${1:-3}

# Verificar que estamos en un repositorio Git
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}Error:${NC} No es un repositorio Git válido."
    exit 1
fi

# Obtener los últimos ficheros modificados
echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  Últimos ${NUM_FILES} ficheros modificados en el repositorio${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Obtener información de los commits más recientes
git log --pretty=format: --name-only -n 50 | \
    grep -v '^$' | \
    awk '!a[$0]++' | \
    head -n "${NUM_FILES}" | \
    while IFS= read -r file; do
        # Obtener la fecha de última modificación del fichero
        last_commit_date=$(git log -1 --format=%ai -- "$file" 2>/dev/null || echo "N/A")
        last_commit_hash=$(git log -1 --format=%h -- "$file" 2>/dev/null || echo "N/A")
        last_commit_author=$(git log -1 --format=%an -- "$file" 2>/dev/null || echo "N/A")

        # Mostrar la información
        echo -e "${GREEN}Ficheiro:${NC} $file"
        echo -e "${YELLOW}├─ Última modificación:${NC} $last_commit_date"
        echo -e "${YELLOW}├─ Commit:${NC} $last_commit_hash"
        echo -e "${YELLOW}└─ Autor:${NC} $last_commit_author"
        echo ""
    done

echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}✓ Script completado exitosamente${NC}"

