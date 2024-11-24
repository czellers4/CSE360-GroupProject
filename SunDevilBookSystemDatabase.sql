CREATE DATABASE SunDevilBookSystem;

USE SunDevilBookSystem;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'buyer', 'seller') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category ENUM('Math', 'Computer', 'Natural Science', 'Other') NOT NULL,
    `condition` ENUM('Used Like New', 'Moderately Used', 'Heavily Used') NOT NULL, 
    price DECIMAL(10, 2) NOT NULL,
    seller_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Carts table
CREATE TABLE IF NOT EXISTS carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Cart Items table (association between carts and books)
CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Transactions table


CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_title VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    transaction_date DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
-- CREATE session table
CREATE TABLE session (
    session_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    session_start DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    session_end DATETIME DEFAULT NULL,
    status ENUM('active', 'inactive') DEFAULT 'active',
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- create statistics table
CREATE TABLE statistics (
    stat_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT DEFAULT NULL,
    category ENUM('books_sold', 'books_purchased', 'revenue', 'total_users') NOT NULL,
    value DOUBLE NOT NULL DEFAULT 0,
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



USE SunDevilBookSystem;
INSERT INTO users (username, password, role, email)
VALUES 
('admin', 'adminpass', 'Admin', 'admin@example.com'),
('buyer1', 'buyerpass1', 'Buyer', 'buyer1@example.com'),
('buyer2', 'buyerpass2', 'Buyer', 'buyer2@example.com'),
('seller1', 'sellerpass1', 'Seller', 'seller1@example.com'),
('seller2', 'sellerpass2', 'Seller', 'seller2@example.com');





