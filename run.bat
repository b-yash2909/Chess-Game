@echo off
echo Configuring Terminal for Unicode...
chcp 65001 > nul

echo Compiling Java Chess Application...
javac -encoding UTF-8 -d out -sourcepath src src/Main.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Running Chess Application...
java "-Dfile.encoding=UTF-8" -cp out Main
pause

