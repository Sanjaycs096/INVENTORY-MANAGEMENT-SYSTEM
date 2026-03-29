# 📦 INVENTORY MANAGEMENT SYSTEM - Complete Setup Guide  

## 🎯 Project Overview

A **modern, production-ready** full-stack inventory management system built with:
- **Backend**: Java Spring Boot, RESTful APIs
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Database**: Supabase PostgreSQL
- **Security**: JWT Authentication, BCrypt Password Hashing
- **Architecture**: Clean Architecture Pattern

---

## 📁 Project Structure

```
inventory-system/
├── backend/
│   ├── src/main/java/com/inventory/
│   │   ├── controller/      # REST API Controllers
│   │   ├── service/         # Business Logic Layer
│   │   ├── repository/      # Data Access Layer
│   │   ├── model/           # Entity Classes
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── security/        # Security & JWT Configuration
│   │   └── InventoryManagementSystemApplication.java
│   └── pom.xml              # Maven dependencies
├── frontend/
│   ├── css/
│   │   └── styles.css      # Main stylesheet
│   ├── js/
│   │   └── api.js          # API client & utilities
│   ├── dashboard.html       # Main dashboard
│   ├── login.html          # Login page
│   ├── products.html       # Product management
│   ├── categories.html     # Category management
│   ├── suppliers.html      # Supplier management
│   └── transactions.html   # Transaction history
├── database/
│   └── schema.sql             # PostgreSQL Schema
├── setup-maven-simple.bat     # 🔧 Maven setup (CURL - Recommended)
├── setup-maven.bat            # 🔧 Maven setup (PowerShell)
├── setup-maven-manual.bat     # 🔧 Maven setup (Manual guide)
├── start-application.bat      # 🚀 One-click startup (Windows)
├── start-backend-only.bat     # Backend-only startup
├── start-frontend-server.bat  # Frontend-only startup
├── FRONTEND-SERVER-GUIDE.md   # Frontend server guide
└── README.md                  # This file
```

---

## 🚀 Quick Start Guide

### Prerequisites

- **Java 17+** (JDK installed) - [Download](https://adoptium.net/)
- **Maven 3.8+** (OR use our auto-installer scripts)
- **PostgreSQL** (Supabase account) - [Sign up free](https://supabase.com/)
- **Frontend Server** (Choose ONE):
  - **Python 3.x** (Simplest) - [Download](https://www.python.org/downloads/) - **Check "Add to PATH"**
  - **VS Code Live Server** (For developers) - Install extension in VS Code
  - **Node.js http-server** (Alternative) - `npm install -g http-server`
- **Modern web browser** (Chrome, Firefox, Edge)

### ⚡ One-Click Start (Windows)

**First Time Setup:**
1. **Install Maven** (if not installed): Run `setup-maven-simple.bat`
2. **Install Python** (if not installed): Download from [python.org](https://www.python.org/downloads/)
   - ⚠️ **IMPORTANT**: Check "Add Python to PATH" during installation!

**Start Application:**
- **Double-click:** `start-application.bat`

This will:
- ✅ Check Java & Maven installation
- ✅ Start Spring Boot backend (port 8080)
- ✅ Auto-detect Python and start frontend server (port 8000)
- ✅ Open browser automatically with login page
- ✅ Show default credentials

**No Python?** The script will guide you through alternatives!

**Alternative Startup Scripts:**
- `start-backend-only.bat` - Backend only (use with VS Code Live Server)
- `start-frontend-server.bat` - Frontend only (separate terminal)

---

## 🎬 **How to Use the Batch Files**

### **Step 0: Setup Maven (First Time Only)**

**If you don't have Maven installed, choose ONE method:**

#### **Method 1: Quick Setup (Recommended)**
```cmd
setup-maven-simple.bat
```
- Uses built-in Windows CURL
- More reliable download
- ~3-5 minutes

#### **Method 2: Auto Setup**
```cmd
setup-maven.bat
```
- Uses PowerShell download
- Multiple fallback mirrors
- ~2-3 minutes

#### **Method 3: Manual Setup**
```cmd
setup-maven-manual.bat
```
- Gives you step-by-step instructions
- You download from browser
- Most reliable if network issues

**All methods install Maven to:** `tools/apache-maven` (portable, no system changes)

---

### **Option 1: Full Application (Recommended)**

1. **Double-click** `start-application.bat`
2. Wait for servers to start (~20-25 seconds)
3. Browser opens automatically to login page
4. Login with default credentials

**What it does:**
```
✓ Validates Java & Maven installation
✓ Starts Spring Boot backend (localhost:8080)
✓ Starts Python HTTP server for frontend (localhost:8000)
✓ Opens browser to http://localhost:8000/login.html
✓ Press any key to stop all servers
```

### **Option 2: Backend Only**

1. **Double-click** `start-backend-only.bat`
2. Backend starts on port 8080
3. For frontend:
   - **VS Code:** Right-click `frontend/login.html` → "Open with Live Server"
   - **Direct:** Open `frontend/login.html` in browser (API calls will work)
   - **Node.js:** `cd frontend && npx http-server`

### **Option 3: Manual Steps (All Platforms)**

```bash
# Terminal 1: Start Backend
mvn spring-boot:run

# Terminal 2: Start Frontend (choose one)
cd frontend && python -m http.server 8000
# OR use VS Code Live Server
# OR open login.html directly
```

---

### Step 1: Database Setup (Supabase)

1. **Create Supabase Project**
   - Go to [https://supabase.com](https://supabase.com)
   - Create new project
   - Note your database credentials

2. **Run Database Schema**
   - Open Supabase SQL Editor
   - Copy contents from `database/schema.sql`
   - Execute the script
   - ✅ This creates all tables, indexes, triggers, and seed data

3. **Get Connection Details**
   ```
   Host: db.YOUR_PROJECT.supabase.co
   Port: 5432
   Database: postgres
   User: postgres
   Password: [your-password]
   ```

### Step 2: Backend Configuration

1. **Update Application Properties**
   
   Edit: `backend/src/main/resources/application.properties`

   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:postgresql://db.YOUR_PROJECT.supabase.co:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=YOUR_PASSWORD

   # JWT Secret (Change this!)
   jwt.secret=your-super-secret-jwt-key-must-be-at-least-256-bits-long
   ```

2. **Environment Variables (Recommended)**
   
   Create `.env` file (copy from `.env.example`):
   ```bash
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   JWT_SECRET=your-jwt-secret-key
   ```

### Step 3: Build & Run Backend

```bash
# Navigate to project root
cd "c:\Users\sanja\project\stock management system"

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

**Expected Output:**
```
==============================================
Inventory Management System Started
Server running at: http://localhost:8080
==============================================
```

### Step 4: Frontend Setup

1. **Option A: Simple HTTP Server (Python)**
   ```bash
   cd frontend
   python -m http.server 8000
   ```
   Access at: `http://localhost:8000`

2. **Option B: Live Server (VS Code)**
   - Install "Live Server" extension
   - Right-click on `frontend/login.html`
   - Select "Open with Live Server"

3. **Option C: Any Web Server**
   - Use XAMPP, WAMP, or any web server
   - Point to the `frontend` directory

### Step 5: Login & Test

1. **Open Login Page**
   - URL: `http://localhost:8000/login.html`

2. **Default Credentials**
   ```
   Username: admin
   Password: Admin@123
   ```

3. **Expected Flow**
   - Login → Dashboard loads
   - See statistics (products, stock, transactions)
   - Navigate to Products, Categories, Suppliers

---

## 🔐 Security Features

### 1. JWT Authentication
- Stateless authentication
- Token expiration (24 hours)
- Secure token storage

### 2. Password Security
- BCrypt hashing with strength 10
- Never stores plain text passwords

### 3. SQL Injection Prevention
- Prepared statements
- JPA/Hibernate ORM

### 4. CORS Configuration
- Configured for localhost development
- Update for production domains

### 5. Role-Based Access Control
- **ADMIN**: Full access
- **MANAGER**: Manage products, suppliers
- **STAFF**: View and basic operations

---

## 📚 API Documentation

### Authentication Endpoints

#### **POST** `/api/auth/login`
```json
Request:
{
  "username": "admin",
  "password": "Admin@123"
}

Response:
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "username": "admin",
    "role": "ADMIN"
  }
}
```

#### **GET** `/api/auth/me`
- Requires: `Authorization: Bearer {token}`
- Returns: Current user details

### Product Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/products` | Get all products | ✅ |
| GET | `/api/products/{id}` | Get product by ID | ✅ |
| GET | `/api/products/search?name={name}` | Search products | ✅ |
| GET | `/api/products/low-stock` | Get low stock items | ✅ |
| POST | `/api/products` | Create product | ✅ (ADMIN/MANAGER) |
| PUT | `/api/products/{id}` | Update product | ✅ (ADMIN/MANAGER) |
| PATCH | `/api/products/{id}/stock` | Update stock | ✅ |
| DELETE | `/api/products/{id}` | Delete product | ✅ (ADMIN) |

### Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| POST | `/api/categories` | Create category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

### Supplier Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/suppliers` | Get all suppliers |
| POST | `/api/suppliers` | Create supplier |
| PUT | `/api/suppliers/{id}` | Update supplier |
| DELETE | `/api/suppliers/{id}` | Delete supplier |

### Transaction Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/transactions` | Get all transactions |
| GET | `/api/transactions/recent` | Get recent transactions |
| GET | `/api/transactions/product/{id}` | Get by product |
| GET | `/api/transactions/type/{type}` | Get by type |

### Dashboard Endpoint

#### **GET** `/api/dashboard/stats`
```json
Response:
{
  "success": true,
  "data": {
    "totalProducts": 125,
    "lowStockProducts": 8,
    "totalInventoryValue": 125430.50,
    "todayTransactions": 15,
    "totalCategories": 5,
    "totalSuppliers": 12,
    "pendingOrders": 3
  }
}
```

---

## 🎨 Frontend Features

### Dashboard (`dashboard.html`)
- 📊 Real-time statistics cards
- ⚠️ Low stock alerts
- 📈 Category distribution
- 🔄 Recent transactions

### Products Page (`products.html`)
- ➕ Add/Edit/Delete products
- 🔍 Search functionality
- 📋 Full product details
- 🏷️ Category & Supplier mapping

### Categories Page
- Manage product categories
- Track products per category

### Suppliers Page
- Supplier contact management
- Product-supplier relationships

### Transactions Page
- Transaction history
- Filter by type, date, product

---

## 🛠️ Development Guide

### Adding New Feature

1. **Create Entity**
   ```java
   @Entity
   @Table(name = "your_table")
   public class YourEntity {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       // fields...
   }
   ```

2. **Create Repository**
   ```java
   @Repository
   public interface YourRepository extends JpaRepository<YourEntity, Long> {
       // custom queries
   }
   ```

3. **Create Service**
   ```java
   @Service
   public class YourService {
       @Autowired
       private YourRepository repository;
       // business logic
   }
   ```

4. **Create Controller**
   ```java
   @RestController
   @RequestMapping("/api/your-resource")
   public class YourController {
       @Autowired
       private YourService service;
       // endpoints
   }
   ```

### Frontend Integration

```javascript
// Add to api.js
const YourAPI = {
    getAll: () => ApiClient.get('/your-resource'),
    create: (data) => ApiClient.post('/your-resource', data),
    // more methods...
};
```

---

## 🧪 Testing

### Test Backend APIs

Using curl:
```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin@123"}'

# Get Products (with token)
curl http://localhost:8080/api/products \
  -H "Authorization: Bearer YOUR_TOKEN"
```

Using Postman:
1. Import collection (create from endpoints above)
2. Set environment variable for token
3. Test all endpoints

### Frontend Testing

1. Open browser console (F12)
2. Check for errors
3. Verify API calls in Network tab
4. Test CRUD operations

---

## 🚀 Deployment

### Backend Deployment

**Option 1: Heroku**
```bash
heroku create inventory-api
heroku config:set SPRING_PROFILES_ACTIVE=production
git push heroku main
```

**Option 2: AWS Elastic Beanstalk**
```bash
mvn clean package
eb init
eb create inventory-env
eb deploy
```

**Option 3: Docker**
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Frontend Deployment

- **Netlify**: Drag & drop `frontend` folder
- **Vercel**: Connect GitHub repo
- **AWS S3**: Upload as static website

### Update Production Config

1. Change CORS origins in `SecurityConfig.java`
2. Update `API_BASE_URL` in `api.js`
3. Use environment variables for secrets
4. Enable HTTPS

---

## 🐛 Troubleshooting

### Issue: Batch file says "Java not found"
**Solution:**
- Install Java 17+ from [Adoptium](https://adoptium.net/)
- Add Java to PATH environment variable
- Restart terminal and try again

### Issue: Batch file says "Maven not found"
**Solution:**
- **Quick Fix:** Run `setup-maven-simple.bat` (uses built-in CURL - most reliable)
- **Alternative:** Run `setup-maven.bat` (uses PowerShell)
- **Manual:** Run `setup-maven-manual.bat` (download with browser)
- **System Install:** Download from [maven.apache.org](https://maven.apache.org/download.cgi), extract, add to PATH

### Issue: Maven download fails (404 or network error)
**Solution:**
- **Best:** Use `setup-maven-simple.bat` (more reliable)
- **Alternative:** Use `setup-maven-manual.bat` and download manually
- Direct link: [Maven 3.9.5](https://archive.apache.org/dist/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip)
- Save to `tools\maven.zip` then run manual setup

### Issue: "The system cannot find the path specified"
**Solution:**
- This was a path navigation issue in the batch file - **FIXED in latest version**
- Re-download `start-application.bat` and `start-backend-only.bat`
- Ensure you're running from the project root directory
- Try running with Administrator privileges if needed

### Issue: "python is not recognized" or Frontend server fails to start
**Solution:**

**Option 1: Install Python (Simplest)**
1. Download Python from: https://www.python.org/downloads/
2. ⚠️ **CRITICAL**: Check "Add Python to PATH" during installation
3. Verify: Open new terminal and type `python --version`
4. Restart `start-application.bat`

**Option 2: Use VS Code Live Server (Recommended for Developers)**
1. Install "Live Server" extension in VS Code
2. Run `start-backend-only.bat` (starts backend only)
3. Open `frontend/login.html` in VS Code
4. Right-click → "Open with Live Server"

**Option 3: Use Node.js http-server**
```bash
npm install -g http-server
http-server frontend -p 8000
```

**Option 4: Use start-frontend-server.bat**
Run this separately to see detailed Python setup instructions

**See also:** `FRONTEND-SERVER-GUIDE.md` for complete frontend server guide

### Issue: Backend starts but frontend can't connect
**Solution:**
- Check if backend is running on port 8080
- Verify CORS configuration in `SecurityConfig.java`
- Check browser console for errors
- Update `API_BASE_URL` in `frontend\js\api.js` if needed

### Issue: Cannot connect to database
**Solution:**
- Verify Supabase credentials in `application.properties`
- Check if database is running
- Ensure schema.sql was executed successfully
- Check network connectivity

### Issue: JWT token invalid
**Solution:**
- Check JWT secret is properly set
- Verify token hasn't expired (24 hours)
- Clear browser localStorage
- Try logging in again

### Issue: CORS errors
**Solution:**
- Add frontend URL to `SecurityConfig.java`
- Check browser console for specific error

### Issue: Build fails
**Solution:**
```bash
mvn clean
mvn dependency:resolve
mvn install -DskipTests
```

### Issue: Port already in use
**Solution:**
```bash
# Find process using port 8080
netstat -ano | findstr :8080
# Kill the process (replace PID)
taskkill /PID <process_id> /F
```

---

## 📊 Database Schema Overview

### Main Tables
- **users**: System users with roles
- **products**: Product inventory
- **categories**: Product categories
- **suppliers**: Supplier information
- **transactions**: Stock movements
- **orders**: Purchase orders
- **order_items**: Order line items

### Key Relationships
- Product ↔ Category (Many-to-One)
- Product ↔ Supplier (Many-to-One)
- Transaction ↔ Product (Many-to-One)
- Transaction ↔ User (Many-to-One)
- Order ↔ OrderItems (One-to-Many)
---


## 🎓 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Supabase Docs](https://supabase.com/docs)
- [JWT Introduction](https://jwt.io/introduction)

---

**🎉 Congratulations! Your modern inventory system is ready!**
