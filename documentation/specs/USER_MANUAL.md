# AI Interview Copilot — Desktop User Manual
## Version 1.0.0 for Windows 10 & 11

---

## 1. Introduction & Welcome

AI Interview Copilot is designed to give you a private, realistic, and stress-free mock interview practice environment directly on your Windows desktop. Whether you are preparing for a FAANG behavioral round or a senior distributed systems architecture interview, this manual guides you through every screen and capability.

---

## 2. Navigating the Interface

### 2.1 Top Window Bar & Custom Controls
- **Opacity Slider (30% to 100%)**: Located at the top right of the window titlebar. Dragging the slider dynamically sets window transparency. This allows you to view reference material, slides, or IDEs beneath the Copilot while practicing.
- **Pin Top Button**: Pinned mode keeps the window permanently on top of all other desktop applications.
- **Operating Mode Badge**:
  - `OFFLINE MODE` (Default): All processing stays on your machine. No network requests are made.
  - `ONLINE MODE`: Enables cloud LLMs (OpenAI, Gemini) and remote Whisper STT if API keys are configured.
- **Theme Toggle**: Switch between Dark Theme (optimized for low eye strain) and Light Theme.
- **Window Controls**: Standard minimize (`—`), maximize/restore (`□`), and close (`✕`). You can also drag the titlebar across multi-monitor setups.

---

## 3. Screen-by-Screen User Guide

### Screen 1: Dashboard
- Displays real-time metrics: Readiness Score (0-100%), Total Sessions Completed, Extracted Technical Competencies, and Verbal Communication average.
- Quick action buttons to launch mock interviews, resume parsing, or coding sandboxes.
- Displays recent session history cards with scores and completion timestamps.

### Screen 2: Mock Interview Simulator
1. **Setup Parameters**: Select your target Job Role (e.g., *Senior Java Backend Engineer*), Topic (*Microservices & Concurrency*), Level (*Senior*), and Language (*English, Hindi, Marathi*).
2. **Start Session**: Generates 5 dynamic questions tailored to your profile.
3. **Timer**: Tracks elapsed time in real-time.
4. **Voice Recording**: Click "Start Voice Recording" to speak your answer aloud. A live volume meter visualizes audio input. Click "Stop & Transcribe" to automatically convert speech to text using Whisper.
5. **Answer Review & Evaluation**: Click "Evaluate Answer & Next Question". The AI evaluates your answer across 3 dimensions:
   - *Technical Accuracy* (0–100%)
   - *Completeness* (0–100%)
   - *Communication Clarity* (0–100%)
   - *Critique & Suggestions*
   - *Ideal Architectural Sample Answer*
6. Conclude the session to generate your comprehensive performance report.

### Screen 3: Resume Intelligence & Skill Parser
1. Click "Choose Resume File" and select any `.pdf` or `.docx` resume (up to 10 MB).
2. The engine instantly extracts candidate name, email, phone, and scans for 150+ technology keywords.
3. **Quality Score**: View your resume score (0–100) and actionable bullet-point suggestions.
4. **Verified Skills**: View skill chips categorized by Languages, Frameworks, Databases, and Cloud/DevOps.
5. **Tailored Interview Questions**: Review questions generated specifically from your verified resume skills.

### Screen 4: AI Technical Assistant & Concept Explainer
1. Select a technical domain (Java, Spring Boot, Python, SQL, Cloud, Android, Testing).
2. Choose your difficulty level and answer format:
   - *Short & Crisp* (Quick 1-2 minute conversational summary)
   - *Detailed Architectural* (Deep dive with internal mechanics)
3. Enter your question and click "Generate Technical Answer".
4. Review the answer, core concepts summary, code snippets, line-by-line explanations, and anticipated follow-up questions.

### Screen 5: Sandboxed Coding Practice
1. Select a problem from the catalog (e.g., *Two Sum*, *Reverse Singly Linked List*, *Valid Parentheses*, *Second Highest Salary*).
2. Write or edit your algorithmic solution in the code editor.
3. Click "Run Code in Isolated Sandbox".
4. Inspect the test cases passed counter, execution time (in milliseconds), memory footprint (in MB), Big-O time and space complexity, and AI refactoring advice.
5. The sandbox automatically intercepts and blocks any disallowed system operations (e.g., process execution or disk commands).

### Screen 6: Multilingual Support
1. Select your target language: English, Hindi (हिंदी), or Marathi (मराठी).
2. Enter technical questions or explanations to test real-time technical translation.
3. Practice answering technical questions in your native language.

### Screen 7: Voice Input Controls
1. Verify that your microphone hardware is detected by inspecting the status indicator.
2. Test microphone recording and observe the live audio volume meter.
3. Click "Read Text Aloud" to hear questions or answers read aloud using Windows speech synthesis.

### Screen 8: Performance Reports
1. View overall readiness ratings and radar breakdowns for accuracy, completeness, and communication.
2. Review verified strengths and areas for growth.
3. Review recommended study topics for your next interview round.
4. Export the summary as a PDF report.

### Screen 9: Offline AI Model Manager
- Displays installed local Ollama models (`llama3:8b`, `phi3:mini`) and Whisper models.
- Inspects your PC's available CPU cores and RAM allocation.
- Provides step-by-step commands to download offline models.

### Screen 10: Privacy, Security & Ethical Policy
- Review the fair use declaration: the tool operates transparently as a desktop training assistant.
- Manage microphone permissions.
- Permanent data purge button to wipe all local resumes and interview history.
