# Security, Privacy & Ethical Assessment Policy
## AI Interview Copilot — Desktop Application

---

## 1. Ethical Assessment Policy & Third-Party Compatibility

### 1.1 Non-Invasive Operating Footprint
The AI Interview Copilot is designed exclusively as an educational preparation tool for candidates. To ensure system stability and avoid interfering with other desktop software:
1. **Zero Process Hiding**: The application never disguises its executable name or process ID. In Windows Task Manager and PowerShell `Get-Process`, it displays transparently as `AI Interview Copilot` / `javaw.exe`.
2. **Zero Hooking / Anti-Cheat Evasion**: The application does not install kernel drivers, does not hook keyboard/mouse events at the Windows API level, does not inject DLLs into third-party processes, and does not capture screens of other applications.
3. **Proctoring Separation**: Candidates must conduct mock interviews as preparation *prior* to taking third-party proctored assessments. The application respects the integrity and terms of service of third-party evaluation platforms.

---

## 2. Application Security Architecture

### 2.1 Password Hashing & Authentication
- All passwords are encrypted using **BCrypt** with a computational work factor of **12** (`BCryptPasswordEncoder(12)`).
- Stateless **JSON Web Tokens (JWT)** are signed with HMAC-SHA256 and expire after 24 hours.
- Session tokens are stored in memory or encrypted local preferences; no passwords are stored in plain text.

### 2.2 Host Protection & Code Sandbox Isolation
To prevent candidate-written code or examples from performing destructive actions on the host system:
1. **Static AST & Lexical Inspection**: Every submitted code snippet is screened against a strict blacklist before execution:
   - Disallowed: `ProcessBuilder`, `Runtime.getRuntime`, `System.exit`, `ClassLoader`, `Unsafe`, `Socket`, `ServerSocket`, `File.delete`, `format c:`, `DROP DATABASE`.
   - Any detection immediately triggers `SECURITY_VIOLATION` status and prevents execution.
2. **Resource Boundaries**:
   - Hard execution timeout: 5000 milliseconds (prevents infinite loops).
   - Memory ceiling: 256 MB.

### 2.3 Audio & Microphone Privacy Controls
1. **Explicit Trigger Only**: Microphone lines are opened strictly when the user clicks "Start Recording" and are closed and flushed immediately upon clicking "Stop".
2. **Zero Background Capture**: When the recording button is not active, the microphone hardware line is released back to Windows.
3. **Local Conversion**: Audio is processed in-memory as a 16kHz 16-bit mono WAV buffer. Temporary audio files are marked `deleteOnExit()`.

### 2.4 Offline Network Silence Guarantee
When the user selects **Offline Mode**:
1. Zero outbound HTTP/HTTPS connections are initiated to external cloud endpoints.
2. The AI router automatically diverts all queries to the local Ollama daemon (`http://localhost:11434`) or the embedded offline heuristic knowledge base.
3. No telemetry, candidate answers, or resume texts leave the candidate's machine.

### 2.5 Candidate Data Deletion & Privacy Wipe
Candidates retain full data sovereignty:
- The "Wipe All Local Practice Data" feature in the Privacy settings issues a cascading purge across all SQLite tables (`resumes`, `resume_skills`, `practice_sessions`, `practice_answers`, `practice_reports`).
