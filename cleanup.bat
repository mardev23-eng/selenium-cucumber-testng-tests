@echo off
REM Cleanup script for Windows to handle file locks

echo ===== Cleanup Script =====
echo Stopping browser processes and cleaning up files...

REM Kill any remaining browser processes
taskkill /F /IM chrome.exe /T 2>nul
taskkill /F /IM chromedriver.exe /T 2>nul
taskkill /F /IM firefox.exe /T 2>nul
taskkill /F /IM geckodriver.exe /T 2>nul
taskkill /F /IM msedge.exe /T 2>nul
taskkill /F /IM msedgedriver.exe /T 2>nul

REM Wait a moment for processes to terminate
timeout /t 2 /nobreak >nul

REM Force delete target directory if it exists
if exist "target" (
    echo Removing target directory...
    rmdir /s /q "target" 2>nul
    if exist "target" (
        echo Warning: Some files in target directory could not be deleted
        echo This is usually due to file locks or permissions
    ) else (
        echo Target directory cleaned successfully
    )
)

echo Cleanup completed!
echo.