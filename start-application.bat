@echo off
title InvenTrack
color 0A
echo ============================================
echo    InvenTrack
echo    Starting Application...
echo ============================================
echo.

REM Check if Java is installed
echo [1/5] Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 or higher from: https://adoptium.net/
    pause
    exit /b 1
)
echo [OK] Java is installed
echo.

REM Check if Maven is installed
echo [2/5] Checking Maven installation...
set MAVEN_CMD=mvn
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [WARN] Maven not found in system PATH
    echo [INFO] Checking for portable Maven...
    
    if exist "tools\apache-maven\bin\mvn.cmd" (
        echo [OK] Using portable Maven
        set MAVEN_CMD=%~dp0tools\apache-maven\bin\mvn.cmd
    ) else (
        echo [ERROR] Maven not found!
        echo.
        echo Please run 'setup-maven.bat' to install portable Maven
        echo OR install Maven manually from: https://maven.apache.org/download.cgi
        echo.
        pause
        exit /b 1
    )
) else (
    echo [OK] Maven is installed
)
echo.

REM Start Backend Server
echo [3/5] Starting Spring Boot Backend...
echo Backend will run on: http://localhost:8080
start "Backend Server" cmd /k "cd /d "%~dp0backend" && "%MAVEN_CMD%" spring-boot:run"
echo [OK] Backend server starting...
echo.

REM Wait for backend to initialize
echo [4/5] Waiting for backend to initialize (20 seconds)...
timeout /t 20 /nobreak >nul
echo [OK] Backend should be ready
echo.

REM Start Frontend Server
echo [5/5] Starting Frontend Server...
echo.

REM Check for Python
python --version >nul 2>&1
if not errorlevel 1 (
    echo [OK] Using Python HTTP Server
    echo Frontend will run on: http://localhost:8000
    set FRONTEND_DIR=%~dp0frontend
    start "Frontend Server" cmd /k "cd /d "%~dp0frontend" && python -m http.server 8000"
    set FRONTEND_URL=http://localhost:8000/index.html
    goto :frontend_ready
)

REM Try py command (Windows Python Launcher)
py -3 --version >nul 2>&1
if not errorlevel 1 (
    echo [OK] Using Python HTTP Server (py launcher)
    echo Frontend will run on: http://localhost:8000
    start "Frontend Server" cmd /k "cd /d "%~dp0frontend" && py -3 -m http.server 8000"
    set FRONTEND_URL=http://localhost:8000/index.html
    goto :frontend_ready
)

REM Python not found - show manual instructions
echo [WARN] Python not found!
echo.
echo FRONTEND SERVER NOT STARTED
echo.
echo Please choose ONE of these options:
echo.
echo Option 1: Install Python 3.x
echo   Download from: https://www.python.org/downloads/
echo   Then re-run this script
echo.
echo Option 2: Use VS Code Live Server Extension
echo   1. Install "Live Server" extension in VS Code
echo   2. Open frontend\index.html
echo   3. Right-click and select "Open with Live Server"
echo.
echo Option 3: Open directly in browser (limited functionality)
echo   Open this file: frontend\index.html
echo.
echo Backend is running - you can use Option 2 or 3 now!
echo.
set FRONTEND_URL=frontend\index.html
pause
goto :frontend_ready

:frontend_ready
echo [OK] Frontend setup complete
echo.

REM Wait a moment for frontend server
timeout /t 3 /nobreak >nul

REM Open browser
echo ============================================
echo    APPLICATION STARTED SUCCESSFULLY!
echo ============================================
echo.
echo Backend API:  http://localhost:8080/api
echo Frontend UI:  http://localhost:8000/index.html
echo.
echo Default Login Credentials:
echo   Admin    -  Username: admin  /  Password: 123@Admin
echo   Demo     -  Username: demo   /  Password: demo001
echo.
echo Opening browser...
if defined FRONTEND_URL (
    start %FRONTEND_URL%
) else (
    start http://localhost:8000/index.html
)

echo.
echo ============================================
echo Press any key to STOP all servers...
echo ============================================
pause >nul

REM Kill servers
echo.
echo Stopping servers...
taskkill /FI "WindowTitle eq Backend Server*" /F >nul 2>&1
taskkill /FI "WindowTitle eq Frontend Server*" /F >nul 2>&1
echo Servers stopped.
echo.
echo Goodbye!
timeout /t 2 >nul
exit
