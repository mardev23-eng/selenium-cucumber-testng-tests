# Profile Website Test Execution Script
# Comprehensive testing for marvinmarzon.netlify.app

Write-Host "===== Marvin Marzon Profile Website Test Suite =====" -ForegroundColor Green
Write-Host ""

# Function to run profile tests
function Run-ProfileTests {
    param(
        [string]$TestType,
        [string]$Tags,
        [string]$Description
    )
    
    Write-Host "Running $Description..." -ForegroundColor Yellow
    
    $command = "mvn clean verify `"-Plocal,chrome`" `"-Dtest.cucumber.tags=$Tags`" `"-Dtest.environment=profile`""
    Write-Host "Executing: $command" -ForegroundColor Cyan
    
    Invoke-Expression $command
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "$Description failed!" -ForegroundColor Red
        return $false
    }
    
    Write-Host "$Description completed successfully!" -ForegroundColor Green
    Write-Host ""
    return $true
}

# Test execution menu
Write-Host "Profile Website Test Options:" -ForegroundColor Blue
Write-Host "1. Smoke Tests (Quick verification)"
Write-Host "2. Content Tests (Verify all content)"
Write-Host "3. Functionality Tests (Interactive elements)"
Write-Host "4. Responsive Tests (Mobile/Tablet/Desktop)"
Write-Host "5. Advanced Tests (Performance/SEO/Accessibility)"
Write-Host "6. Full Test Suite (All tests)"
Write-Host "7. Custom Test Suite"
Write-Host ""

$choice = Read-Host "Enter your choice (1-7) or press Enter for Smoke Tests"

$success = $true

switch ($choice) {
    "1" { 
        $success = Run-ProfileTests "smoke" "@smoke and @profile" "Profile Smoke Tests"
    }
    "2" { 
        $success = Run-ProfileTests "content" "@content and @profile" "Profile Content Tests"
    }
    "3" { 
        $success = Run-ProfileTests "functionality" "@functionality and @profile" "Profile Functionality Tests"
    }
    "4" { 
        $success = Run-ProfileTests "responsive" "@responsive and @profile" "Profile Responsive Tests"
    }
    "5" { 
        $success = Run-ProfileTests "advanced" "@advanced and @profile" "Profile Advanced Tests"
    }
    "6" { 
        Write-Host "Running Full Profile Test Suite..." -ForegroundColor Yellow
        $success = Run-ProfileTests "full" "@profile" "Full Profile Test Suite"
    }
    "7" {
        $customTags = Read-Host "Enter custom tags (e.g., '@smoke and @content')"
        $success = Run-ProfileTests "custom" $customTags "Custom Profile Tests"
    }
    default { 
        $success = Run-ProfileTests "smoke" "@smoke and @profile" "Profile Smoke Tests"
    }
}

if ($success) {
    Write-Host ""
    Write-Host "===== Test Execution Summary =====" -ForegroundColor Green
    Write-Host "Profile website tests completed successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Reports available at:" -ForegroundColor Cyan
    Write-Host "- HTML Report: target/reports/cucumber-html-report/index.html" -ForegroundColor White
    Write-Host "- JSON Report: target/reports/cucumber-json-report.json" -ForegroundColor White
    Write-Host "- Allure Report: Run 'mvn allure:serve' to view" -ForegroundColor White
    Write-Host ""
    
    $openReport = Read-Host "Open HTML report? (y/n)"
    if ($openReport -eq "y" -or $openReport -eq "Y") {
        if (Test-Path "target/reports/cucumber-html-report/index.html") {
            Start-Process "target/reports/cucumber-html-report/index.html"
        } else {
            Write-Host "HTML report not found. Run tests first." -ForegroundColor Yellow
        }
    }
} else {
    Write-Host ""
    Write-Host "===== Test Execution Failed =====" -ForegroundColor Red
    Write-Host "Please check the logs above for details." -ForegroundColor Yellow
    Write-Host ""
}

Write-Host "Press any key to exit..."
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")