# Windows 10 & Windows 11 Installation & Deployment Guide
## Project: AI Interview Copilot
### Version: 1.0.0

---

## 1. System Requirements

- **Operating System**: Windows 10 (64-bit, Version 2004 / Build 19041 or higher) or Windows 11.
- **CPU**: Dual-Core 2.0 GHz or higher (Intel Core i3/i5/i7/i9 or AMD Ryzen).
- **RAM**: Minimum 4 GB RAM (8 GB recommended for running local LLMs like `llama3:8b`).
- **Disk Space**: 800 MB for Java runtime and application binaries; 5 GB+ optional for offline GGUF/Ollama models.
- **Audio**: Working microphone (built-in or USB headset) and audio playback device.

---

## 2. Java Runtime Installation (Java 17 LTS or later)

The application requires Java 17 LTS or later (tested on Java 17, Java 21, and Java 24).

### Step 2.1: Verify Existing Java Version
Open PowerShell as Administrator and run:
```powershell
java -version
```
Expected output:
```
java version "17.0.12" 2024-07-16 LTS
Java(TM) SE Runtime Environment (build 17.0.12+8-LTS-286)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.12+8-LTS-286, mixed mode, sharing)
```

### Step 2.2: Configure JAVA_HOME and PATH Environment Variables
If Java is installed in `C:\Program Files\Java\jdk-17`:
```powershell
[System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "Machine")
$currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
if ($currentPath -notlike "*%JAVA_HOME%\bin*") {
    [System.Environment]::SetEnvironmentVariable("Path", "$currentPath;C:\Program Files\Java\jdk-17\bin", "Machine")
}
```

---

## 3. Apache Maven Installation

Maven 3.9+ is bundled in `C:\Users\hp\tools\maven\apache-maven-3.9.9\bin` or can be added to PATH:
```powershell
$userPath = [System.Environment]::GetEnvironmentVariable("Path", "User")
$mavenBin = "C:\Users\hp\tools\maven\apache-maven-3.9.9\bin"
if ($userPath -notlike "*$mavenBin*") {
    [System.Environment]::SetEnvironmentVariable("Path", "$userPath;$mavenBin", "User")
}
$env:Path = "$env:Path;$mavenBin"
mvn -v
```

---

## 4. Database Setup

### Option A: Local SQLite (Recommended for 100% Offline Standalone Use)
Zero manual setup required! The application automatically provisions and maintains the local database at:
```
./ai-copilot.db
```
Hibernate and Spring Data automatically apply DDL schemas and pre-populate seed data on first startup.

### Option B: PostgreSQL (For Team or Centralized Enterprise Deployments)
1. Install PostgreSQL 14+ on Windows.
2. Open `psql` or pgAdmin and create the database:
   ```sql
   CREATE DATABASE aicopilot;
   ```
3. Execute the schema migration script:
   ```powershell
   psql -U postgres -d aicopilot -f database\migrations\V1__schema_postgres.sql
   psql -U postgres -d aicopilot -f database\seeds\V2__seed_data.sql
   ```
4. Configure environment variables in Windows:
   ```powershell
   $env:DB_HOST = "localhost"
   $env:DB_PORT = "5432"
   $env:DB_NAME = "aicopilot"
   $env:DB_USER = "postgres"
   $env:DB_PASSWORD = "your_secure_password"
   ```

---

## 5. Offline Local AI Models Setup (Optional for Local LLM)

To run offline neural models directly on your Windows hardware:
1. Download Ollama for Windows from [https://ollama.com](https://ollama.com).
2. Open PowerShell and pull your preferred offline models:
   ```powershell
   # 8 Billion parameter model (Recommended: 8GB RAM, 6GB VRAM)
   ollama pull llama3:8b

   # Ultra-lightweight model (Runs fast on any standard CPU: 4GB RAM)
   ollama pull phi3:mini
   ```
3. Verify local daemon status:
   ```powershell
   curl http://localhost:11434/api/tags
   ```
4. The AI Interview Copilot will automatically discover and route offline queries to `http://localhost:11434` when Offline Mode is active.

---

## 6. Building and Running the Application

### 6.1 Build the Multi-Module Project
```powershell
cd "C:\Users\hp\Desktop\Mentore Solution\Projects\ai-interview-copilot"
.\scripts\build.ps1
```
Or double-click `scripts\build.bat`.

### 6.2 Launch the JavaFX Desktop Client
```powershell
.\scripts\launch-desktop.ps1
```
Or double-click `scripts\launch-desktop.bat`.

### 6.3 Run Backend Server Independently (Optional)
```powershell
.\scripts\start-backend.ps1 -Profile sqlite -Port 8080
```
API Documentation and Swagger explorer: `http://localhost:8080/swagger-ui/index.html`.

---

## 7. Windows Packaging & Installer Generation

To package a standalone Windows distribution bundle:
```powershell
.\scripts\package-windows.ps1
```
The output directory will contain:
`dist\AI-Interview-Copilot-Windows\AI-Interview-Copilot.bat` which can be run on any Windows 10/11 system without Maven installed.

---

## 8. Troubleshooting Common Windows Errors

| Issue | Cause | Solution |
| :--- | :--- | :--- |
| `java: command not found` | Java binary not in system PATH | Set `JAVA_HOME` and append `%JAVA_HOME%\bin` to PATH as detailed in Section 2. |
| `Microphone input line not supported` | Windows privacy setting blocking desktop mic | Open Windows Settings -> Privacy & Security -> Microphone -> Toggle "Let desktop apps access your microphone" to ON. |
| `Port 8080 already in use` | Another service running on port 8080 | Run `scripts\start-backend.ps1 -Port 8085` or terminate PID using `netstat -ano \| findstr :8080`. |
| `TargetDataLine line unavailable` | Another audio app has exclusive mic lock | Open Windows Sound Settings -> Microphone Properties -> Advanced -> Uncheck "Allow applications to take exclusive control". |
