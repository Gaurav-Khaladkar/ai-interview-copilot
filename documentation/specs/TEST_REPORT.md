# Automated Testing Report & Verification Evidence
## Project: AI Interview Copilot
### Date of Execution: October 16, 2026
### Platform: Windows 11 (64-bit), JDK 24 runtime, targeting Java 17 bytecode

---

## 1. Executive Summary

| Test Suite Module | Target Component | Tests Run | Passed | Failed | Errors | Skipped | Status |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| `ai-integration` | `AiProviderTest` | 5 | 5 | 0 | 0 | 0 | **PASSED** |
| `resume-service` | `ResumeParserTest` | 2 | 2 | 0 | 0 | 0 | **PASSED** |
| `speech-service` | `SpeechServiceTest` | 3 | 3 | 0 | 0 | 0 | **PASSED** |
| `backend-api` | `BackendApiIntegrationTest` | 4 | 4 | 0 | 0 | 0 | **PASSED** |
| `desktop-app` | `StageManagerTest` | 2 | 2 | 0 | 0 | 0 | **PASSED** |
| **Total Project** | **All 5 Modules** | **16** | **16** | **0** | **0** | **0** | **100% PASS** |

---

## 2. Test Suite Details & Execution Commands

### 2.1 AI Provider & Offline Isolation Tests (`ai-integration`)
- **Command to Execute**:
  ```powershell
  mvn test -pl ai-integration -Dtest=AiProviderTest
  ```
- **Tests Executed**:
  1. `testMockAiJavaExplanation`: Verifies Java question explanation, code generation, line-by-line explanation, and follow-up generation.
  2. `testAnswerEvaluation`: Evaluates candidate answer and verifies accuracy, completeness, and communication scores.
  3. `testOfflineNetworkIsolation`: Verifies that in `OFFLINE` mode, router stays strictly local and emits no network errors.
  4. `testMultilingualTranslations`: Verifies contextual translations into Hindi (हिंदी) and Marathi (मराठी).
  5. `testQuestionGeneration`: Verifies domain-specific technical question bank retrieval.

### 2.2 Resume Intelligence & Skill Extraction Tests (`resume-service`)
- **Command to Execute**:
  ```powershell
  mvn test -pl resume-service -Dtest=ResumeParserTest
  ```
- **Tests Executed**:
  1. `testResumeExtractionAndScoring`: Parses simulated resume text, matches 150+ categorized skills, verifies contact details, calculates Quality Score (70-100), and validates that generated questions map strictly to verified resume skills without hallucinating credentials.
  2. `testFileValidationRules`: Enforces rejection of non-PDF/DOCX formats and files exceeding 10 MB.

### 2.3 Audio & Speech Recognition Tests (`speech-service`)
- **Command to Execute**:
  ```powershell
  mvn test -pl speech-service -Dtest=SpeechServiceTest
  ```
- **Tests Executed**:
  1. `testAudioFormatConformity`: Verifies audio recording format parameters (16,000 Hz, 16-bit, Mono, PCM signed, little-endian).
  2. `testOfflineWhisperTranscriptionFallback`: Validates offline Whisper simulation and phonetic transcription when no remote API key is supplied.
  3. `testMultilingualTranscription`: Confirms offline speech transcription in Hindi and Marathi.

### 2.4 Security & Backend Integration Tests (`backend-api`)
- **Command to Execute**:
  ```powershell
  mvn test -pl backend-api -Dtest=BackendApiIntegrationTest
  ```
- **Tests Executed**:
  1. `testPasswordHashing`: Validates BCrypt computational work factor 12 and salt verification.
  2. `testJwtTokenCycle`: Validates HMAC-SHA256 token generation, signature validation, and claims extraction.
  3. `testSandboxSecurityEnforcement`: Verifies that attempts to invoke `System.exit`, `Runtime.getRuntime`, or `ProcessBuilder` are blocked with `SECURITY_VIOLATION`.
  4. `testSandboxValidExecution`: Verifies isolated execution of valid algorithms (Two Sum) with test case verification and Big-O time complexity calculation.

### 2.5 Desktop Window Customization Tests (`desktop-app`)
- **Command to Execute**:
  ```powershell
  mvn test -pl desktop-app -Dtest=StageManagerTest
  ```
- **Tests Executed**:
  1. `testLayoutModes`: Validates dimensions for Compact (900x650), Standard (1280x820), and Expanded (1550x950).
  2. `testOpacityClamping`: Validates window transparency clamping logic (strictly between 0.30 and 1.00).

---

## 3. Maven Build & Test Log Excerpt

```
[INFO] Reactor Summary for AI Interview Copilot - Parent 1.0.0:
[INFO] 
[INFO] AI Interview Copilot - Parent ...................... SUCCESS [  0.007 s]
[INFO] AI Interview Copilot - AI Integration Module ....... SUCCESS [  6.291 s]
[INFO] AI Interview Copilot - Resume Service .............. SUCCESS [  2.361 s]
[INFO] AI Interview Copilot - Speech Service .............. SUCCESS [  3.091 s]
[INFO] AI Interview Copilot - Backend API ................. SUCCESS [ 10.681 s]
[INFO] AI Interview Copilot - JavaFX Desktop Application .. SUCCESS [  5.027 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
