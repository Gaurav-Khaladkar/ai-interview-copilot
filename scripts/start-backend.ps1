# ============================================================================
# AI Interview Copilot - Start Backend Server (PowerShell)
# Launches the Spring Boot REST API with SQLite (default) or PostgreSQL
# ============================================================================

param(
    [string]$Profile = "sqlite",
    [int]$Port = 8080
)

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
$jarPath = "$projectRoot\backend-api\target\backend-api-1.0.0-exec.jar"

if (!(Test-Path $jarPath)) {
    Write-Host "Executable JAR not found. Building backend..." -ForegroundColor Yellow
    & "$projectRoot\scripts\build.ps1"
}

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host " Starting AI Interview Copilot Backend API                           " -ForegroundColor Cyan
Write-Host " Profile: $Profile | Port: $Port                                     " -ForegroundColor Cyan
Write-Host " Swagger Docs: http://localhost:$Port/swagger-ui/index.html          " -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan
Push-Location $projectRoot
try {
    java "-Dspring.profiles.active=$Profile" "-Dserver.port=$Port" -jar "$jarPath"
}
finally {
    Pop-Location
}
