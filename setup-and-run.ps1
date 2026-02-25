# Developer Habit Tracker - Setup & Run Script

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Developer Habit Tracker Setup" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Set Java home
$JAVA_HOME = "C:\Program Files\Java\jdk-25.0.2"
$MAVEN_HOME = "C:\Program Files\Apache\maven"
$JAVA_BIN = "$JAVA_HOME\bin"

# Add to current session PATH
$env:PATH = "$JAVA_BIN;$MAVEN_HOME\bin;$env:PATH"
$env:JAVA_HOME = $JAVA_HOME

Write-Host "✓ Java path set to: $JAVA_HOME" -ForegroundColor Green

# Check if Java is available
Write-Host ""
Write-Host "Checking Java installation..." -ForegroundColor Yellow
& "$JAVA_BIN\java.exe" -version
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Java is ready!" -ForegroundColor Green
} else {
    Write-Host "✗ Java not found" -ForegroundColor Red
    exit 1
}

Write-Host ""

# Check if Maven is installed
Write-Host "Checking Maven installation..." -ForegroundColor Yellow
if (Test-Path "$MAVEN_HOME\bin\mvn.cmd") {
    & "$MAVEN_HOME\bin\mvn.cmd" -version
    Write-Host "✓ Maven is ready!" -ForegroundColor Green
} else {
    Write-Host ""
    Write-Host "⚠️  Maven is not installed" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Installing Maven 3.9.6..." -ForegroundColor Cyan
    
    # Create Program Files\Apache if it doesn't exist
    if (!(Test-Path "C:\Program Files\Apache")) {
        New-Item -ItemType Directory -Path "C:\Program Files\Apache" -Force | Out-Null
    }
    
    # Download Maven
    $mavenUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
    $mavenZip = "$MAVEN_HOME.zip"
    
    Write-Host "Downloading Maven from: $mavenUrl" -ForegroundColor Cyan
    
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip -ErrorAction Stop
        Write-Host "✓ Maven downloaded" -ForegroundColor Green
        
        # Extract Maven
        Write-Host "Extracting Maven..." -ForegroundColor Cyan
        Expand-Archive -Path $mavenZip -DestinationPath "C:\Program Files\Apache" -Force
        
        # Rename folder
        $extractedFolder = Get-ChildItem "C:\Program Files\Apache" -Directory -Filter "apache-maven*" | Select-Object -First 1
        if ($extractedFolder) {
            Rename-Item -Path $extractedFolder.FullName -NewName "maven" -Force
            Write-Host "✓ Maven extracted and configured" -ForegroundColor Green
        }
        
        # Cleanup
        Remove-Item $mavenZip -Force
        
        # Update PATH for this session
        $env:PATH = "$MAVEN_HOME\bin;$env:PATH"
        
    } catch {
        Write-Host "✗ Failed to download Maven: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "Manual installation:" -ForegroundColor Yellow
        Write-Host "1. Download from: https://maven.apache.org/download.cgi" -ForegroundColor Gray
        Write-Host "2. Extract to: C:\Program Files\Apache\maven" -ForegroundColor Gray
        Write-Host "3. Run this script again" -ForegroundColor Gray
        exit 1
    }
}

Write-Host ""
Write-Host "✓ Verify Maven..." -ForegroundColor Yellow
& "$MAVEN_HOME\bin\mvn.cmd" -version

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Building and Running Application" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Default local database settings (override if needed)
$env:SPRING_DATASOURCE_URL = $env:SPRING_DATASOURCE_URL ?? "jdbc:postgresql://localhost:5432/dev_habit_tracker"
$env:SPRING_DATASOURCE_USERNAME = $env:SPRING_DATASOURCE_USERNAME ?? "postgres"
$env:SPRING_DATASOURCE_PASSWORD = $env:SPRING_DATASOURCE_PASSWORD ?? "postgres"
$env:SPRING_PROFILES_ACTIVE = $env:SPRING_PROFILES_ACTIVE ?? "dev"

# Set the working directory
cd "C:\Users\Kevin Lai\Downloads\Bootify_proj"

# Build and run
Write-Host "Building project..." -ForegroundColor Cyan
& "$MAVEN_HOME\bin\mvn.cmd" clean install -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✓ Build successful!" -ForegroundColor Green
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host "Starting application on port 8080" -ForegroundColor Cyan
    Write-Host "=====================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Access the application at: http://localhost:8080" -ForegroundColor Cyan
    Write-Host "Database: PostgreSQL (configure via SPRING_DATASOURCE_URL)" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Yellow
    Write-Host ""
    
    # Run the application
    & "$MAVEN_HOME\bin\mvn.cmd" spring-boot:run
} else {
    Write-Host ""
    Write-Host "✗ Build failed! Check errors above" -ForegroundColor Red
    exit 1
}
