@echo off
setlocal
cd /d "%~dp0"

echo ======================================================================
echo  AI Interview Copilot - Windows Desktop Edition
echo ======================================================================

REM 1. Check if Backend is running
curl -s --connect-timeout 2 http://localhost:8080/api/health >nul 2>&1
if %ERRORLEVEL% equ 0 goto LAUNCH_GUI

echo [*] Starting Backend Server on port 8080...
if exist "app\backend-api.jar" (
    start "AI-Copilot-Backend" /min java -Dspring.profiles.active=sqlite -Dserver.port=8080 -jar "app\backend-api.jar"
) else (
    if exist "..\..\backend-api\target\backend-api-1.0.0-exec.jar" (
        start "AI-Copilot-Backend" /min java -Dspring.profiles.active=sqlite -Dserver.port=8080 -jar "..\..\backend-api\target\backend-api-1.0.0-exec.jar"
    )
)
ping 127.0.0.1 -n 3 >nul

:LAUNCH_GUI
echo [*] Launching Desktop GUI Dashboard...
java --enable-native-access=ALL-UNNAMED -jar "app\AI-Interview-Copilot.jar"
pause
