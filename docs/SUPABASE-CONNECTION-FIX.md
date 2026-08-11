# Fix Supabase Connection Timeout

## The Problem
Your backend cannot connect to Supabase database - connection is timing out.

## The Solution
Use Supabase **Connection Pooler** instead of direct connection.

## Steps to Fix:

### 1. Get Your Connection String from Supabase

1. Go to your [Supabase Dashboard](https://supabase.com/dashboard/project/cvzgzmoxfwkcajbqksdd)
2. Click **Settings** (gear icon) in the left sidebar
3. Click **Database**
4. Scroll down to **Connection string** section
5. Select **Session mode** (Transaction mode for pooler)
6. Copy the connection string (it will look like):
   ```
   postgresql://postgres.cvzgzmoxfwkcajbqksdd:[YOUR-PASSWORD]@aws-0-ap-south-1.pooler.supabase.com:6543/postgres
   ```

### 2. Update application.properties

Open `backend/src/main/resources/application.properties` and update lines 6-8:

```properties
# If using Connection Pooler (Recommended):
spring.datasource.url=jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres
spring.datasource.username=postgres.cvzgzmoxfwkcajbqksdd
spring.datasource.password=CarChrono@096

# OR if using Direct Connection:
spring.datasource.url=jdbc:postgresql://db.cvzgzmoxfwkcajbqksdd.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=CarChrono@096
```

**Important:** 
- For **pooler** (port 6543): username is `postgres.cvzgzmoxfwkcajbqksdd`
- For **direct** (port 5432): username is just `postgres`
- The pooler hostname might be different (check your Supabase dashboard)

### 3. Alternative: Check if Database is Paused

1. In Supabase Dashboard, go to your project
2. Check if there's a message saying the project is **paused**
3. If paused, click **Resume project**
4. Wait 1-2 minutes for it to start

### 4. Restart Backend

After updating the connection settings:
1. Close the "Backend Server" terminal window
2. Run `start-application.bat` again
3. Wait for backend to start (15-20 seconds)
4. Try logging in with: **admin** / **Admin@123**

## Common Issues:

### Issue 1: Wrong Region
The pooler URL includes a region (e.g., `aws-0-ap-south-1`). Your actual region might be different. Check your Supabase dashboard for the correct URL.

### Issue 2: IPv6 Issues
If you're having connection issues, try disabling IPv6:
```properties
spring.datasource.url=jdbc:postgresql://aws-0-ap-south-1.pooler.supabase.com:6543/postgres?preferQueryMode=simple&ssl=require
```

### Issue 3: Firewall
Make sure your firewall allows outbound connections to:
- Port 5432 (direct connection)
- Port 6543 (connection pooler)

## Test Your Connection

You can test if Supabase is reachable using PowerShell:
```powershell
Test-NetConnection -ComputerName db.cvzgzmoxfwkcajbqksdd.supabase.co -Port 5432
```

If this times out, your firewall or network might be blocking it.
