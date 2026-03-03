-- ===================================================
-- INVENTORY MANAGEMENT SYSTEM - DATABASE SCHEMA
-- Database: PostgreSQL (Supabase)
-- ===================================================

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===================================================
-- TABLE: categories
-- ===================================================
CREATE TABLE IF NOT EXISTS categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===================================================
-- TABLE: suppliers
-- ===================================================
CREATE TABLE IF NOT EXISTS suppliers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    contact_person VARCHAR(150),
    phone VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- ===================================================
-- TABLE: users
-- ===================================================
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150),
    role VARCHAR(20) NOT NULL DEFAULT 'STAFF',
    is_active BOOLEAN DEFAULT TRUE,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT role_check CHECK (role IN ('ADMIN', 'STAFF', 'MANAGER')),
    CONSTRAINT email_format_users CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- ===================================================
-- TABLE: products
-- ===================================================
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL,
    supplier_id INTEGER REFERENCES suppliers(id) ON DELETE SET NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    min_stock_level INTEGER DEFAULT 10,
    unit VARCHAR(20) DEFAULT 'PCS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT price_positive CHECK (price >= 0),
    CONSTRAINT quantity_non_negative CHECK (quantity >= 0),
    CONSTRAINT min_stock_positive CHECK (min_stock_level >= 0)
);

-- ===================================================
-- TABLE: transactions
-- ===================================================
CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    transaction_code VARCHAR(50) UNIQUE NOT NULL,
    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    transaction_type VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2),
    total_amount DECIMAL(10, 2),
    previous_quantity INTEGER,
    new_quantity INTEGER,
    notes TEXT,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT type_check CHECK (transaction_type IN ('STOCK_IN', 'STOCK_OUT', 'ADJUSTMENT', 'RETURN')),
    CONSTRAINT quantity_positive CHECK (quantity > 0)
);

-- ===================================================
-- TABLE: orders
-- ===================================================
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    order_code VARCHAR(50) UNIQUE NOT NULL,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    supplier_id INTEGER REFERENCES suppliers(id) ON DELETE SET NULL,
    order_status VARCHAR(20) DEFAULT 'PENDING',
    total_amount DECIMAL(10, 2) DEFAULT 0,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expected_delivery_date DATE,
    actual_delivery_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT status_check CHECK (order_status IN ('PENDING', 'APPROVED', 'DELIVERED', 'CANCELLED'))
);

-- ===================================================
-- TABLE: order_items
-- ===================================================
CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id INTEGER NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    CONSTRAINT quantity_positive_order CHECK (quantity > 0),
    CONSTRAINT price_positive_order CHECK (unit_price >= 0)
);

-- ===================================================
-- INDEXES FOR PERFORMANCE
-- ===================================================
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category_id);
CREATE INDEX IF NOT EXISTS idx_products_supplier ON products(supplier_id);
CREATE INDEX IF NOT EXISTS idx_products_name ON products(name);
CREATE INDEX IF NOT EXISTS idx_products_code ON products(product_code);
CREATE INDEX IF NOT EXISTS idx_transactions_product ON transactions(product_id);
CREATE INDEX IF NOT EXISTS idx_transactions_user ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(transaction_type);
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_supplier ON orders(supplier_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(order_status);
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- ===================================================
-- TRIGGERS FOR UPDATED_AT
-- ===================================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS update_categories_updated_at ON categories;
CREATE TRIGGER update_categories_updated_at BEFORE UPDATE ON categories
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_suppliers_updated_at ON suppliers;
CREATE TRIGGER update_suppliers_updated_at BEFORE UPDATE ON suppliers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_products_updated_at ON products;
CREATE TRIGGER update_products_updated_at BEFORE UPDATE ON products
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_orders_updated_at ON orders;
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- ===================================================
-- SEED DATA
-- ===================================================

-- Insert default categories
INSERT INTO categories (name, description) VALUES
    ('Electronics', 'Electronic devices and components'),
    ('Furniture', 'Office and home furniture'),
    ('Stationery', 'Office supplies and stationery items'),
    ('Food & Beverage', 'Food and beverage products'),
    ('Clothing', 'Clothing and apparel items')
ON CONFLICT (name) DO NOTHING;

-- Insert default admin user (password: Admin@123)
-- Password hash generated using BCrypt with strength 10
INSERT INTO users (username, email, password_hash, full_name, role) VALUES
    ('admin', 'admin@inventory.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'System Administrator', 'ADMIN'),
    ('staff1', 'staff@inventory.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Staff User', 'STAFF')
ON CONFLICT (username) DO NOTHING;

-- Insert sample suppliers
INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
    ('TechSupply Co.', 'John Doe', '+1-234-567-8900', 'contact@techsupply.com', '123 Tech Street, Silicon Valley, CA'),
    ('Office Depot Inc.', 'Jane Smith', '+1-234-567-8901', 'sales@officedepot.com', '456 Commerce Ave, New York, NY'),
    ('Global Traders', 'Mike Johnson', '+1-234-567-8902', 'info@globaltraders.com', '789 Trade Road, Chicago, IL')
ON CONFLICT (email) DO NOTHING;

-- Insert sample products
INSERT INTO products (product_code, name, description, category_id, supplier_id, price, quantity, min_stock_level, unit) VALUES
    ('PROD-001', 'Laptop Dell XPS 15', 'High-performance laptop with 16GB RAM', 1, 1, 1299.99, 25, 5, 'PCS'),
    ('PROD-002', 'Office Desk', 'Ergonomic office desk with adjustable height', 2, 2, 399.99, 15, 3, 'PCS'),
    ('PROD-003', 'Wireless Mouse', 'Bluetooth wireless mouse', 1, 1, 29.99, 100, 20, 'PCS'),
    ('PROD-004', 'Notebook A4', 'Professional notebook 200 pages', 3, 2, 5.99, 200, 50, 'PCS'),
    ('PROD-005', 'Office Chair', 'Ergonomic office chair with lumbar support', 2, 2, 249.99, 30, 5, 'PCS')
ON CONFLICT (product_code) DO NOTHING;

-- ===================================================
-- VIEWS FOR REPORTING
-- ===================================================

-- View: Low stock products
CREATE OR REPLACE VIEW low_stock_products AS
SELECT 
    p.id,
    p.product_code,
    p.name,
    p.quantity,
    p.min_stock_level,
    c.name AS category_name,
    s.name AS supplier_name
FROM products p
LEFT JOIN categories c ON p.category_id = c.id
LEFT JOIN suppliers s ON p.supplier_id = s.id
WHERE p.quantity <= p.min_stock_level;

-- View: Product inventory value
CREATE OR REPLACE VIEW inventory_value AS
SELECT 
    p.id,
    p.product_code,
    p.name,
    p.quantity,
    p.price,
    (p.quantity * p.price) AS total_value,
    c.name AS category_name
FROM products p
LEFT JOIN categories c ON p.category_id = c.id;

-- View: Transaction summary
CREATE OR REPLACE VIEW transaction_summary AS
SELECT 
    t.id,
    t.transaction_code,
    t.transaction_type,
    t.quantity,
    t.total_amount,
    t.transaction_date,
    p.name AS product_name,
    p.product_code,
    u.username AS user_name
FROM transactions t
JOIN products p ON t.product_id = p.id
JOIN users u ON t.user_id = u.id
ORDER BY t.transaction_date DESC;

-- ===================================================
-- FUNCTIONS FOR BUSINESS LOGIC
-- ===================================================

-- Function: Get total inventory value
CREATE OR REPLACE FUNCTION get_total_inventory_value()
RETURNS DECIMAL(15, 2) AS $$
BEGIN
    RETURN (SELECT COALESCE(SUM(quantity * price), 0) FROM products);
END;
$$ LANGUAGE plpgsql;

-- Function: Get product count by category
CREATE OR REPLACE FUNCTION get_product_count_by_category(category_input_id INTEGER)
RETURNS INTEGER AS $$
BEGIN
    RETURN (SELECT COUNT(*) FROM products WHERE category_id = category_input_id);
END;
$$ LANGUAGE plpgsql;

-- ===================================================
-- PERMISSIONS (Optional - for RLS in Supabase)
-- ===================================================

-- Enable Row Level Security (RLS) if needed
-- ALTER TABLE products ENABLE ROW LEVEL SECURITY;
-- ALTER TABLE transactions ENABLE ROW LEVEL SECURITY;
-- ALTER TABLE orders ENABLE ROW LEVEL SECURITY;

-- Create policies as needed for your security requirements

-- ===================================================
-- END OF SCHEMA
-- ===================================================
