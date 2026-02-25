# Developer Habit & Productivity Tracker

A lightweight web app to **log your daily coding sessions** (hours + focus + notes), **tag skills with minutes practiced**, and visualize progress with a **GitHub-style heatmap**, **streak counter**, **skills distribution**, and **weekly hours**.

This repo follows **Option C** from the PDF guide: it keeps a **static Bootstrap/JS frontend** and a **REST API backend**, but implements the PDF’s *daily log + skills analytics* feature set.

## Features

- **Daily log entry**: log date, hours coded, focus score, notes
- **Skill tagging**: attach skills to a daily log with minutes practiced
- **Contribution heatmap**: last 12 months activity intensity (hours/day)
- **Streak tracking**: current consecutive-day coding streak
- **Charts**:
  - skills distribution (last 30 days)
  - weekly hours (last 12 weeks)
- **Goals (MVP)**: simple “weekly hours” progress table
- **REST API**: CRUD for users/skills/goals + dashboard endpoints
- **Responsive UI**: Bootstrap 5 + Cal-Heatmap + Chart.js

## Technology Stack

### Backend
- **Spring Boot 3.4.x**
- **Java 21**
- **MyBatis 3.0.3**
- **PostgreSQL**
- **Maven**

### Frontend
- **Bootstrap 5**
- **Cal-Heatmap 4.x** (+ D3)
- **Chart.js 4.x**

## Prerequisites

- **Java 21+**
- **Maven 3.8+**
- **PostgreSQL** (local or hosted, e.g. Railway)

## Local development

### 1) Start PostgreSQL (Docker quick option)

```bash
docker run --name dev-habit-postgres -p 5432:5432 \
  -e POSTGRES_DB=dev_habit_tracker \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  postgres:16
```

### 2) Run the app

```bash
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/dev_habit_tracker"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres"

# Optional: load sample dev data (users/skills/logs/goals)
export SPRING_PROFILES_ACTIVE="dev"

mvn spring-boot:run
```

Open `http://localhost:8080`.

## Database schema

Schema is initialized from `src/main/resources/schema.sql` (idempotent “CREATE IF NOT EXISTS”).

Core tables:
- `users`
- `skills`
- `daily_logs`
- `log_skills`
- `goals`

## REST endpoints (high level)

- **Users**
  - `GET /api/users`
  - `GET /api/users/{id}`
  - `GET /api/users/username/{username}`
  - `POST /api/users`
  - `PUT /api/users/{id}`
  - `DELETE /api/users/{id}`

- **Skills**
  - `GET /api/skills`
  - `POST /api/skills`
  - `PUT /api/skills/{id}`
  - `DELETE /api/skills/{id}`

- **Daily logs**
  - `GET /api/daily-logs?userId=...&startDate=YYYY-MM-DD&endDate=YYYY-MM-DD`
  - `POST /api/daily-logs` (upsert by `userId + logDate`, includes skill minutes)

- **Dashboard / Analytics**
  - `GET /api/dashboard?userId=...`
  - `GET /api/activity?userId=...&start=YYYY-MM-DD&end=YYYY-MM-DD`

- **Goals**
  - `GET /api/goals?userId=...&activeOnly=true|false`
  - `POST /api/goals`
  - `PUT /api/goals/{id}`
  - `DELETE /api/goals/{id}`

## Railway deployment

See `RAILWAY_DEPLOYMENT.md`.

