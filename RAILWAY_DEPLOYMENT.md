# Railway deployment (Docker + PostgreSQL)

This guide deploys the app to Railway using the repo `Dockerfile` and a managed Railway PostgreSQL database.

## 0) What you need

- A Railway account
- This repo pushed to GitHub

## 1) Create a Railway project

1. In Railway: **New Project**
2. Choose **Deploy from GitHub repo**
3. Select this repository

Railway will detect the `Dockerfile` at the repo root and build a container.

## 2) Add PostgreSQL to the project

1. In the same Railway project, click **+ New**
2. Choose **Database → PostgreSQL**

Railway will provision the database and create variables on the PostgreSQL service, including:

- `PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`
- `DATABASE_URL` (internal/private, usable from other Railway services)
- `DATABASE_PUBLIC_URL` (TCP proxy, for connecting from your laptop)

## 3) Wire the app service to Postgres (important)

In your **app service** (not the Postgres service), go to **Variables** and set:

- **`SPRING_DATASOURCE_URL`**
  - `jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}`
- **`SPRING_DATASOURCE_USERNAME`**
  - `${{Postgres.PGUSER}}`
- **`SPRING_DATASOURCE_PASSWORD`**
  - `${{Postgres.PGPASSWORD}}`

Notes:
- The `${{Postgres.*}}` syntax references variables from the PostgreSQL service (named “Postgres” by Railway; if you renamed it, use that name).
- You do **not** need `DATABASE_PUBLIC_URL` for the app-to-database connection; the app runs inside Railway and should use the private connection.

## 4) Deploy

1. Railway will automatically kick off a build/deploy when you add variables or push commits.
2. Open the app service’s **Deployments** tab and wait for a successful deploy.

If the app fails to start, check the logs for:

- missing/incorrect datasource variables
- schema init errors (should be idempotent; see `src/main/resources/schema.sql`)

## 5) Create a public domain

1. In the app service: **Settings → Networking**
2. Click **Generate Domain**
3. Visit the generated URL

## 6) Optional: connect to Railway Postgres from your laptop

Use `DATABASE_PUBLIC_URL` (TCP proxy) for external connections.

Example:

```bash
psql "$DATABASE_PUBLIC_URL"
```

## 7) Environment variable summary

App service (recommended):

- `SPRING_DATASOURCE_URL=jdbc:postgresql://${{Postgres.PGHOST}}:${{Postgres.PGPORT}}/${{Postgres.PGDATABASE}}`
- `SPRING_DATASOURCE_USERNAME=${{Postgres.PGUSER}}`
- `SPRING_DATASOURCE_PASSWORD=${{Postgres.PGPASSWORD}}`
- `PORT=${{PORT}}` (Railway sets this automatically)

