-- ============================================================================
-- AI Interview Copilot - SQLite Offline Migration Schema (V1)
-- ============================================================================

PRAGMA foreign_keys = ON;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    full_name TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'ROLE_CANDIDATE',
    is_active INTEGER NOT NULL DEFAULT 1,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 2. User Settings Table
CREATE TABLE IF NOT EXISTS user_settings (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL UNIQUE,
    target_domain TEXT DEFAULT 'Java Full Stack',
    years_experience INTEGER DEFAULT 2,
    preferred_language TEXT DEFAULT 'English',
    default_difficulty TEXT DEFAULT 'Mid-Level',
    auto_audio_playback INTEGER DEFAULT 0,
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Resumes Table
CREATE TABLE IF NOT EXISTS resumes (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    file_name TEXT NOT NULL,
    file_type TEXT NOT NULL,
    file_size INTEGER NOT NULL,
    raw_text TEXT,
    quality_score INTEGER DEFAULT 0,
    suggestions_json TEXT,
    uploaded_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_resumes_user_id ON resumes(user_id);

-- 4. Resume Skills Table
CREATE TABLE IF NOT EXISTS resume_skills (
    id TEXT PRIMARY KEY,
    resume_id TEXT NOT NULL,
    skill_name TEXT NOT NULL,
    category TEXT NOT NULL,
    proficiency_level TEXT DEFAULT 'Intermediate',
    years_experience INTEGER DEFAULT 1,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_resume_skills_resume ON resume_skills(resume_id);
CREATE INDEX IF NOT EXISTS idx_resume_skills_name ON resume_skills(skill_name);

-- 5. Practice Sessions Table
CREATE TABLE IF NOT EXISTS practice_sessions (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    job_role TEXT NOT NULL,
    topic TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    language TEXT DEFAULT 'English',
    duration_minutes INTEGER DEFAULT 30,
    status TEXT DEFAULT 'IN_PROGRESS',
    started_at TEXT DEFAULT CURRENT_TIMESTAMP,
    completed_at TEXT,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sessions_user ON practice_sessions(user_id);

-- 6. Practice Questions Table
CREATE TABLE IF NOT EXISTS practice_questions (
    id TEXT PRIMARY KEY,
    session_id TEXT NOT NULL,
    question_order INTEGER NOT NULL,
    question_type TEXT DEFAULT 'TECHNICAL',
    question_text TEXT NOT NULL,
    expected_keywords TEXT,
    difficulty TEXT,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(session_id) REFERENCES practice_sessions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_questions_session ON practice_questions(session_id);

-- 7. Practice Answers Table
CREATE TABLE IF NOT EXISTS practice_answers (
    id TEXT PRIMARY KEY,
    question_id TEXT NOT NULL,
    user_answer TEXT,
    input_mode TEXT DEFAULT 'TEXT',
    transcription_text TEXT,
    score_accuracy INTEGER DEFAULT 0,
    score_completeness INTEGER DEFAULT 0,
    score_communication INTEGER DEFAULT 0,
    ai_feedback TEXT,
    ideal_answer_sample TEXT,
    submitted_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(question_id) REFERENCES practice_questions(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_answers_question ON practice_answers(question_id);

-- 8. Practice Reports Table
CREATE TABLE IF NOT EXISTS practice_reports (
    id TEXT PRIMARY KEY,
    session_id TEXT NOT NULL UNIQUE,
    overall_score INTEGER DEFAULT 0,
    technical_score INTEGER DEFAULT 0,
    communication_score INTEGER DEFAULT 0,
    strengths_json TEXT,
    weaknesses_json TEXT,
    recommended_topics_json TEXT,
    generated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(session_id) REFERENCES practice_sessions(id) ON DELETE CASCADE
);

-- 9. Application Preferences Table
CREATE TABLE IF NOT EXISTS application_preferences (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL UNIQUE,
    theme TEXT DEFAULT 'DARK',
    window_opacity REAL DEFAULT 1.0,
    always_on_top INTEGER DEFAULT 0,
    window_width INTEGER DEFAULT 1280,
    window_height INTEGER DEFAULT 820,
    window_x INTEGER DEFAULT 100,
    window_y INTEGER DEFAULT 100,
    active_mode TEXT DEFAULT 'OFFLINE',
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 10. Model Preferences Table
CREATE TABLE IF NOT EXISTS model_preferences (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL UNIQUE,
    online_provider TEXT DEFAULT 'OPENAI',
    online_model_name TEXT DEFAULT 'gpt-4o-mini',
    online_api_key_vault_ref TEXT,
    local_provider TEXT DEFAULT 'OLLAMA',
    local_model_name TEXT DEFAULT 'llama3:8b',
    local_endpoint_url TEXT DEFAULT 'http://localhost:11434',
    whisper_model TEXT DEFAULT 'whisper-base',
    updated_at TEXT DEFAULT CURRENT_TIMESTAMP,
    is_synced INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);
