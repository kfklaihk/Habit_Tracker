-- =====================================================================================
-- Developer Daily Log + Skills (Option C: REST + static UI, no Thymeleaf)
-- =====================================================================================
-- This schema intentionally replaces the prior habits-based model.
-- In development we use an in-memory DB, so data resets on restart.

DROP TABLE IF EXISTS log_skills;
DROP TABLE IF EXISTS daily_logs;
DROP TABLE IF EXISTS goals;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS habit_entries;
DROP TABLE IF EXISTS habits;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Skill reference table (e.g., Java, Spring Boot, Docker)
CREATE TABLE IF NOT EXISTS skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100),
    color_hex VARCHAR(7)
);

-- One log per user per day
CREATE TABLE IF NOT EXISTS daily_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    log_date DATE NOT NULL,
    hours_coded DECIMAL(4,1) NOT NULL,
    focus_score INT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_user_log_date UNIQUE (user_id, log_date),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Join table: minutes practiced per skill for a given daily log
CREATE TABLE IF NOT EXISTS log_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    daily_log_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    minutes_practiced INT NOT NULL,
    FOREIGN KEY (daily_log_id) REFERENCES daily_logs(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE RESTRICT
);

-- Simple goal tracking (MVP)
CREATE TABLE IF NOT EXISTS goals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    goal_type VARCHAR(50) NOT NULL,
    target_value DECIMAL(8,2),
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

-- =====================================================================================
-- Sample data
-- =====================================================================================
INSERT INTO users (id, username, email) VALUES
    (1, 'john_dev', 'john@example.com'),
    (2, 'jane_coder', 'jane@example.com');

INSERT INTO skills (id, name, category, color_hex) VALUES
    (1, 'Java', 'Language', '#b07219'),
    (2, 'Spring Boot', 'Framework', '#6db33f'),
    (3, 'MyBatis', 'Framework', '#dc382d'),
    (4, 'SQL', 'Database', '#00758f'),
    (5, 'Docker', 'DevOps', '#2496ed'),
    (6, 'Git', 'Tool', '#f05032'),
    (7, 'JavaScript', 'Language', '#f1e05a');

-- Daily logs for user 1 (last ~2 weeks)
INSERT INTO daily_logs (id, user_id, log_date, hours_coded, focus_score, notes) VALUES
    (1, 1, DATEADD('DAY', -12, CURRENT_DATE()), 1.5, 6, 'Refactored service layer'),
    (2, 1, DATEADD('DAY', -10, CURRENT_DATE()), 2.0, 7, 'Worked on dashboard API'),
    (3, 1, DATEADD('DAY',  -9, CURRENT_DATE()), 3.0, 8, 'Practiced MyBatis queries'),
    (4, 1, DATEADD('DAY',  -7, CURRENT_DATE()), 2.5, 7, 'Bugfix + cleanup'),
    (5, 1, DATEADD('DAY',  -6, CURRENT_DATE()), 1.0, 5, 'Short session'),
    (6, 1, DATEADD('DAY',  -4, CURRENT_DATE()), 4.0, 9, 'Feature implementation'),
    (7, 1, DATEADD('DAY',  -3, CURRENT_DATE()), 2.0, 7, 'Wrote tests'),
    (8, 1, DATEADD('DAY',  -1, CURRENT_DATE()), 3.5, 8, 'UI polish'),
    (9, 1, CURRENT_DATE(),                  2.5, 7, 'Daily log entry');

-- Daily logs for user 2
INSERT INTO daily_logs (id, user_id, log_date, hours_coded, focus_score, notes) VALUES
    (10, 2, DATEADD('DAY', -5, CURRENT_DATE()), 1.0, 6, 'Learning Spring'),
    (11, 2, DATEADD('DAY', -2, CURRENT_DATE()), 2.5, 7, 'SQL practice');

INSERT INTO log_skills (id, daily_log_id, skill_id, minutes_practiced) VALUES
    (1, 1, 1, 60),
    (2, 1, 6, 30),
    (3, 2, 2, 90),
    (4, 2, 4, 30),
    (5, 3, 3, 120),
    (6, 3, 4, 60),
    (7, 4, 1, 90),
    (8, 4, 2, 60),
    (9, 6, 2, 120),
    (10, 6, 5, 60),
    (11, 7, 1, 60),
    (12, 7, 4, 30),
    (13, 8, 7, 120),
    (14, 8, 6, 30),
    (15, 9, 1, 60),
    (16, 9, 2, 60),
    (17, 10, 2, 60),
    (18, 11, 4, 90);

INSERT INTO goals (id, user_id, name, description, goal_type, target_value, unit, start_date, end_date, active) VALUES
    (1, 1, 'Code 10 hrs/week', 'Target: 10 hours of focused coding each week', 'WEEKLY_HOURS', 10.0, 'hours/week',
     DATEADD('WEEK', -4, CURRENT_DATE()), NULL, TRUE),
    (2, 2, 'Practice SQL 5 hrs/week', 'Improve query fluency', 'WEEKLY_HOURS', 5.0, 'hours/week',
     DATEADD('WEEK', -2, CURRENT_DATE()), NULL, TRUE);
