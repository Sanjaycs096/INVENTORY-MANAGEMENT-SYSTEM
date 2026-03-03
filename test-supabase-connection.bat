@echo off
title Test Supabase Connection
color 0E
echo ============================================
echo    SUPABASE CONNECTION TEST
echo ============================================
echo.

echo Testing connection to Supabase database...
echo.

echo [1/3] Testing DNS resolution...
nslookup db.cvzgzmoxfwkcajbqksdd.supabase.co
echo.

echo [2/3] Testing port 5432 (Direct Connection)...
powershell -Command "Test-NetConnection -ComputerName db.cvzgzmoxfwkcajbqksdd.supabase.co -Port 5432 -InformationLevel Detailed"
echo.

echo [3/3] Testing port 6543 (Connection Pooler)...
powershell -Command "Test-NetConnection -ComputerName aws-0-ap-south-1.pooler.supabase.com -Port 6543 -InformationLevel Detailed"
echo.

echo ============================================
echo    TEST COMPLETE
echo ============================================
echo.
echo If you see "TcpTestSucceeded : True" above, the connection is working.
echo If you see "False" or timeout, check:
echo   1. Is your Supabase project paused?
echo   2. Is your firewall blocking the connection?
echo   3. Is your internet connection working?
echo.
pause
