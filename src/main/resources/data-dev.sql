-- Sample data for development only. Enable by running with:
--   SPRING_PROFILES_ACTIVE=dev

INSERT INTO users (id, username, email)
VALUES
    (1, 'john_dev', 'john@example.com'),
    (2, 'jane_coder', 'jane@example.com')
ON CONFLICT (id) DO NOTHING;

INSERT INTO skills (id, name, category, color_hex)
VALUES
    (1, 'Java', 'Language', '#b07219'),
    (2, 'Spring Boot', 'Framework', '#6db33f'),
    (3, 'MyBatis', 'Framework', '#dc382d'),
    (4, 'SQL', 'Database', '#00758f'),
    (5, 'Docker', 'DevOps', '#2496ed'),
    (6, 'Git', 'Tool', '#f05032'),
    (7, 'JavaScript', 'Language', '#f1e05a')
ON CONFLICT (id) DO NOTHING;

INSERT INTO daily_logs (id, user_id, log_date, hours_coded, focus_score, notes)
VALUES
    (1, 1, CURRENT_DATE - INTERVAL '12 days', 1.5, 6, 'Refactored service layer'),
    (2, 1, CURRENT_DATE - INTERVAL '10 days', 2.0, 7, 'Worked on dashboard API'),
    (3, 1, CURRENT_DATE - INTERVAL  '9 days', 3.0, 8, 'Practiced MyBatis queries'),
    (4, 1, CURRENT_DATE - INTERVAL  '7 days', 2.5, 7, 'Bugfix + cleanup'),
    (5, 1, CURRENT_DATE - INTERVAL  '6 days', 1.0, 5, 'Short session'),
    (6, 1, CURRENT_DATE - INTERVAL  '4 days', 4.0, 9, 'Feature implementation'),
    (7, 1, CURRENT_DATE - INTERVAL  '3 days', 2.0, 7, 'Wrote tests'),
    (8, 1, CURRENT_DATE - INTERVAL  '1 day', 3.5, 8, 'UI polish'),
    (9, 1, CURRENT_DATE,                 2.5, 7, 'Daily log entry'),
    (10, 2, CURRENT_DATE - INTERVAL '5 days', 1.0, 6, 'Learning Spring'),
    (11, 2, CURRENT_DATE - INTERVAL '2 days', 2.5, 7, 'SQL practice')
ON CONFLICT (id) DO NOTHING;

INSERT INTO log_skills (id, daily_log_id, skill_id, minutes_practiced)
VALUES
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
    (18, 11, 4, 90)
ON CONFLICT (id) DO NOTHING;

INSERT INTO goals (id, user_id, name, description, goal_type, target_value, unit, start_date, end_date, active)
VALUES
    (1, 1, 'Code 10 hrs/week', 'Target: 10 hours of focused coding each week', 'WEEKLY_HOURS', 10.0, 'hours/week',
     CURRENT_DATE - INTERVAL '4 weeks', NULL, TRUE),
    (2, 2, 'Practice SQL 5 hrs/week', 'Improve query fluency', 'WEEKLY_HOURS', 5.0, 'hours/week',
     CURRENT_DATE - INTERVAL '2 weeks', NULL, TRUE)
ON CONFLICT (id) DO NOTHING;

-- Ensure sequences are >= max(id) after seeding
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT COALESCE(MAX(id), 1) FROM users));
SELECT setval(pg_get_serial_sequence('skills', 'id'), (SELECT COALESCE(MAX(id), 1) FROM skills));
SELECT setval(pg_get_serial_sequence('daily_logs', 'id'), (SELECT COALESCE(MAX(id), 1) FROM daily_logs));
SELECT setval(pg_get_serial_sequence('log_skills', 'id'), (SELECT COALESCE(MAX(id), 1) FROM log_skills));
SELECT setval(pg_get_serial_sequence('goals', 'id'), (SELECT COALESCE(MAX(id), 1) FROM goals));

