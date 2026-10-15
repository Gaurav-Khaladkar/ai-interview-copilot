# ============================================================================
# AI Interview Copilot - Windows Build Script (PowerShell)
# Builds all modules, runs automated test suites, and produces executable JARs
# ============================================================================

$ErrorActionPreference = "Stop"

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host " Building AI Interview Copilot for Windows 10/11                      " -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
Set-Location $projectRoot

# Locate Maven
$mvnCmd = "mvn"
if (!(Get-Command "mvn" -ErrorAction SilentlyContinue)) {
    if (Test-Path "C:\Users\hp\tools\maven\apache-maven-3.9.9\bin\mvn.cmd") {
        $mvnCmd = "C:\Users\hp\tools\maven\apache-maven-3.9.9\bin\mvn.cmd"
    } else {
        Write-Error "Maven could not be found. Please ensure Maven is installed and in PATH."
    }
}

Write-Host "`n[1/3] Compiling and running tests across all modules..." -ForegroundColor Yellow
& $mvnCmd clean test

if ($LASTEXITCODE -ne 0) {
    Write-Error "Test suite execution failed."
}

Write-Host "`n[2/3] Packaging standalone executable artifacts..." -ForegroundColor Yellow
& $mvnCmd package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Error "Packaging failed."
}

Write-Host "`n[3/3] Build Completed Successfully!" -ForegroundColor Green
Write-Host "Backend Executable JAR: $projectRoot\backend-api\target\backend-api-1.0.0-exec.jar" -ForegroundColor White
Write-Host "Desktop Standalone JAR:  $projectRoot\desktop-app\target\desktop-app-1.0.0.jar" -ForegroundColor White
Write-Host "======================================================================" -ForegroundColor Cyan
