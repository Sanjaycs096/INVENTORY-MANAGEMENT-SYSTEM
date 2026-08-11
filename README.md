<div align="center">
  
# 📦 InvenTrack

**A Production-Grade Full-Stack Inventory Management System**

InvenTrack is a robust, secure, and scalable inventory management solution built to streamline operations, track stock movements, and manage suppliers with an intuitive web interface and a powerful REST API.

[Live Demo](https://inventrack.vercel.app) · [Report Bug](https://github.com/Sanjaycs096/INVENTORY-MANAGEMENT-SYSTEM/issues) · [Request Feature](https://github.com/Sanjaycs096/INVENTORY-MANAGEMENT-SYSTEM/issues)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)
![Vanilla JS](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)

</div>

---

## 📑 Table of Contents
- [Overview](#-overview)
- [Key Features](#-key-features)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Demo](#-demo)
- [Getting Started](#-getting-started)
- [Environment Variables](#-environment-variables)
- [API Documentation](#-api-documentation)
- [Security](#-security)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Contributing](#-contributing)
- [License](#-license)

---

## 📖 Overview

InvenTrack solves the challenge of modern inventory control for small to medium-sized businesses. Originally migrated from a legacy Swing GUI application, this modernized platform provides a multi-user, web-based interface with stateless authentication and robust cloud-native architecture. 

It is designed for warehouse managers, staff, and system administrators to maintain real-time visibility into stock levels, track historical transactions, and manage procurement.

---

## ✨ Key Features

- **Real-time Dashboard:** Instantly view key metrics like total value, low-stock warnings, and recent transaction volume.
- **Product & Stock Management:** Complete CRUD operations for inventory items with categorization.
- **Supplier Directory:** Maintain relationships and contact information for product vendors.
- **Transaction Logging:** Immutable ledger of all `STOCK_IN`, `STOCK_OUT`, and `ADJUSTMENT` events.
- **Role-Based Access Control (RBAC):** Distinct permissions for `ADMIN`, `MANAGER`, and `STAFF`.
- **Stateless Security:** JWT-based authentication ensuring secure, RESTful communication.

---

## 📸 Screenshots

*(TODO: Add application screenshots here once UI updates are finalized.)*
- `docs/screenshots/dashboard.png`
- `docs/screenshots/inventory-view.png`
- `docs/screenshots/mobile-view.png`

---

## 🏗 Architecture

The system follows a Clean Architecture pattern, cleanly separating the frontend client from the backend API.

- **Frontend:** Lightweight, zero-dependency Vanilla HTML/CSS/JS deployed via edge CDN.
- **Backend:** N-Tier Spring Boot application handling business logic, validation, and data persistence.
- **Database:** PostgreSQL hosted on Supabase, utilizing relational integrity and automated indexing.

For an in-depth look, see the [Architecture Documentation](docs/architecture.md).

---

## 💻 Tech Stack

### Frontend
- **Core:** HTML5, CSS3, Vanilla JavaScript (ES6+)
- **Hosting:** Vercel

### Backend
- **Framework:** Java 17, Spring Boot 3.2.0
- **Security:** Spring Security, JWT (io.jsonwebtoken)
- **Data Access:** Spring Data JPA, Hibernate
- **Hosting:** Render (Dockerized)

### Database & Infrastructure
- **Database:** PostgreSQL (Supabase)
- **CI/CD:** GitHub Actions (Maven Build Validation)
- **Dependency Management:** Dependabot

---

## 📁 Project Structure

```text
INVENTORY-MANAGEMENT-SYSTEM/
├── backend/                  # Java Spring Boot API
│   ├── src/main/java/...     # Controllers, Services, Repositories, Models
│   ├── src/main/resources/   # application.properties
│   ├── Dockerfile            # Production container configuration
│   └── pom.xml               # Maven dependencies
├── frontend/                 # Vanilla JS Client App
│   ├── css/                  # Stylesheets
│   ├── js/                   # API client and logic
│   └── *.html                # Views (dashboard, products, etc.)
├── database/                 # SQL Schema and Seed Data
├── docs/                     # Technical Documentation
└── .github/                  # CI/CD Workflows & Templates
```

---

## 🚀 Demo

- **Live Frontend:** [https://inventrack.vercel.app](https://inventrack.vercel.app)
- **Live API Endpoint:** [https://inventrack-api-sgt3.onrender.com/api/actuator/health](https://inventrack-api-sgt3.onrender.com/api/actuator/health)

*(Note: The Render free tier backend may take 30-50 seconds to spin up on the first request.)*

---

## 🛠 Getting Started

### Prerequisites
- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL** (Local or Supabase)

### Local Development

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Sanjaycs096/INVENTORY-MANAGEMENT-SYSTEM.git
   cd INVENTORY-MANAGEMENT-SYSTEM
   ```

2. **Database Setup:**
   Execute the `database/schema.sql` script against your PostgreSQL instance to create the tables and seed data.

3. **Backend Setup:**
   ```bash
   cd backend
   # Set your environment variables (see below)
   mvn spring-boot:run
   ```
   *The API will start on `http://localhost:8080`.*

4. **Frontend Setup:**
   Serve the `frontend/` directory using any HTTP server:
   ```bash
   cd ../frontend
   python -m http.server 8000
   # OR use Node: npx http-server -p 8000
   ```
   *Access the application at `http://localhost:8000`.*

> **Windows Users:** You can also use the provided `start-application.bat` script in the root directory for a one-click automated startup sequence.

---

## 🔐 Environment Variables

Create an `.env` file in the root (or `backend/` directory) for local development:

```env
DB_URL=jdbc:postgresql://your-host:5432/postgres
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password
JWT_SECRET=your_very_long_secure_jwt_secret_key
CORS_ALLOWED_ORIGINS=http://localhost:8000,http://127.0.0.1:8000
PORT=8080
```

---

## 📚 API Documentation

The REST API requires a Bearer JWT token for all authenticated routes.
Detailed endpoint documentation can be found in [docs/api.md](docs/api.md).

---

## 🛡 Security

- **Authentication:** Token-based (JWT) with configurable expiration.
- **Passwords:** BCrypt hashing (strength 10).
- **SQL Injection:** Mitigated via Spring Data JPA / Hibernate parameterized queries.
- **CORS:** Strictly enforced via Spring Security configuration, restricting cross-origin requests to trusted domains.

---

## 🧪 Testing

The backend includes automated CI/CD validation via GitHub Actions.

Run the test suite locally:
```bash
cd backend
mvn test
```

---

## ☁️ Deployment

### Backend (Render)
The API is deployed via Docker to Render. The configuration is defined in `render.yaml` and relies on the `backend/Dockerfile`. Environment variables (like database credentials and JWT secrets) are injected securely via the Render dashboard.

### Frontend (Vercel)
The UI is deployed globally via Vercel. Configuration is handled by `vercel.json`, which automatically sets up security headers and configures reverse-proxy rewrites from `/api/*` to the Render backend, mitigating CORS complexities in production.

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:
1. Fork the project.
2. Create your feature branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request using our [PR Template](.github/pull_request_template.md).

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---
<div align="center">
  <i>Engineered for Reliability and Scale.</i>
</div>
