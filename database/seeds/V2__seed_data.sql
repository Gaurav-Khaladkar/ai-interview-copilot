-- ============================================================================
-- AI Interview Copilot - Seed Data (V2)
-- Preloaded Questions, Technical Domains & Default User
-- ============================================================================

-- Default Candidate User: demo@aicopilot.com / Password123!
-- BCrypt hash for "Password123!" is $2a$12$NqR5hY6iW9Q9x7Q1ZzB8Ue6hZ5yF0J1k2l3m4n5o6p7q8r9s0t1u2
INSERT INTO users (id, email, password_hash, full_name, role, is_active, is_synced)
VALUES (
    'usr-1000-0000-0000-000000000001',
    'demo@aicopilot.com',
    '$2a$12$04yv6Y1UHQ7rB74mN6/4heq57ZgO2vYk22OQ8Y.zV.fCsqkGj0Zry',
    'Senior Java Candidate',
    'ROLE_CANDIDATE',
    1,
    1
);

INSERT INTO user_settings (id, user_id, target_domain, years_experience, preferred_language, default_difficulty, auto_audio_playback, is_synced)
VALUES (
    'set-1000-0000-0000-000000000001',
    'usr-1000-0000-0000-000000000001',
    'Java Backend & Distributed Systems',
    4,
    'English',
    'Senior',
    0,
    1
);

INSERT INTO application_preferences (id, user_id, theme, window_opacity, always_on_top, window_width, window_height, window_x, window_y, active_mode, is_synced)
VALUES (
    'pref-1000-0000-0000-000000000001',
    'usr-1000-0000-0000-000000000001',
    'DARK',
    1.0,
    0,
    1280,
    820,
    100,
    100,
    'OFFLINE',
    1
);

INSERT INTO model_preferences (id, user_id, online_provider, online_model_name, local_provider, local_model_name, local_endpoint_url, whisper_model, is_synced)
VALUES (
    'mod-1000-0000-0000-000000000001',
    'usr-1000-0000-0000-000000000001',
    'OPENAI',
    'gpt-4o-mini',
    'OLLAMA',
    'llama3:8b',
    'http://localhost:11434',
    'whisper-base',
    1
);
