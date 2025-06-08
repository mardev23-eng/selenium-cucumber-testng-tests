@echo off
REM Profile Website Test Execution Script for Windows

echo ===== Marvin Marzon Profile Website Test Suite =====
echo.

echo Profile Website Test Options:
echo 1. Smoke Tests (Quick verification)
echo 2. Content Tests (Verify all content)  
echo 3. Functionality Tests (Interactive elements)
echo 4. Responsive Tests (Mobile/Tablet/Desktop)
echo 5. Advanced Tests (Performance/SEO/Accessibility)
echo 6. Full Test Suite (All tests)
echo.

set /p choice="Enter your choice (1-6) or press Enter for Smoke Tests: "

if "%choice%"=="" set choice=1

if "%choice%"=="1" (
    echo Running Profile Smoke Tests...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@smoke and @profile" "-Dtest.environment=profile"
) else if "%choice%"=="2" (
    echo Running Profile Content Tests...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@content and @profile" "-Dtest.environment=profile"
) else if "%choice%"=="3" (
    echo Running Profile Functionality Tests...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@functionality and @profile" "-Dtest.environment=profile"
) else if "%choice%"=="4" (
    echo Running Profile Responsive Tests...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@responsive and @profile" "-Dtest.environment=profile"
) else if "%choice%"=="5" (
    echo Running Profile Advanced Tests...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@advanced and @profile" "-Dtest.environment=profile"
) else if "%choice%"=="6" (
    echo Running Full Profile Test Suite...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@profile" "-Dtest.environment=profile"
) else (
    echo Invalid choice. Running Smoke Tests...
    mvn clean verify "-Plocal,chrome" "-Dtest.cucumber.tags=@smoke and @profile" "-Dtest.environment=profile"
)

if %ERRORLEVEL% neq 0 (
    echo.
    echo Test execution failed!
    echo Please check the logs above for details.
    pause
    exit /b 1
)

echo.
echo ===== Test Execution Summary =====
echo Profile website tests completed successfully!
echo.
echo Reports available at:
echo - HTML Report: target/reports/cucumber-html-report/index.html
echo - JSON Report: target/reports/cucumber-json-report.json
echo - Allure Report: Run 'mvn allure:serve' to view
echo.

set /p openReport="Open HTML report? (y/n): "
if /i "%openReport%"=="y" (
    if exist "target\reports\cucumber-html-report\index.html" (
        start "" "target\reports\cucumber-html-report\index.html"
    ) else (
        echo HTML report not found. Run tests first.
    )
)

echo.
echo Press any key to exit...
pause >nul