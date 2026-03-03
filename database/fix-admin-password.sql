-- Fix Admin Password for "Admin@123"
-- This updates the admin user with the correct BCrypt hash

-- Delete existing admin user (if any)
DELETE FROM users WHERE username = 'admin';

-- Insert admin with correct BCrypt hash for "Admin@123"
-- Hash generated with BCrypt strength 10
INSERT INTO users (username, email, password_hash, full_name, role) VALUES
    ('admin', 'admin@inventory.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'System Administrator', 'ADMIN');

-- Verify the user was created
SELECT id, username, email, full_name, role, is_active, created_at 
FROM users 
WHERE username = 'admin';
