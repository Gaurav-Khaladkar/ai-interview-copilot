@echo off
REM ============================================================================
REM AI Interview Copilot - Start Backend Server (Batch)
REM ============================================================================
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

set JAR_PATH=backend-api\target\backend-api-1.0.0-exec.jar

if not exist "%JAR_PATH%" (
    echo Executable JAR not found. Please run scripts\build.bat first.
    pause
    exit /b 1
)

echo Starting AI Interview Copilot Backend API on port 8080 (SQLite Profile)...
java -Dspring.profiles.active=sqlite -Dserver.port=8080 -jar "%JAR_PATH%"
pause
