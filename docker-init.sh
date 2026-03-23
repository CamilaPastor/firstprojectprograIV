#!/bin/bash
# Script de inicialización rápida para Docker

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}  Inicializador Docker - Bolsa     ${NC}"
echo -e "${BLUE}=====================================${NC}"
echo ""

# 1. Verificar Docker
echo -e "${YELLOW}[1/5] Verificando Docker...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}✗ Docker no está instalado${NC}"
    echo "  Descargar desde: https://www.docker.com/products/docker-desktop"
    exit 1
fi
DOCKER_VERSION=$(docker --version)
echo -e "${GREEN}✓ ${DOCKER_VERSION}${NC}"

# 2. Verificar Docker Compose
echo -e "${YELLOW}[2/5] Verificando Docker Compose...${NC}"
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}✗ Docker Compose no está instalado${NC}"
    exit 1
fi
COMPOSE_VERSION=$(docker-compose --version)
echo -e "${GREEN}✓ ${COMPOSE_VERSION}${NC}"

# 3. Verificar archivos necesarios
echo -e "${YELLOW}[3/5] Verificando archivos...${NC}"
FILES=("Dockerfile" "docker-compose.yml" "BolsaEmpleo.sql" "pom.xml")
for file in "${FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo -e "${RED}✗ Archivo $file no encontrado${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ $file${NC}"
done

# 4. Construir imagen
echo ""
echo -e "${YELLOW}[4/5] Construyendo imagen Docker...${NC}"
docker-compose build --no-cache

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Imagen construida exitosamente${NC}"
else
    echo -e "${RED}✗ Error al construir imagen${NC}"
    exit 1
fi

# 5. Iniciar servicios
echo ""
echo -e "${YELLOW}[5/5] Iniciando servicios...${NC}"
docker-compose up -d

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Servicios iniciados${NC}"
else
    echo -e "${RED}✗ Error al iniciar servicios${NC}"
    exit 1
fi

# Esperar a que los servicios estén healthy
echo ""
echo -e "${YELLOW}Esperando a que los servicios se inicialicen...${NC}"
sleep 10

# Mostrar status
echo ""
echo -e "${BLUE}=====================================${NC}"
echo -e "${BLUE}  Estado de Servicios              ${NC}"
echo -e "${BLUE}=====================================${NC}"
docker-compose ps

echo ""
echo -e "${GREEN}=====================================${NC}"
echo -e "${GREEN}  ✓ Inicialización Completada     ${NC}"
echo -e "${GREEN}=====================================${NC}"
echo ""
echo -e "${BLUE}Información útil:${NC}"
echo -e "  🌐 Aplicación:  ${GREEN}http://localhost:8080${NC}"
echo -e "  🗄️  MySQL:      ${GREEN}localhost:3306${NC}"
echo -e "  👤 Usuario BD:  ${GREEN}bolsa_user${NC}"
echo -e "  🔑 Contraseña:  ${GREEN}bolsa1234${NC}"
echo ""
echo -e "${BLUE}Comandos útiles:${NC}"
echo -e "  Ver logs:       ${YELLOW}docker-compose logs -f${NC}"
echo -e "  Detener:        ${YELLOW}docker-compose down${NC}"
echo -e "  Conectar MySQL: ${YELLOW}docker-compose exec mysql mysql -u bolsa_user -p${NC}"
echo ""
