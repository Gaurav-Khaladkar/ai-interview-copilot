-- ============================================================================
-- AI Interview Copilot - PostgreSQL Production Migration Schema (V1)
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 1. Users Table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'ROLE_CANDIDATE',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- 2. User Settings Table
CREATE TABLE IF NOT EXISTS user_settings (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_domain VARCHAR(100) DEFAULT 'Java Full Stack',
    years_experience INT DEFAULT 2,
    preferred_language VARCHAR(50) DEFAULT 'English',
    default_difficulty VARCHAR(50) DEFAULT 'Mid-Level',
    auto_audio_playback BOOLEAN DEFAULT FALSE,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_user_settings_user UNIQUE (user_id)
);

-- 3. Resumes Table
CREATE TABLE IF NOT EXISTS resumes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    raw_text TEXT,
    quality_score INT DEFAULT 0,
    suggestions_json TEXT,
    uploaded_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_resumes_user_id ON resumes(user_id);

-- 4. Resume Skills Table
CREATE TABLE IF NOT EXISTS resume_skills (
    id VARCHAR(36) PRIMARY KEY,
    resume_id VARCHAR(36) NOT NULL REFERENCES resumes(id) ON DELETE CASCADE,
    skill_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    proficiency_level VARCHAR(50) DEFAULT 'Intermediate',
    years_experience INT DEFAULT 1,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_resume_skills_resume ON resume_skills(resume_id);
CREATE INDEX IF NOT EXISTS idx_resume_skills_name ON resume_skills(skill_name);

-- 5. Practice Sessions Table
CREATE TABLE IF NOT EXISTS practice_sessions (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    job_role VARCHAR(150) NOT NULL,
    topic VARCHAR(150) NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    language VARCHAR(50) DEFAULT 'English',
    duration_minutes INT DEFAULT 30,
    status VARCHAR(50) DEFAULT 'IN_PROGRESS',
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_sessions_user ON practice_sessions(user_id);

-- 6. Practice Questions Table
CREATE TABLE IF NOT EXISTS practice_questions (
    id VARCHAR(36) PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL REFERENCES practice_sessions(id) ON DELETE CASCADE,
    question_order INT NOT NULL,
    question_type VARCHAR(50) DEFAULT 'TECHNICAL',
    question_text TEXT NOT NULL,
    expected_keywords TEXT,
    difficulty VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_questions_session ON practice_questions(session_id);

-- 7. Practice Answers Table
CREATE TABLE IF NOT EXISTS practice_answers (
    id VARCHAR(36) PRIMARY KEY,
    question_id VARCHAR(36) NOT NULL REFERENCES practice_questions(id) ON DELETE CASCADE,
    user_answer TEXT,
    input_mode VARCHAR(20) DEFAULT 'TEXT',
    transcription_text TEXT,
    score_accuracy INT DEFAULT 0,
    score_completeness INT DEFAULT 0,
    score_communication INT DEFAULT 0,
    ai_feedback TEXT,
    ideal_answer_sample TEXT,
    submitted_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX IF NOT EXISTS idx_answers_question ON practice_answers(question_id);

-- 8. Practice Reports Table
CREATE TABLE IF NOT EXISTS practice_reports (
    id VARCHAR(36) PRIMARY KEY,
    session_id VARCHAR(36) NOT NULL REFERENCES practice_sessions(id) ON DELETE CASCADE,
    overall_score INT DEFAULT 0,
    technical_score INT DEFAULT 0,
    communication_score INT DEFAULT 0,
    strengths_json TEXT,
    weaknesses_json TEXT,
    recommended_topics_json TEXT,
    generated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_practice_reports_session UNIQUE (session_id)
);

-- 9. Application Preferences Table
CREATE TABLE IF NOT EXISTS application_preferences (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    theme VARCHAR(20) DEFAULT 'DARK',
    window_opacity DOUBLE PRECISION DEFAULT 1.0,
    always_on_top BOOLEAN DEFAULT FALSE,
    window_width INT DEFAULT 1280,
    window_height INT DEFAULT 820,
    window_x INT DEFAULT 100,
    window_y INT DEFAULT 100,
    active_mode VARCHAR(20) DEFAULT 'ONLINE',
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_app_preferences_user UNIQUE (user_id)
);

-- 10. Model Preferences Table
CREATE TABLE IF NOT EXISTS model_preferences (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    online_provider VARCHAR(50) DEFAULT 'OPENAI',
    online_model_name VARCHAR(100) DEFAULT 'gpt-4o-mini',
    online_api_key_vault_ref VARCHAR(255),
    local_provider VARCHAR(50) DEFAULT 'OLLAMA',
    local_model_name VARCHAR(100) DEFAULT 'llama3:8b',
    local_endpoint_url VARCHAR(255) DEFAULT 'http://localhost:11434',
    whisper_model VARCHAR(50) DEFAULT 'whisper-base',
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    is_synced BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_model_preferences_user UNIQUE (user_id)
);
