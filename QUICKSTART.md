# Quick Start Guide

## ✓ Current Status

- ✓ **Java 25.0.2 is installed**: `C:\Program Files\Java\jdk-25.0.2`
- ✓ **Node PATH configured**

## Step 1: Download Maven

Maven needs to be manually downloaded since we don't have admin rights to Program Files.

1. **Download Maven 3.9.6**:
   - Go to: https://maven.apache.org/download.cgi
   - Click: **apache-maven-3.9.6-bin.zip** (Binary zip archive)
   - Save to: `C:\Users\Kevin Lai\Downloads\`

2. **Extract the ZIP**:
   - Right-click the file → **Extract All**
   - Extract to: `C:\Users\Kevin Lai\Downloads\`
   - Creates: `C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\`

## Step 2: Run the Application

Open **PowerShell** and copy-paste this command:

```powershell
$java = "C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
$mvn = "C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\bin\mvn.cmd"
$project = "C:\Users\Kevin Lai\Downloads\Bootify_proj"

# Verify installations
Write-Host "Testing Java..."
& $java -version

Write-Host "`nTesting Maven..."
& $mvn -version

# Build and run
Write-Host "`nBuilding and running application..."
cd $project
& $mvn clean spring-boot:run
```

## Step 3: Access the Application

- **Web App**: http://localhost:8080
- **Database**: PostgreSQL (configure via `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`)

## Using the Application

1. **Select a user** from the dropdown (2 sample users included)
2. **View dashboard** - Heatmap, streak, skills pie chart, weekly hours bar chart, goals
3. **Log today** - Click "+ Log Today" and record hours/focus/skill minutes
4. **Manage skills** - Click "Manage Skills" and add technologies you practice

## Stop the Application

Press **Ctrl+C** in PowerShell

## Local PostgreSQL (quick option)

If you have Docker installed, you can start a local PostgreSQL like this:

```powershell
docker run --name dev-habit-postgres -p 5432:5432 `
  -e POSTGRES_DB=dev_habit_tracker `
  -e POSTGRES_USER=postgres `
  -e POSTGRES_PASSWORD=postgres `
  postgres:16
```

Then run the app with sample seed data:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/dev_habit_tracker"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="postgres"
$env:SPRING_PROFILES_ACTIVE="dev"

mvn spring-boot:run
```

## Troubleshooting

**Verify paths exist:**
```powershell
Test-Path "C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
Test-Path "C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\bin\mvn.cmd"
```

**First run note:**
- Maven will download ~500MB of dependencies on first run
- This may take 5-10 minutes
- Subsequent runs will be much faster

## Next Steps

- Check `INSTALLATION_MANUAL.md` for detailed setup
- See `README.md` for comprehensive documentation
- Review API endpoints for integration
- Customize habits and tracking preferences

---

**Need help?** See `INSTALLATION_MANUAL.md` for alternative options and troubleshooting
