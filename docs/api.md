# API Documentation

The InvenTrack backend exposes a RESTful API. All endpoints (except login) require a valid JWT token in the `Authorization` header.

```http
Authorization: Bearer <your_jwt_token>
```

## Authentication

### Login
- **URL:** `/api/auth/login`
- **Method:** `POST`
- **Body:** `{"username": "admin", "password": "password"}`
- **Success Response:** `200 OK`
  ```json
  {
    "success": true,
    "data": { "token": "jwt...", "username": "admin", "role": "ADMIN" }
  }
  ```

## Products

### Get All Products
- **URL:** `/api/products`
- **Method:** `GET`
- **Role:** All authenticated users.

### Create Product
- **URL:** `/api/products`
- **Method:** `POST`
- **Role:** ADMIN, MANAGER
- **Body:**
  ```json
  {
    "name": "Laptop",
    "sku": "LAP-001",
    "description": "High-end laptop",
    "price": 1200.00,
    "cost": 900.00,
    "stockQuantity": 15,
    "minStockLevel": 5,
    "categoryId": 1,
    "supplierId": 1
  }
  ```

### Update Stock
- **URL:** `/api/products/{id}/stock`
- **Method:** `PATCH`
- **Role:** All authenticated users.
- **Body:**
  ```json
  {
    "quantity": 5,
    "type": "IN",
    "notes": "Restock"
  }
  ```

## Dashboard

### Get Statistics
- **URL:** `/api/dashboard/stats`
- **Method:** `GET`
- **Response:**
  ```json
  {
    "success": true,
    "data": {
      "totalProducts": 42,
      "lowStockProducts": 3,
      "totalInventoryValue": 45000.50,
      "todayTransactions": 12
    }
  }
  ```
