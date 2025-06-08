# Safe test execution script with cleanup

Write-Host "===== Enterprise Test Automation Framework (Safe Mode) =====" -ForegroundColor Green
Write-Host ""

# Function to run cleanup
function Invoke-Cleanup {
    Write-Host "Running cleanup..." -ForegroundColor Yellow
    & ".\cleanup.ps1"
}

# Function to run Maven with proper error handling
function Run-MavenTestSafe {
    param(
        [string]$Profiles,
        [string]$Description
    )
    
    Write-Host "Running $Description..." -ForegroundColor Yellow
    
    # Run cleanup first
    Invoke-Cleanup
    
    # Use proper PowerShell syntax for Maven profiles
    $command = "mvn clean verify `"-P$Profiles`""
    Write-Host "Executing: $command" -ForegroundColor Cyan
    
    try {
        Invoke-Expression $command
        
        if ($LASTEXITCODE -ne 0) {
            Write-Host "Test execution failed!" -ForegroundColor Red
            Write-Host "Running cleanup after failure..." -ForegroundColor Yellow
            Invoke-Cleanup
            exit 1
        }
        
        Write-Host "$Description completed successfully!" -ForegroundColor Green
    }
    catch {
        Write-Host "Error during test execution: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "Running cleanup after error..." -ForegroundColor Yellow
        Invoke-Cleanup
        exit 1
    }
    
    Write-Host ""
}

# Test execution options
Write-Host "Available test execution options:" -ForegroundColor Blue
Write-Host "1. Local Headed Chrome"
Write-Host "2. Local Headless Chrome"
Write-Host "3. Development Environment"
Write-Host "4. Staging Environment"
Write-Host "5. Smoke Tests Only"
Write-Host ""

$choice = Read-Host "Enter your choice (1-5) or press Enter for Local Headed Chrome"

switch ($choice) {
    "1" { Run-MavenTestSafe "local,headed,chrome" "Local Tests (Headed Chrome)" }
    "2" { Run-MavenTestSafe "local,headless,chrome" "Local Tests (Headless Chrome)" }
    "3" { Run-MavenTestSafe "dev,chrome" "Development Environment Tests" }
    "4" { Run-MavenTestSafe "staging,firefox" "Staging Environment Tests" }
    "5" { Run-MavenTestSafe "local,chrome,smoke" "Smoke Tests (Local Chrome)" }
    default { Run-MavenTestSafe "local,headed,chrome" "Local Tests (Headed Chrome)" }
}

# Final cleanup
Write-Host "Running final cleanup..." -ForegroundColor Yellow
Invoke-Cleanup

Write-Host "All tests completed successfully!" -ForegroundColor Green