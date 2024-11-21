-- Create the database
CREATE DATABASE SunDevilBookSystem;
USE SunDevilBookSystem;

-- Create Users table
CREATE TABLE Users (
    userID INT AUTO_INCREMENT PRIMARY KEY,         -- Auto-increment primary key
    username NVARCHAR(50) UNIQUE NOT NULL,         -- Unique username
    password NVARCHAR(255) NOT NULL,               -- Hashed password
    email NVARCHAR(100) UNIQUE NOT NULL,           -- Unique email
    role NVARCHAR(20) NOT NULL                     -- Role: 'Buyer', 'Seller', 'Admin'
);

-- Create Buyers table
CREATE TABLE Buyers (
    buyerID INT AUTO_INCREMENT PRIMARY KEY,        -- Auto-increment primary key
    userID INT NOT NULL,                           -- References Users
    registrationDate DATETIME DEFAULT CURRENT_TIMESTAMP, -- Registration date
    FOREIGN KEY (userID) REFERENCES Users(userID)  -- Foreign key constraint
);

-- Create Sellers table
CREATE TABLE Sellers (
    sellerID INT AUTO_INCREMENT PRIMARY KEY,       -- Auto-increment primary key
    userID INT NOT NULL,                           -- References Users
    bookID INT NOT NULL,                           -- References Books
    listingDate DATETIME DEFAULT CURRENT_TIMESTAMP, -- Date listed
    FOREIGN KEY (userID) REFERENCES Users(userID), -- Foreign key to Users
    FOREIGN KEY (bookID) REFERENCES Books(bookID)  -- Foreign key to Books
);

-- Create Books table
CREATE TABLE Books (
    bookID INT AUTO_INCREMENT PRIMARY KEY,         -- Auto-increment primary key
    title NVARCHAR(100) NOT NULL,                  -- Book title
    subject NVARCHAR(50) NOT NULL,                 -- Book subject/category
    condition NVARCHAR(50) NOT NULL,               -- Book condition (e.g., 'New', 'Used')
    price DECIMAL(10, 2) NOT NULL,                 -- Price of the book
    forSale BIT NOT NULL DEFAULT 1                 -- 1 for sale, 0 not for sale
);

-- Create Transactions table
CREATE TABLE Transactions (
    transactionID INT AUTO_INCREMENT PRIMARY KEY,  -- Auto-increment primary key
    buyerID INT NOT NULL,                          -- References Buyers
    bookID INT NOT NULL,                           -- References Books
    sellerID INT NOT NULL,                         -- References Sellers
    price DECIMAL(10, 2) NOT NULL,                 -- Transaction price
    transactionDate DATETIME DEFAULT CURRENT_TIMESTAMP, -- Transaction date
    FOREIGN KEY (buyerID) REFERENCES Buyers(buyerID),   -- Foreign key to Buyers
    FOREIGN KEY (bookID) REFERENCES Books(bookID),      -- Foreign key to Books
    FOREIGN KEY (sellerID) REFERENCES Sellers(sellerID) -- Foreign key to Sellers
);
