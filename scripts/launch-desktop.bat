@echo off
REM ============================================================================
REM AI Interview Copilot - Launch JavaFX Desktop Client (Batch)
REM ============================================================================
setlocal enabledelayedexpansion

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

set DESKTOP_JAR=desktop-app\target\desktop-app-1.0.0.jar

if not exist "%DESKTOP_JAR%" (
    echo Desktop JAR not found. Please run scripts\build.bat first.
    pause
    exit /b 1
)

echo Launching AI Interview Copilot Desktop Client...
start "" javaw --enable-native-access=ALL-UNNAMED -jar "%DESKTOP_JAR%"
