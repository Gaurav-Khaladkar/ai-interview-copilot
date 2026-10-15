# REST API Specification
## Project: AI Interview Copilot
### Base URL: `http://localhost:8080/api`
### Authentication: Bearer Token (JWT in `Authorization` header)

---

## 1. Authentication Endpoints

### 1.1 Register User
- **POST** `/auth/register`
- **Request Body**:
  ```json
  {
    "email": "candidate@example.com",
    "password": "Password123!",
    "fullName": "Jane Doe",
    "targetDomain": "Java / Spring Boot",
    "yearsExperience": 3
  }
  ```
- **Response** `201 Created`:
  ```json
  {
    "userId": "d98f7e2a-1b4c-4e89-b7e2-123456789abc",
    "email": "candidate@example.com",
    "fullName": "Jane Doe",
    "token": "eyJhbGciOiJIUzI1NiIsIn...",
    "expiresInSeconds": 86400
  }
  ```

### 1.2 Login User
- **POST** `/auth/login`
- **Request Body**:
  ```json
  {
    "email": "candidate@example.com",
    "password": "Password123!"
  }
  ```
- **Response** `200 OK`:
  ```json
  {
    "userId": "d98f7e2a-1b4c-4e89-b7e2-123456789abc",
    "email": "candidate@example.com",
    "fullName": "Jane Doe",
    "token": "eyJhbGciOiJIUzI1NiIsIn...",
    "role": "ROLE_CANDIDATE"
  }
  ```

### 1.3 Logout
- **POST** `/auth/logout`
- **Headers**: `Authorization: Bearer <token>`
- **Response** `200 OK`:
  ```json
  {
    "message": "Session invalidated successfully."
  }
  ```

---

## 2. Resume Management Endpoints

### 2.1 Upload & Parse Resume
- **POST** `/resumes/upload`
- **Content-Type**: `multipart/form-data`
- **Form Parameters**: `file` (PDF or DOCX, max 10MB)
- **Response** `201 Created`:
  ```json
  {
    "resumeId": "550e8400-e29b-41d4-a716-446655440000",
    "fileName": "Jane_Doe_Java_Resume.pdf",
    "fileType": "application/pdf",
    "qualityScore": 88,
    "skills": [
      {"name": "Java", "category": "Languages", "proficiency": "Advanced"},
      {"name": "Spring Boot", "category": "Frameworks", "proficiency": "Advanced"},
      {"name": "PostgreSQL", "category": "Databases", "proficiency": "Intermediate"},
      {"name": "Docker", "category": "DevOps", "proficiency": "Intermediate"}
    ],
    "education": ["B.Tech Computer Science, 2021"],
    "experienceSummary": "3 years as Backend Software Engineer developing microservices.",
    "suggestions": [
      "Quantify metrics on your microservices latency optimization.",
      "Add cloud infrastructure experience details (e.g. AWS/Azure)."
    ]
  }
  ```

### 2.2 List User Resumes
- **GET** `/resumes`
- **Response** `200 OK`: Array of resume summaries.

### 2.3 Get Resume Details
- **GET** `/resumes/{id}`
- **Response** `200 OK`: Full parsed resume object.

### 2.4 Delete Resume
- **DELETE** `/resumes/{id}`
- **Response** `204 No Content`.

---

## 3. AI Assistant & Knowledge Base Endpoints

### 3.1 Generate Interview Answer / Explanation
- **POST** `/assistant/answers`
- **Request Body**:
  ```json
  {
    "question": "Explain how HashMap works internally in Java, including collision handling in Java 8+.",
    "domain": "Java",
    "difficulty": "Senior",
    "format": "DETAILED",
    "targetLanguage": "English",
    "includeCodeExample": true
  }
  ```
- **Response** `200 OK`:
  ```json
  {
    "answer": "HashMap operates on hashing principles using an array of Node<K,V> buckets...",
    "codeSnippet": "Map<String, Integer> map = new HashMap<>();\nmap.put(\"key\", 1);",
    "codeExplanation": [
      {"line": 1, "explanation": "Instantiates HashMap with initial capacity 16 and load factor 0.75."},
      {"line": 2, "explanation": "Calculates hash = (h = key.hashCode()) ^ (h >>> 16), determines bucket index."}
    ],
    "followUpQuestions": [
      "What is the threshold when a linked list bucket converts to a Red-Black Tree in Java 8?",
      "Why must hashCode() and equals() be overridden together?"
    ],
    "modeUsed": "ONLINE_GPT"
  }
  ```

### 3.2 Translate Q&A
- **POST** `/assistant/translate`
- **Request Body**:
  ```json
  {
    "text": "Explain the difference between optimistic and pessimistic locking.",
    "targetLanguage": "Hindi"
  }
  ```
- **Response** `200 OK`:
  ```json
  {
    "translatedText": "ऑप्टिमिस्टिक और पेसिमिस्टिक लॉकिंग के बीच अंतर समझाइए।",
    "sourceLanguage": "English",
    "targetLanguage": "Hindi"
  }
  ```

---

## 4. Speech & Voice Input Endpoints

### 4.1 Transcribe Audio
- **POST** `/speech/transcribe`
- **Content-Type**: `multipart/form-data`
- **Form Parameters**: `audioFile` (16kHz WAV), `language` (e.g., "en", "hi", "mr")
- **Response** `200 OK`:
  ```json
  {
    "transcription": "I have extensive experience designing microservices using Spring Cloud and Kafka.",
    "confidence": 0.96,
    "durationSeconds": 4.2,
    "recognizedLanguage": "en"
  }
  ```

---

## 5. Mock Interview Practice Endpoints

### 5.1 Create Practice Session
- **POST** `/practice/sessions`
- **Request Body**:
  ```json
  {
    "jobRole": "Senior Java Backend Engineer",
    "topic": "Microservices & Distributed Systems",
    "difficulty": "Senior",
    "language": "English",
    "durationMinutes": 30,
    "resumeId": "550e8400-e29b-41d4-a716-446655440000"
  }
  ```
- **Response** `201 Created`:
  ```json
  {
    "sessionId": "b3e94a81-79b8-4e12-9c2d-998877665544",
    "status": "IN_PROGRESS",
    "firstQuestion": {
      "questionId": "q-1",
      "order": 1,
      "text": "How do you implement distributed transactions across microservices, and when would you choose the Saga pattern over 2-Phase Commit?"
    }
  }
  ```

### 5.2 Submit Candidate Answer & Get Next Question
- **POST** `/practice/sessions/{id}/answers`
- **Request Body**:
  ```json
  {
    "questionId": "q-1",
    "userAnswer": "The Saga pattern manages transactions through choreography or orchestration...",
    "inputMode": "VOICE"
  }
  ```
- **Response** `200 OK`:
  ```json
  {
    "feedback": {
      "accuracyScore": 88,
      "completenessScore": 85,
      "communicationScore": 90,
      "critique": "Solid explanation of compensating transactions; consider mentioning outbox pattern.",
      "idealSample": "In distributed microservices, 2PC introduces blocking and single-point-of-failure bottlenecks..."
    },
    "nextQuestion": {
      "questionId": "q-2",
      "order": 2,
      "text": "Explain circuit breaker patterns and how Resilience4j prevents cascading failures."
    },
    "isSessionComplete": false
  }
  ```

### 5.3 Complete Session & Get Final Report
- **GET** `/practice/reports/{sessionId}`
- **Response** `200 OK`:
  ```json
  {
    "reportId": "rep-987",
    "sessionId": "b3e94a81-79b8-4e12-9c2d-998877665544",
    "overallScore": 86,
    "technicalScore": 87,
    "communicationScore": 85,
    "strengths": ["Clear distributed systems conceptual clarity", "Strong architectural terminology"],
    "weaknesses": ["Could elaborate more on observability and tracing (OpenTelemetry/Zipkin)"],
    "recommendedTopics": ["Distributed Tracing", "Kafka exactly-once semantics"]
  }
  ```

---

## 6. Coding Practice Endpoints

### 6.1 Execute & Evaluate Code
- **POST** `/coding/execute`
- **Request Body**:
  ```json
  {
    "language": "JAVA",
    "sourceCode": "class Solution { public int[] twoSum(int[] nums, int target) { ... } }",
    "problemId": "TWO_SUM",
    "timeoutMs": 5000
  }
  ```
- **Response** `200 OK`:
  ```json
  {
    "status": "PASSED",
    "testsPassed": 3,
    "totalTests": 3,
    "executionTimeMs": 42,
    "memoryUsedMb": 18.4,
    "timeComplexity": "O(N)",
    "spaceComplexity": "O(N)",
    "aiReview": "Optimal one-pass HashMap solution implemented with clean null checks."
  }
  ```

---

## 7. Settings & Sync Endpoints

### 7.1 Get / Update Preferences
- **GET** `/settings`
- **PUT** `/settings`
  - Updates opacity, window coordinates, theme, online/offline mode.

### 7.2 Trigger Sync
- **POST** `/sync/trigger`
  - Synchronizes local SQLite entities with central PostgreSQL when connected.
