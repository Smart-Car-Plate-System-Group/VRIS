@echo off
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
    echo Java JDK 17 or newer is required. Install it and try again.
    pause
    exit /b 1
)
javac -encoding UTF-8 SmartCarPlateSystem.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b 1
)
java SmartCarPlateSystem
if errorlevel 1 pause
