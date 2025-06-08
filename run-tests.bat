@echo off
REM Enterprise Cucumber-TestNG Test Execution Scripts
REM Windows Batch file for PowerShell compatibility

echo ===== Enterprise Test Automation Framework =====
echo.

REM Local Execution (Headed)
echo Running Local Tests (Headed)...
mvn clean verify "-Plocal,headed,chrome"
if %ERRORLEVEL% neq 0 (
    echo Test execution failed!
    exit /b 1
)

echo.
echo Tests completed successfully!
pause