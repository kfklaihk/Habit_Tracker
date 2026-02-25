# Developer Habit & Productivity Tracker

https://habittracker-production-0b6c.up.railway.app/

<img width="925" height="560" alt="image" src="https://github.com/user-attachments/assets/84cba623-3951-4706-b86e-9b4ce90a45e1" />


A lightweight web app to **log your daily coding sessions** (hours + focus + notes), **tag skills with minutes practiced**, and visualize with  **skills distribution**, and **monthly hours**.

## Features

- **Daily log entry**: log date, hours coded, focus score, notes
- **Skill tagging**: attach skills to a daily log with minutes practiced
- **Contribution Activity**: last 12 months activity log (hours/day)
- **Charts**:
  - skills distribution (last 30 days)
  - weekly hours (last 12 weeks)
- **Goals**: simple “weekly hours” progress table
- **REST API**: CRUD for users/skills/goals + dashboard endpoints
- **Responsive UI**: Bootstrap 5 +  Chart.js

## Technology Stack

### Backend
- **Spring Boot 3.4.x**
- **Java 21**
- **MyBatis 3.0.3**
- **PostgreSQL**
- **Maven**

### Frontend
- **Bootstrap 5**
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
# (3 sample users + ~50 seeded activity days)
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

