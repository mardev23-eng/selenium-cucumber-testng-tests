@echo off
REM Safe test execution script with cleanup

echo ===== Enterprise Test Automation Framework (Safe Mode) =====
echo.

echo Running cleanup before tests...
call cleanup.bat

echo.
echo Running Local Tests (Headed Chrome)...
mvn clean verify "-Plocal,headed,chrome"

if %ERRORLEVEL% neq 0 (
    echo Test execution failed!
    echo Running cleanup after failure...
    call cleanup.bat
    exit /b 1
)

echo.
echo Running final cleanup...
call cleanup.bat

echo.
echo Tests completed successfully!
pause