-- =====================================================================================
-- Developer Daily Log + Skills (PostgreSQL)
-- =====================================================================================
-- Non-destructive, idempotent schema initialization for PostgreSQL.
-- Tables are created if missing; existing data is preserved.

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Skill reference table (e.g., Java, Spring Boot, Docker)
CREATE TABLE IF NOT EXISTS skills (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100),
    color_hex VARCHAR(7)
);

-- One log per user per day
CREATE TABLE IF NOT EXISTS daily_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    hours_coded NUMERIC(4,1) NOT NULL,
    focus_score INT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_log_date UNIQUE (user_id, log_date),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Join table: minutes practiced per skill for a given daily log
CREATE TABLE IF NOT EXISTS log_skills (
    id BIGSERIAL PRIMARY KEY,
    daily_log_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    minutes_practiced INT NOT NULL,
    CONSTRAINT unique_log_skill UNIQUE (daily_log_id, skill_id),
    FOREIGN KEY (daily_log_id) REFERENCES daily_logs(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE RESTRICT
);

-- Simple goal tracking (MVP)
CREATE TABLE IF NOT EXISTS goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    goal_type VARCHAR(50) NOT NULL,
    target_value NUMERIC(8,2),
    unit VARCHAR(50),
    start_date DATE,
    end_date DATE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_daily_logs_user_date ON daily_logs(user_id, log_date);
CREATE INDEX IF NOT EXISTS idx_log_skills_daily_log ON log_skills(daily_log_id);
CREATE INDEX IF NOT EXISTS idx_log_skills_skill ON log_skills(skill_id);
CREATE INDEX IF NOT EXISTS idx_goals_user_active ON goals(user_id, active);
