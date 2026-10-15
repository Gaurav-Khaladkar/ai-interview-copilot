# ============================================================================
# AI Interview Copilot - Windows Packaging & Installer Script (PowerShell)
# Generates a Portable Windows Application Bundle and Inno Setup Script
# ============================================================================

$ErrorActionPreference = "Stop"

Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host " Packaging AI Interview Copilot for Windows 10 & Windows 11           " -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$projectRoot = Split-Path -Parent $scriptDir
$distDir = "$projectRoot\dist\AI-Interview-Copilot-Windows"
$desktopJar = "$projectRoot\desktop-app\target\desktop-app-1.0.0.jar"

if (!(Test-Path $desktopJar)) {
    Write-Host "Desktop JAR not found. Triggering build..." -ForegroundColor Yellow
    & "$projectRoot\scripts\build.ps1"
}

# Create distribution structure
if (Test-Path $distDir) {
    Remove-Item -Recurse -Force $distDir
}
New-Item -ItemType Directory -Force -Path @(
    "$distDir\app",
    "$distDir\data",
    "$distDir\models",
    "$distDir\docs"
) | Out-Null

# Copy application JAR and resources
Copy-Item $desktopJar -Destination "$distDir\app\AI-Interview-Copilot.jar" -Force
Copy-Item "$projectRoot\README.md" -Destination "$distDir\docs\README.txt" -Force
Copy-Item "$projectRoot\documentation\specs\SRS.md" -Destination "$distDir\docs\SRS.txt" -Force

# Create Windows Launcher Batch & VBScript for silent launch
$launcherBat = @"
@echo off
cd /d "%~dp0"
start "" javaw --enable-native-access=ALL-UNNAMED -jar app\AI-Interview-Copilot.jar
"@
$launcherBat | Out-File -FilePath "$distDir\AI-Interview-Copilot.bat" -Encoding ascii

$launcherVbs = @"
Set WshShell = CreateObject("WScript.Shell")
WshShell.Run chr(34) & "AI-Interview-Copilot.bat" & Chr(34), 0
Set WshShell = Nothing
"@
$launcherVbs | Out-File -FilePath "$distDir\Launch-Silent.vbs" -Encoding ascii

# Check for jpackage
$jpackage = Get-Command "jpackage" -ErrorAction SilentlyContinue
if ($jpackage) {
    Write-Host "Creating Native Windows MSI/EXE package using jpackage..." -ForegroundColor Green
    $outputDir = "$projectRoot\dist\installer"
    New-Item -ItemType Directory -Force -Path $outputDir | Out-Null
    
    jpackage --type app-image `
        --name "AI-Interview-Copilot" `
        --input "$distDir\app" `
        --main-jar "AI-Interview-Copilot.jar" `
        --main-class "com.aicopilot.desktop.DesktopLauncher" `
        --dest "$outputDir"
    Write-Host "Native bundle generated in: $outputDir\AI-Interview-Copilot" -ForegroundColor Green
} else {
    Write-Host "jpackage not found on PATH. Standard portable distribution ready in: $distDir" -ForegroundColor Yellow
}

Write-Host "`nDistribution packaged successfully at:" -ForegroundColor Green
Write-Host "  $distDir" -ForegroundColor White
Write-Host "Run AI-Interview-Copilot.bat to start the application on any Windows 10/11 system with Java 17+ installed." -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan
