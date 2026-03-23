@echo off
REM Script de inicialización rápida para Docker (Windows)

setlocal enabledelayedexpansion

REM Colores (usando color)
color 0B

cls
echo.
echo =====================================
echo   Inicializador Docker - Bolsa
echo =====================================
echo.

REM 1. Verificar Docker
echo [1/5] Verificando Docker...
docker --version >nul 2>&1
if errorlevel 1 (
    echo X Docker no esta instalado
    echo   Descargar desde: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)
for /f "tokens=*" %%a in ('docker --version') do set DOCKER_VERSION=%%a
echo + %DOCKER_VERSION%

REM 2. Verificar Docker Compose
echo [2/5] Verificando Docker Compose...
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo X Docker Compose no esta instalado
    pause
    exit /b 1
)
for /f "tokens=*" %%a in ('docker-compose --version') do set COMPOSE_VERSION=%%a
echo + %COMPOSE_VERSION%

REM 3. Verificar archivos necesarios
echo [3/5] Verificando archivos...
setlocal enabledelayedexpansion

for %%F in (Dockerfile docker-compose.yml BolsaEmpleo.sql pom.xml) do (
    if not exist "%%F" (
        echo X Archivo %%F no encontrado
        pause
        exit /b 1
    )
    echo + %%F
)

REM 4. Construir imagen
echo.
echo [4/5] Construyendo imagen Docker...
docker-compose build --no-cache
if errorlevel 1 (
    echo X Error al construir imagen
    pause
    exit /b 1
)
echo + Imagen construida exitosamente

REM 5. Iniciar servicios
echo.
echo [5/5] Iniciando servicios...
docker-compose up -d
if errorlevel 1 (
    echo X Error al iniciar servicios
    pause
    exit /b 1
)
echo + Servicios iniciados

REM Esperar a que los servicios se inicialicen
echo.
echo Esperando a que los servicios se inicialicen...
timeout /t 10 /nobreak

REM Mostrar status
echo.
echo =====================================
echo   Estado de Servicios
echo =====================================
docker-compose ps

echo.
echo =====================================
echo   + Inicializacion Completada
echo =====================================
echo.
echo Informacion util:
echo   Aplicacion:  http://localhost:8080
echo   MySQL:       localhost:3306
echo   Usuario BD:  bolsa_user
echo   Contraseña:  bolsa1234
echo.
echo Comandos utiles:
echo   Ver logs:       docker-compose logs -f
echo   Detener:        docker-compose down
echo   Conectar MySQL: docker-compose exec mysql mysql -u bolsa_user -p
echo.
pause
