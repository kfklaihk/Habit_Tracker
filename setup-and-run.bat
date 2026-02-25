@echo off
REM Developer Habit Tracker - Setup and Run Script

setlocal enabledelayedexpansion

echo =====================================
echo Developer Habit Tracker Setup
echo =====================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-25.0.2
set MAVEN_HOME=C:\Program Files\Apache\maven
set JAVA_BIN=%JAVA_HOME%\bin

REM Check Java
echo Checking Java installation...
"%JAVA_BIN%\java.exe" -version
if errorlevel 1 (
    echo Java not found!
    exit /b 1
)
echo Java is ready!
echo.

REM Check Maven
echo Checking Maven installation...
if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    "%MAVEN_HOME%\bin\mvn.cmd" -version
    echo Maven is ready!
) else (
    echo Maven is not installed
    echo.
    echo Please download Maven from: https://maven.apache.org/download.cgi
    echo Extract to: C:\Program Files\Apache\maven
    echo Then run this script again
    exit /b 1
)

echo.
echo =====================================
echo Building and Running Application
echo =====================================
echo.

REM Default local database settings (override if needed)
if "%SPRING_DATASOURCE_URL%"=="" set SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/dev_habit_tracker
if "%SPRING_DATASOURCE_USERNAME%"=="" set SPRING_DATASOURCE_USERNAME=postgres
if "%SPRING_DATASOURCE_PASSWORD%"=="" set SPRING_DATASOURCE_PASSWORD=postgres
if "%SPRING_PROFILES_ACTIVE%"=="" set SPRING_PROFILES_ACTIVE=dev

cd /d "C:\Users\Kevin Lai\Downloads\Bootify_proj"

echo Building project...
"%MAVEN_HOME%\bin\mvn.cmd" clean install -DskipTests

if errorlevel 1 (
    echo Build failed!
    exit /b 1
)

echo.
echo Build successful!
echo.
echo =====================================
echo Starting application on port 8080
echo =====================================
echo.
echo Access the application at: http://localhost:8080
echo Database: PostgreSQL (configure via SPRING_DATASOURCE_URL)
echo.
echo Press Ctrl+C to stop the server
echo.

REM Run the application
"%MAVEN_HOME%\bin\mvn.cmd" spring-boot:run

pause
