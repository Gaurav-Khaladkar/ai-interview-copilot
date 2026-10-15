# ============================================================================
# AI Interview Copilot - Launch JavaFX Desktop Client (PowerShell)
# ============================================================================

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
$desktopJar = "$projectRoot\desktop-app\target\desktop-app-1.0.0.jar"

if (!(Test-Path $desktopJar)) {
    Write-Host "Desktop JAR not found. Building project..." -ForegroundColor Yellow
    & "$projectRoot\scripts\build.ps1"
}

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host " Launching AI Interview Copilot Windows Desktop Client               " -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan

java --enable-native-access=ALL-UNNAMED -jar $desktopJar
