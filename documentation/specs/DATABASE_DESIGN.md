# Database Design & Entity Relationship Specification
## Project: AI Interview Copilot

---

## 1. Entity-Relationship Overview

The database supports both PostgreSQL (online/cloud) and SQLite (offline/local). All table definitions and column constraints are designed for dual compatibility.

```mermaid
erDiagram
    USERS ||--o{ USER_SETTINGS : has
    USERS ||--o{ RESUMES : uploads
    USERS ||--o{ PRACTICE_SESSIONS : takes
    RESUMES ||--o{ RESUME_SKILLS : contains
    PRACTICE_SESSIONS ||--o{ PRACTICE_QUESTIONS : includes
    PRACTICE_QUESTIONS ||--o{ PRACTICE_ANSWERS : receives
    PRACTICE_SESSIONS ||--|| PRACTICE_REPORTS : generates
    USERS ||--o{ APPLICATION_PREFERENCES : configures
    USERS ||--o{ MODEL_PREFERENCES : manages

    USERS {
        uuid id PK
        varchar email UK
        varchar password_hash
        varchar full_name
        varchar role
        boolean is_active
        timestamp created_at
        timestamp updated_at
        boolean is_synced
    }

    USER_SETTINGS {
        uuid id PK
        uuid user_id FK
        varchar target_domain
        int years_experience
        varchar preferred_language
        varchar default_difficulty
        boolean auto_audio_playback
        timestamp updated_at
        boolean is_synced
    }

    RESUMES {
        uuid id PK
        uuid user_id FK
        varchar file_name
        varchar file_type
        bigint file_size
        text raw_text
        int quality_score
        text suggestions_json
        timestamp uploaded_at
        boolean is_synced
    }

    RESUME_SKILLS {
        uuid id PK
        uuid resume_id FK
        varchar skill_name
        varchar category
        varchar proficiency_level
        int years_experience
        boolean is_synced
    }

    PRACTICE_SESSIONS {
        uuid id PK
        uuid user_id FK
        varchar job_role
        varchar topic
        varchar difficulty
        varchar language
        int duration_minutes
        varchar status
        timestamp started_at
        timestamp completed_at
        boolean is_synced
    }

    PRACTICE_QUESTIONS {
        uuid id PK
        uuid session_id FK
        int question_order
        varchar question_type
        text question_text
        text expected_keywords
        varchar difficulty
        timestamp created_at
        boolean is_synced
    }

    PRACTICE_ANSWERS {
        uuid id PK
        uuid question_id FK
        text user_answer
        varchar input_mode
        text transcription_text
        int score_accuracy
        int score_completeness
        int score_communication
        text ai_feedback
        text ideal_answer_sample
        timestamp submitted_at
        boolean is_synced
    }

    PRACTICE_REPORTS {
        uuid id PK
        uuid session_id FK
        int overall_score
        int technical_score
        int communication_score
        text strengths_json
        text weaknesses_json
        text recommended_topics_json
        timestamp generated_at
        boolean is_synced
    }

    APPLICATION_PREFERENCES {
        uuid id PK
        uuid user_id FK
        varchar theme
        double window_opacity
        boolean always_on_top
        int window_width
        int window_height
        int window_x
        int window_y
        varchar active_mode
        timestamp updated_at
        boolean is_synced
    }

    MODEL_PREFERENCES {
        uuid id PK
        uuid user_id FK
        varchar online_provider
        varchar online_model_name
        varchar online_api_key_vault_ref
        varchar local_provider
        varchar local_model_name
        varchar local_endpoint_url
        varchar whisper_model
        timestamp updated_at
        boolean is_synced
    }
```

---

## 2. Table Specifications & Indexes

1. **`users`**:
   - `id`: VARCHAR(36) / UUID PRIMARY KEY.
   - `email`: VARCHAR(255) NOT NULL UNIQUE (B-Tree Index).
   - `password_hash`: VARCHAR(255) NOT NULL (BCrypt hash).
   - `full_name`: VARCHAR(150) NOT NULL.
   - `role`: VARCHAR(50) DEFAULT 'ROLE_CANDIDATE'.
   - `is_active`: BOOLEAN DEFAULT TRUE.
   - `created_at`, `updated_at`: TIMESTAMP.
   - `is_synced`: BOOLEAN DEFAULT FALSE.

2. **`user_settings`**:
   - `user_id`: Foreign Key referencing `users(id)` ON DELETE CASCADE.
   - Target domain indexing for performance analysis.

3. **`resumes` & `resume_skills`**:
   - Foreign key cascading on resume deletion.
   - Index on `(resume_id, skill_name)`.

4. **`practice_sessions`, `practice_questions`, `practice_answers`, `practice_reports`**:
   - Composite index on `(user_id, started_at)` for fast history lookups.
   - `is_synced` column on all persistent records to facilitate offline-to-cloud bidirectional synchronization.
