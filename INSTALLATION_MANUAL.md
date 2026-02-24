# Java and Maven Setup Guide for Windows

## Current Status
✓ **Java 17 is installed**: `C:\Program Files\Java\jdk-25.0.2`
✗ **Maven is NOT installed**: Needs to be downloaded and extracted

---

## Option 1: Manual Setup (Recommended)

### Step 1: Download Maven
1. Go to: https://maven.apache.org/download.cgi
2. Under "Files", download: **apache-maven-3.9.6-bin.zip** (Binary zip archive)
3. Save it to: `C:\Users\Kevin Lai\Downloads\`

### Step 2: Extract Maven
1. Right-click the ZIP file → **Extract All...**
2. Extract to: `C:\Users\Kevin Lai\Downloads\`
3. This creates: `C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\`

### Step 3: Run Your Application
Open PowerShell in the project directory and run:

```powershell
$java = "C:\Program Files\Java\jdk-25.0.2\bin\java.exe"
$mvn = "C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\bin\mvn.cmd"

& $java -version
& $mvn -version

cd "C:\Users\Kevin Lai\Downloads\Bootify_proj"
& $mvn clean spring-boot:run
```

---

## Option 2: Using VS Code Extension

Since you have Java extensions in VS Code:

1. Open the project in VS Code
2. Press `F5` to start debugging
3. VS Code will use its built-in Java runtime

---

## Option 3: Using Gradle Wrapper (If Available)

If your project includes a gradle wrapper:

```powershell
cd "C:\Users\Kevin Lai\Downloads\Bootify_proj"
.\gradlew.bat bootRun
```

---

## Troubleshooting

### Java Not Found
```powershell
"C:\Program Files\Java\jdk-25.0.2\bin\java.exe" -version
```

### Maven Not Found
Verify the path exists:
```powershell
Test-Path "C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\bin\mvn.cmd"
```

### Port 8080 Already in Use
Edit `src/main/resources/application.yml`:
```yaml
server:
  port: 8081
```

### Permission Denied
Run PowerShell as Administrator:
- Right-click PowerShell → "Run as Administrator"

---

## Quick Start Command (Once Files Are Ready)

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
$env:PATH = "$env:JAVA_HOME\bin;C:\Users\Kevin Lai\Downloads\apache-maven-3.9.6\bin;$env:PATH"

cd "C:\Users\Kevin Lai\Downloads\Bootify_proj"
mvn clean spring-boot:run
```

---

## What Happens When Running

1. **First run**: Downloads all Maven dependencies (~500MB)
2. **Compilation**: Compiles all Java source files
3. **Build**: Packages the application
4. **Start**: Launches Spring Boot on port 8080

Expected output:
```
...
[INFO] Application startup complete in X seconds
...
```

Then open: **http://localhost:8080**

---

## After Application Starts

1. **Web App**: http://localhost:8080
2. **H2 Console**: http://localhost:8080/h2-console
3. **Stop Server**: Press `Ctrl+C` in the terminal

---

## Accessing Sample Data

Default load includes:
- 2 users: John Doe, Jane Smith  
- 4 habits: Daily Coding, Code Review, Tech Articles, Exercise
- 30+ sample entries

Login with any user email from the system.

---

## Next Steps

1. Download Maven (Option 1, Step 1-2)
2. Run the Quick Start Command above
3. Open http://localhost:8080 in browser
4. Create new habits and track your progress!

