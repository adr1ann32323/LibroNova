# LibroNova
Java

Script sql de la base de datos:
-- ========================
--   DATABASE CREATION
-- ========================
CREATE DATABASE bygwy9bvcp9kjci9cvan;
USE bygwy9bvcp9kjci9cvan;

-- ========================
--   USER ACCOUNT TABLE
-- ========================
CREATE TABLE user_account (
    id_user INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN','ASSISTANT','MEMBER') NOT NULL DEFAULT 'ASSISTANT',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO user_account (name, email, password, role, active)
VALUES
('System Administrator', 'admin@library.com', 'admin123', 'ADMIN', TRUE),
('Adrian Arboleda', 'adrian@gmail.com', 'adrian123', 'ASSISTANT', TRUE),
('Alesis Calle', 'alesis@hotmail.com', 'alesis123', 'MEMBER', TRUE),
('Maria', 'maria@gmail.com.com', 'maria123', 'MEMBER', FALSE);

-- ========================
--   BOOK TABLE
-- ========================
CREATE TABLE book (
    id_book INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(200),
    category VARCHAR(100),
    publisher VARCHAR(150),
    year INT,
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    reference_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO book (isbn, title, author, category, publisher, year, total_copies, available_copies, reference_price, is_active)
VALUES
('9780134685991', 'Effective Java', 'Joshua Bloch', 'Programming', 'Addison-Wesley', 2018, 5, 5, 55.00, TRUE),
('9781492056270', 'Java: The Complete Reference', 'Herbert Schildt', 'Programming', 'McGraw-Hill', 2020, 3, 3, 65.00, TRUE),
('9780321356680', 'Clean Code', 'Robert C. Martin', 'Software Engineering', 'Prentice Hall', 2008, 4, 4, 45.00, TRUE),
('9780596009205', 'Head First Design Patterns', 'Eric Freeman', 'Design', 'O’Reilly Media', 2004, 2, 2, 40.00, TRUE);

-- ========================
--   LOAN TABLE
-- ========================
CREATE TABLE loan (
    id_loan INT AUTO_INCREMENT PRIMARY KEY,
    id_user INT NOT NULL,
    id_book INT NOT NULL,
    loan_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    status ENUM('ACTIVE','RETURNED','LATE','CANCELLED') DEFAULT 'ACTIVE',
    fine DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_user) REFERENCES user_account(id_user) ON DELETE RESTRICT,
    FOREIGN KEY (id_book) REFERENCES book(id_book) ON DELETE RESTRICT
);

-- Sample data for loans
INSERT INTO loan (id_user, id_book, loan_date, due_date)
VALUES
(3, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY)),
(3, 2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 10 DAY));



-- ========================
--   EXAMPLE QUERIES
-- ========================

-- View all users
SELECT * FROM user_account;

-- View all books
SELECT * FROM book;

-- View all loans with user and book details
SELECT l.id_loan, l.loan_date, l.due_date, l.status, l.fine,
       u.name AS user_name, b.title AS book_title
FROM loan l
JOIN user_account u ON l.id_user = u.id_user
JOIN book b ON l.id_book = b.id_book;

-- Filter books by category
SELECT * FROM book WHERE category = 'Programming';

-- Filter books by author
SELECT * FROM book WHERE author LIKE '%Martin%';

