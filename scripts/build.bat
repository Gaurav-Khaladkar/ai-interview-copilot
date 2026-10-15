@echo off
REM ============================================================================
REM AI Interview Copilot - Windows Batch Build Script
REM ============================================================================
setlocal enabledelayedexpansion

echo ======================================================================
echo  Building AI Interview Copilot for Windows 10/11
echo ======================================================================

set SCRIPT_DIR=%~dp0
cd /d "%SCRIPT_DIR%.."

set MVN_CMD=mvn
where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    if exist "C:\Users\hp\tools\maven\apache-maven-3.9.9\bin\mvn.cmd" (
        set MVN_CMD="C:\Users\hp\tools\maven\apache-maven-3.9.9\bin\mvn.cmd"
    ) else (
        echo [ERROR] Maven not found in PATH or standard location.
        exit /b 1
    )
)

echo.
echo [1/2] Compiling and running automated tests...
call %MVN_CMD% clean test
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Test execution failed.
    exit /b %ERRORLEVEL%
)

echo.
echo [2/2] Packaging modules into standalone JARs...
call %MVN_CMD% package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Packaging failed.
    exit /b %ERRORLEVEL%
)

echo.
echo ======================================================================
echo  BUILD SUCCESSFUL!
echo ======================================================================
pause
