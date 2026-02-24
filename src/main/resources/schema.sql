-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Habits table
CREATE TABLE IF NOT EXISTS habits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    color VARCHAR(7) DEFAULT '#4CAF50',
    target_frequency VARCHAR(20) DEFAULT 'DAILY',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Habit entries table (tracks daily completion)
CREATE TABLE IF NOT EXISTS habit_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    habit_id BIGINT NOT NULL,
    entry_date DATE NOT NULL,
    completed BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (habit_id) REFERENCES habits(id) ON DELETE CASCADE,
    CONSTRAINT unique_habit_date UNIQUE (habit_id, entry_date)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_habit_user ON habits(user_id);
CREATE INDEX IF NOT EXISTS idx_entry_habit ON habit_entries(habit_id);
CREATE INDEX IF NOT EXISTS idx_entry_date ON habit_entries(entry_date);

-- Sample data
INSERT INTO users (username, email) VALUES 
    ('john_dev', 'john@example.com'),
    ('jane_coder', 'jane@example.com');

INSERT INTO habits (user_id, name, description, color, target_frequency) VALUES 
    (1, 'Daily Commit', 'Make at least one meaningful commit', '#2196F3', 'DAILY'),
    (1, 'Code Review', 'Review team members code', '#FF9800', 'DAILY'),
    (1, 'Learn New Tech', 'Study new technology or framework', '#9C27B0', 'WEEKLY'),
    (2, 'Write Tests', 'Write unit or integration tests', '#4CAF50', 'DAILY');

-- Sample habit entries (optional)
INSERT INTO habit_entries (habit_id, entry_date, completed) VALUES 
    (1, CURRENT_DATE(), TRUE),
    (1, DATEADD('DAY', -1, CURRENT_DATE()), TRUE),
    (2, CURRENT_DATE(), TRUE);
