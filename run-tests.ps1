# Enterprise Cucumber-TestNG Test Execution Scripts
# PowerShell script with proper syntax

Write-Host "===== Enterprise Test Automation Framework =====" -ForegroundColor Green
Write-Host ""

# Function to run Maven with proper PowerShell syntax
function Run-MavenTest {
    param(
        [string]$Profiles,
        [string]$Description
    )
    
    Write-Host "Running $Description..." -ForegroundColor Yellow
    
    # Use proper PowerShell syntax for Maven profiles
    $command = "mvn clean verify `"-P$Profiles`""
    Write-Host "Executing: $command" -ForegroundColor Cyan
    
    Invoke-Expression $command
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Test execution failed!" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "$Description completed successfully!" -ForegroundColor Green
    Write-Host ""
}

# Test execution examples
Write-Host "Available test execution options:" -ForegroundColor Blue
Write-Host "1. Local Headed Chrome"
Write-Host "2. Local Headless Chrome"
Write-Host "3. Development Environment"
Write-Host "4. Staging Environment"
Write-Host ""

$choice = Read-Host "Enter your choice (1-4) or press Enter for Local Headed Chrome"

switch ($choice) {
    "1" { Run-MavenTest "local,headed,chrome" "Local Tests (Headed Chrome)" }
    "2" { Run-MavenTest "local,headless,chrome" "Local Tests (Headless Chrome)" }
    "3" { Run-MavenTest "dev,chrome" "Development Environment Tests" }
    "4" { Run-MavenTest "staging,firefox" "Staging Environment Tests" }
    default { Run-MavenTest "local,headed,chrome" "Local Tests (Headed Chrome)" }
}

Write-Host "All tests completed successfully!" -ForegroundColor Green