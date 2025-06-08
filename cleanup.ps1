# PowerShell cleanup script for handling file locks

Write-Host "===== Cleanup Script =====" -ForegroundColor Green
Write-Host "Stopping browser processes and cleaning up files..." -ForegroundColor Yellow

# Function to safely stop processes
function Stop-ProcessSafely {
    param([string]$ProcessName)
    
    try {
        $processes = Get-Process -Name $ProcessName -ErrorAction SilentlyContinue
        if ($processes) {
            Write-Host "Stopping $ProcessName processes..." -ForegroundColor Cyan
            $processes | Stop-Process -Force -ErrorAction SilentlyContinue
            Start-Sleep -Seconds 1
        }
    }
    catch {
        # Ignore errors - process might not exist
    }
}

# Stop browser and driver processes
Stop-ProcessSafely "chrome"
Stop-ProcessSafely "chromedriver"
Stop-ProcessSafely "firefox"
Stop-ProcessSafely "geckodriver"
Stop-ProcessSafely "msedge"
Stop-ProcessSafely "msedgedriver"

# Wait for processes to fully terminate
Start-Sleep -Seconds 2

# Remove target directory
if (Test-Path "target") {
    Write-Host "Removing target directory..." -ForegroundColor Cyan
    try {
        Remove-Item -Path "target" -Recurse -Force -ErrorAction Stop
        Write-Host "Target directory cleaned successfully!" -ForegroundColor Green
    }
    catch {
        Write-Host "Warning: Some files in target directory could not be deleted" -ForegroundColor Yellow
        Write-Host "This is usually due to file locks or permissions" -ForegroundColor Yellow
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "Cleanup completed!" -ForegroundColor Green
Write-Host ""