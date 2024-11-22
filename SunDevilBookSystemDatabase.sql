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
    `condition` ENUM('Used Like New', 'Moderately Used', 'Heavily Used') NOT NULL, -- Enclose 'condition' in backticks
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
CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    buyer_id INT NOT NULL,
    book_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);



INSERT INTO users (username, password, role, email)
VALUES 
('admin', 'adminpass', 'Admin', 'admin@example.com'),
('buyer1', 'buyerpass1', 'Buyer', 'buyer1@example.com'),
('buyer2', 'buyerpass2', 'Buyer', 'buyer2@example.com'),
('seller1', 'sellerpass1', 'Seller', 'seller1@example.com'),
('seller2', 'sellerpass2', 'Seller', 'seller2@example.com');



INSERT INTO books (title, category, `condition`, price, seller_id) 
VALUES 
    ('Calculus 101', 'Math', 'Used Like New', 25.99, 1),
    ('Introduction to Algorithms', 'Computer', 'Moderately Used', 50.75, 2),
    ('Physics for Scientists', 'Natural Science', 'Heavily Used', 15.50, 3),
    ('Java Programming', 'Computer', 'Used Like New', 45.00, 1),
    ('Organic Chemistry', 'Natural Science', 'Moderately Used', 30.00, 2);

INSERT INTO books (title, category, `condition`, price, seller_id) 
VALUES
    ('Advanced Mathematics', 'Math', 'Used Like New', 35.99, 1),
    ('Introduction to Algorithms', 'Computer', 'Moderately Used', 45.50, 2),
    ('Natural Science Fundamentals', 'Natural Science', 'Heavily Used', 25.00, 3),
    ('Discrete Mathematics', 'Math', 'Moderately Used', 30.00, 1),
    ('Programming in Java', 'Computer', 'Used Like New', 40.00, 2),
    ('Organic Chemistry Basics', 'Natural Science', 'Moderately Used', 50.00, 3),
    ('Linear Algebra Simplified', 'Math', 'Heavily Used', 20.00, 1),
    ('Data Structures and Algorithms', 'Computer', 'Used Like New', 60.00, 2),
    ('Physics for Beginners', 'Natural Science', 'Moderately Used', 30.00, 3),
    ('Calculus Volume 1', 'Math', 'Moderately Used', 35.00, 1),
    ('Artificial Intelligence Basics', 'Computer', 'Used Like New', 70.00, 2),
    ('Biology: A Comprehensive Guide', 'Natural Science', 'Heavily Used', 25.50, 3),
    ('Statistics for Engineers', 'Math', 'Used Like New', 55.00, 1),
    ('Networking Essentials', 'Computer', 'Moderately Used', 45.00, 2),
    ('Astronomy Simplified', 'Natural Science', 'Heavily Used', 40.00, 3),
    ('Probability and Statistics', 'Math', 'Moderately Used', 50.00, 1),
    ('Web Development Guide', 'Computer', 'Used Like New', 35.00, 2),
    ('Earth Science Basics', 'Natural Science', 'Moderately Used', 28.99, 3),
    ('Trigonometry Essentials', 'Math', 'Used Like New', 22.50, 1),
    ('Introduction to Machine Learning', 'Computer', 'Moderately Used', 65.00, 2);



INSERT INTO cart (user_id, book_id, quantity)
VALUES 
(2, 1, 1), -- buyer1 added Math for Beginners
(2, 2, 1), -- buyer1 added Advanced Computer Science
(3, 3, 2); -- buyer2 added Natural Science 101 (2 copies)


INSERT INTO transactions (user_id, book_id, total_price, date)
VALUES 
(2, 1, 25.00, '2024-11-17'), -- buyer1 purchased Math for Beginners
(3, 3, 30.00, '2024-11-17'), -- buyer2 purchased 2 copies of Natural Science 101
(2, 2, 30.00, '2024-11-17'); -- buyer1 purchased Advanced Computer Science

INSERT INTO users (username, password, role, email)
VALUES 
('admin', 'adminpass', 'Admin', 'admin@example.com'),
('buyer1', 'buyerpass1', 'Buyer', 'buyer1@example.com'),
('buyer2', 'buyerpass2', 'Buyer', 'buyer2@example.com'),
('seller1', 'sellerpass1', 'Seller', 'seller1@example.com'),
('seller2', 'sellerpass2', 'Seller', 'seller2@example.com');





