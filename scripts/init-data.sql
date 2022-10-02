-- Insert Roles
INSERT INTO roles (name, description) VALUES
('ROLE_ADMIN', 'Administrator role with full access'),
('ROLE_USER', 'Regular user role'),
('ROLE_MANAGER', 'Manager role with limited admin access');

-- Insert Admin User (password: admin123 hashed with BCrypt)
INSERT INTO users (username, email, password_hash, first_name, last_name, phone, active) VALUES
('admin', 'admin@bank.com', '$2a$10$dXj3SW6G7P50eS3B0/gZ2OPST9/PgBkqquzi.Ss7KIUgO2t0jWMUm', 'Admin', 'User', '0901234567', TRUE);

-- Insert User-Role Mapping
INSERT INTO user_roles (user_id, role_id) VALUES
((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));

-- Insert Customers
INSERT INTO customers (cin, first_name, last_name, email, phone, address, city) VALUES
('123456789', 'John', 'Doe', 'john.doe@example.com', '0912345678', '123 Main St', 'New York'),
('987654321', 'Jane', 'Smith', 'jane.smith@example.com', '0923456789', '456 Oak Ave', 'Los Angeles'),
('555666777', 'Michael', 'Johnson', 'michael.j@example.com', '0934567890', '789 Pine Rd', 'Chicago');

-- Insert Accounts
INSERT INTO accounts (customer_id, account_number, currency, balance, status) VALUES
((SELECT id FROM customers WHERE cin = '123456789'), 'ACC001', 'USD', 5000.00, 'ACTIVE'),
((SELECT id FROM customers WHERE cin = '123456789'), 'ACC002', 'EUR', 3000.00, 'ACTIVE'),
((SELECT id FROM customers WHERE cin = '987654321'), 'ACC003', 'USD', 10000.00, 'ACTIVE'),
((SELECT id FROM customers WHERE cin = '555666777'), 'ACC004', 'GBP', 7500.00, 'ACTIVE');

-- Insert Operations
INSERT INTO operations (account_id, operation_type, amount, description) VALUES
((SELECT id FROM accounts WHERE account_number = 'ACC001'), 'CREDIT', 1000.00, 'Initial deposit'),
((SELECT id FROM accounts WHERE account_number = 'ACC001'), 'DEBIT', 100.00, 'ATM withdrawal'),
((SELECT id FROM accounts WHERE account_number = 'ACC002'), 'CREDIT', 500.00, 'Transfer from ACC001'),
((SELECT id FROM accounts WHERE account_number = 'ACC003'), 'CREDIT', 2000.00, 'Salary deposit'),
((SELECT id FROM accounts WHERE account_number = 'ACC003'), 'DEBIT', 500.00, 'Bill payment'),
((SELECT id FROM accounts WHERE account_number = 'ACC004'), 'CREDIT', 1500.00, 'Transfer from ACC003'),
((SELECT id FROM accounts WHERE account_number = 'ACC004'), 'DEBIT', 200.00, 'Service charge');
