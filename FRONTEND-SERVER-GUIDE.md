# Frontend Server Setup Guide

## Quick Start (Choose ONE method)

### ✅ Method 1: Python HTTP Server (Simplest)

**Install Python:**
1. Download Python 3.x from: https://www.python.org/downloads/
2. **IMPORTANT**: During installation, check "Add Python to PATH"
3. Verify installation:
   ```
   python --version
   ```
4. Run the application:
   ```
   start-application.bat
   ```

**Manual Frontend Start:**
```bash
cd frontend
python -m http.server 8000
```
Then open: http://localhost:8000/login.html

---

### ✅ Method 2: VS Code Live Server (Recommended for Developers)

**Setup:**
1. Open VS Code
2. Install extension: "Live Server" by Ritwick Dey
3. Open `frontend/login.html` in VS Code
4. Right-click → "Open with Live Server"

**Advantages:**
- Auto-reload on file changes
- No Python needed
- Better development experience

**Note:** Still need to start backend with:
```bash
start-backend-only.bat
```

---

### ✅ Method 3: Node.js http-server

**If you have Node.js installed:**
```bash
npm install -g http-server
http-server frontend -p 8000 -c-1
```

---

### ⚠️ Method 4: Direct File Open (Emergency Only)

Simply double-click `frontend/login.html`

**Limitations:**
- CORS errors may occur
- API calls might fail
- Use only for UI testing

---

## Recommended Setup

**For End Users:**
→ Install Python and use `start-application.bat`

**For Developers:**
→ Use VS Code Live Server + `start-backend-only.bat`

---

## Troubleshooting

### "python is not recognized"
- Python not installed OR not in PATH
- **Solution**: Reinstall Python, ensure "Add to PATH" is checked

### "Port 8000 already in use"
- Another app is using port 8000
- **Solution**: Close other apps or change port in scripts

### CORS Error in Browser
- Using direct file:// protocol
- **Solution**: Use a real HTTP server (Methods 1-3)

---

## Quick Command Reference

```bash
# Start full application (with Python)
start-application.bat

# Start backend only (use for VS Code Live Server)
start-backend-only.bat

# Start frontend only (separate terminal)
start-frontend-server.bat
```

---

## Need Help?

Check the main README.md for complete setup instructions.
