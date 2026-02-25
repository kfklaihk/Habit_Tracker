-- Sample data for development only. Enable by running with:
--   SPRING_PROFILES_ACTIVE=dev

INSERT INTO users (id, username, email)
VALUES
    (1, 'john_dev', 'john@example.com'),
    (2, 'jane_coder', 'jane@example.com'),
    (3, 'sam_builder', 'sam@example.com')
ON CONFLICT (id) DO UPDATE
SET username = EXCLUDED.username,
    email = EXCLUDED.email;

INSERT INTO skills (id, name, category, color_hex)
VALUES
    (1, 'Java', 'Language', '#b07219'),
    (2, 'Spring Boot', 'Framework', '#6db33f'),
    (3, 'MyBatis', 'Framework', '#dc382d'),
    (4, 'SQL', 'Database', '#00758f'),
    (5, 'Docker', 'DevOps', '#2496ed'),
    (6, 'Git', 'Tool', '#f05032'),
    (7, 'JavaScript', 'Language', '#f1e05a'),
    (8, 'TypeScript', 'Language', '#3178c6'),
    (9, 'PostgreSQL', 'Database', '#336791'),
    (10, 'Linux', 'Tool', '#333333')
ON CONFLICT (id) DO UPDATE
SET name = EXCLUDED.name,
    category = EXCLUDED.category,
    color_hex = EXCLUDED.color_hex;

-- ~50 total activity days (daily logs), spread across ~12 months:
-- User 1: 20 logs
INSERT INTO daily_logs (user_id, log_date, hours_coded, focus_score, notes)
SELECT
    1,
    (CURRENT_DATE - gs),
    ROUND(((2 + (gs % 9))::numeric) / 2, 1),
    5 + (gs % 6),
    '[seed] ' ||
    CASE (gs % 6)
      WHEN 0 THEN 'Built dashboard analytics + activity heatmap'
      WHEN 1 THEN 'Refactored MyBatis mappers and cleaned up queries'
      WHEN 2 THEN 'Improved UI layout, charts, and responsiveness'
      WHEN 3 THEN 'Debugged deployment configuration and database connectivity'
      WHEN 4 THEN 'Practiced PostgreSQL aggregations and indexing'
      ELSE 'Reviewed code and wrote tests'
    END
FROM generate_series(0, 323, 17) AS gs
ON CONFLICT (user_id, log_date) DO UPDATE
SET hours_coded = EXCLUDED.hours_coded,
    focus_score = EXCLUDED.focus_score,
    notes = EXCLUDED.notes,
    updated_at = CURRENT_TIMESTAMP;

-- User 2: 15 logs
INSERT INTO daily_logs (user_id, log_date, hours_coded, focus_score, notes)
SELECT
    2,
    (CURRENT_DATE - gs),
    ROUND(((3 + (gs % 8))::numeric) / 2, 1),
    4 + (gs % 7),
    '[seed] ' ||
    CASE (gs % 6)
      WHEN 0 THEN 'Worked through Spring Boot API wiring'
      WHEN 1 THEN 'Practiced SQL joins and group-by reporting'
      WHEN 2 THEN 'Implemented skill tagging with minutes practiced'
      WHEN 3 THEN 'Polished charts and labels for readability'
      WHEN 4 THEN 'Debugged edge cases (empty months, no skills logged)'
      ELSE 'Cleaned up code style and naming'
    END
FROM generate_series(5, 285, 20) AS gs
ON CONFLICT (user_id, log_date) DO UPDATE
SET hours_coded = EXCLUDED.hours_coded,
    focus_score = EXCLUDED.focus_score,
    notes = EXCLUDED.notes,
    updated_at = CURRENT_TIMESTAMP;

-- User 3: 15 logs
INSERT INTO daily_logs (user_id, log_date, hours_coded, focus_score, notes)
SELECT
    3,
    (CURRENT_DATE - gs),
    ROUND(((2 + (gs % 10))::numeric) / 2, 1),
    4 + (gs % 7),
    '[seed] ' ||
    CASE (gs % 6)
      WHEN 0 THEN 'Dockerized the app and tested Railway deploy'
      WHEN 1 THEN 'Focused coding sprint on backend services'
      WHEN 2 THEN 'Added more realistic seed data for demos'
      WHEN 3 THEN 'Reviewed goal tracking and progress calculations'
      WHEN 4 THEN 'Practiced TypeScript and frontend tooling'
      ELSE 'Triage + bugfix session'
    END
FROM generate_series(12, 292, 20) AS gs
ON CONFLICT (user_id, log_date) DO UPDATE
SET hours_coded = EXCLUDED.hours_coded,
    focus_score = EXCLUDED.focus_score,
    notes = EXCLUDED.notes,
    updated_at = CURRENT_TIMESTAMP;

-- Add skill minutes for seeded logs (1 skill per log for all, plus an extra skill for ~1/3 of logs).
INSERT INTO log_skills (daily_log_id, skill_id, minutes_practiced)
SELECT
    dl.id,
    ((dl.user_id + EXTRACT(DOY FROM dl.log_date)::int) % 10) + 1,
    GREATEST(15, LEAST(360, (dl.hours_coded * 60)::int))
FROM daily_logs dl
WHERE dl.notes LIKE '[seed]%'
ON CONFLICT (daily_log_id, skill_id) DO UPDATE
SET minutes_practiced = EXCLUDED.minutes_practiced;

INSERT INTO log_skills (daily_log_id, skill_id, minutes_practiced)
SELECT
    dl.id,
    (((dl.user_id + EXTRACT(DOY FROM dl.log_date)::int) % 10) + 1) % 10 + 1,
    GREATEST(10, LEAST(180, ((dl.hours_coded * 60)::int / 2)))
FROM daily_logs dl
WHERE dl.notes LIKE '[seed]%'
  AND (EXTRACT(DAY FROM dl.log_date)::int % 3) = 0
ON CONFLICT (daily_log_id, skill_id) DO UPDATE
SET minutes_practiced = EXCLUDED.minutes_practiced;

-- Goals (MVP)
INSERT INTO goals (user_id, name, description, goal_type, target_value, unit, start_date, end_date, active)
SELECT 1, 'Code 10 hrs/week', 'Target: 10 hours of focused coding each week', 'WEEKLY_HOURS', 10.0, 'hours/week',
       CURRENT_DATE - INTERVAL '12 weeks', NULL, TRUE
WHERE NOT EXISTS (SELECT 1 FROM goals WHERE user_id = 1 AND name = 'Code 10 hrs/week' AND active = TRUE);

INSERT INTO goals (user_id, name, description, goal_type, target_value, unit, start_date, end_date, active)
SELECT 2, 'Code 8 hrs/week', 'Target: 8 hours of coding each week', 'WEEKLY_HOURS', 8.0, 'hours/week',
       CURRENT_DATE - INTERVAL '8 weeks', NULL, TRUE
WHERE NOT EXISTS (SELECT 1 FROM goals WHERE user_id = 2 AND name = 'Code 8 hrs/week' AND active = TRUE);

INSERT INTO goals (user_id, name, description, goal_type, target_value, unit, start_date, end_date, active)
SELECT 3, 'Practice SQL 6 hrs/week', 'Improve query fluency', 'WEEKLY_HOURS', 6.0, 'hours/week',
       CURRENT_DATE - INTERVAL '8 weeks', NULL, TRUE
WHERE NOT EXISTS (SELECT 1 FROM goals WHERE user_id = 3 AND name = 'Practice SQL 6 hrs/week' AND active = TRUE);

-- Ensure sequences are >= max(id) after seeding
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT COALESCE(MAX(id), 1) FROM users));
SELECT setval(pg_get_serial_sequence('skills', 'id'), (SELECT COALESCE(MAX(id), 1) FROM skills));
SELECT setval(pg_get_serial_sequence('daily_logs', 'id'), (SELECT COALESCE(MAX(id), 1) FROM daily_logs));
SELECT setval(pg_get_serial_sequence('log_skills', 'id'), (SELECT COALESCE(MAX(id), 1) FROM log_skills));
SELECT setval(pg_get_serial_sequence('goals', 'id'), (SELECT COALESCE(MAX(id), 1) FROM goals));

