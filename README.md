# README.md

## Library Portal — Desktop Application in Java (JDBC + JOptionPane)

Management project: users, books, loans, CSV exports, logs, and tests.  

---

### 1. General Description

This application is a desktop library management system developed in Java.  
It follows a layered architecture (DAO → Service → Controller → View) and implements:

- User management (registration, login, roles, activate/deactivate).
- Book catalog (CRUD, unique ISBN, stock management).
- Loan management (loan registration, returns, transactions, fines, overdue detection).
- CSV export (catalog and overdue loans).
- Logs in `app.log`.
- Simple graphical interface using `JOptionPane` dialogs and text tables.
- Unit tests (JUnit 5) for main business rules.

**Purpose:** Teaching and small deployments; the code is modular, so you can replace views, DAOs, or change persistence.

---

### 2. Developer Information 

- **Name:** Adrian Alesis Arboleda  
- **Team / Clan:** Berners Lee 
- **Email:** adr1ann32323@gmail.com  
- **Document / ID:** 1026160655  

---

### 3. Prerequisites

Before running the project, make sure you have installed:

- **Java JDK 17 (or 11+):** Verify with:
    ```bash
    java -version
    ```

- **Maven 3.6+:** Verify with:
    ```bash
    mvn -v
    ```

- **MySQL Server (local) or a remote provider compatible with MySQL**  
    (Clever Cloud).

- **Internet access** to download Maven dependencies (or manually add the required JARs).

- **Optional:** MySQL client or MySQL Workbench to execute SQL scripts.

---

### 4. Project Structure

```plaintext
src/
 ├── main/
 │   ├── java/com/yourcompany/app/
 │   │   ├── config/DBconfig.java
 │   │   ├── controller/*.java
 │   │   ├── dao/*.java
 │   │   ├── dao/jdbc/*.java
 │   │   ├── domain/*.java
 │   │   ├── errors/*.java
 │   │   ├── service/*.java
 │   │   ├── service/decorator/*.java
 │   │   ├── util/*.java       
 │   │   └── view/*.java      
 │   └── resources/
 │       └── config/application.properties
 └── test/
         └── java/... (JUnit 5 tests)
```

---

### 5. Configuration

#### 5.1 Maven Dependencies (`pom.xml`)

Make sure to include at least:

```xml
<!-- MySQL driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- JUnit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>
```

#### 5.2 `application.properties` (`resources/config/application.properties`)

Place this file in `src/main/resources/config/application.properties`. Example:

**Local MySQL**
```properties
db.url=jdbc:mysql://localhost:3306/library_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=your_local_password

# Business configuration
loanDays=7
finePerDay=1500
```

**Clever Cloud (example)**  
Make sure to use the correct host/database provided by Clever Cloud:
```properties
db.url=jdbc:mysql://<host>:3306/<database>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=YOUR_CLEVER_USER
db.password=YOUR_CLEVER_PASSWORD

loanDays=7
finePerDay=1500
```

#### 5.3 `DBconfig.java`

The `DBconfig.connect()` method should read the properties file and use `DriverManager.getConnection(url, user, pass)`. Optionally, include `Class.forName("com.mysql.cj.jdbc.Driver");` before connecting.

---

### 6. Database Configuration

#### 6.1 Included SQL Scripts

- `sql/library_Test.sql` — initial schema with `user_account`, `book`, `loan` tables.

Run the SQL script with a MySQL client, Workbench, or Clever Cloud:

```bash
# Local
mysql -u root -p < sql/library_Test.sql
```

Included files:

- `sql/library_test.sql` — updated schema with `user_account`, `book`, `loan` tables.

---

### 7. Running the Application

#### 7.1 Build with Maven
```bash
mvn clean package
```

This will compile and produce a JAR (if your `pom.xml` is configured to package a fat JAR).  
Otherwise, run it from the IDE.

#### 7.2 Run from IDE

- Import the project into your IDE (IntelliJ/Eclipse).
- Ensure `src/main/resources` is in the classpath.
- Run `com.LibroNova.app.Main`.

#### 7.3 Run from Command Line
```bash
mvn exec:java -Dexec.mainClass="com.LibroNova.app.Main"
```
---

### 8. Testing

Run unit tests:
```bash
mvn test
```

Recommended tests:

- `LoanServiceTest` — fine calculation and stock validation.
- `BookServiceTest` — ISBN uniqueness validation.

---

### 9. Logging

The application writes logs to `app.log` in the project root (using `FileWriter` or `java.util.logging`).  
Errors are logged with timestamps and messages.

---

### 10. CSV Export / Reports

- `CsvExporter.exportBooks(List<Book>)` → `books_export.csv`
- `CsvExporter.exportLateLoans(List<Loan>)` → `overdue_loans.csv`

Files are written to the working directory (project root). You can change the path in `CsvExporter`.

---

### 11. GUI / Using JOptionPane

- `MainView` displays login dialogs and redirects to `MemberView` or `StaffView`.
- `StaffView` contains submenus for books, users, loans, exports.
- `BookView`, `UserView`, `LoanView` use `TableFormatter` to present tables in `JOptionPane` (or a `JTextArea` inside a `JScrollPane` for large outputs).

---

### 12. Diagrams

Add your diagrams (images) in `docs/diagrams/` and include them here.

#### 12.1 Class Diagram (UML)

**Description:** Main classes and relationships: `User`, `Book`, `Loan`, `IUserDao/LoanDao/BookDao`, `UserService/BookService/LoanService`, `UserController/BookController/LoanController`, Views, and `DBconfig`.

Insert the image in `docs/diagrams/class-diagram.png`.

#### 12.2 Use Case Diagram

**Actors:** Member, Assistant, Admin.  
**Use cases:** Log in, Register book, Loan book, Return book, Report incident (optional), Export CSV.

Insert the image in `docs/diagrams/usecase-diagram.png`.

---

### 13. Implementation Notes and Details

#### 13.1 Transactions

Critical operations (`LoanService.registerLoan` and `LoanService.returnBook`) use manual transactions:

```java
conn.setAutoCommit(false);
```

#### 13.2 Business Rules

- **Unique ISBN:** `bookService.createBook()` checks `bookDao.findByIsbn()` before inserting.
- **Stock validation:** `available_copies` must be >= 0 before loaning.
- **Active user:** Only active users can loan.
- **Fines:** Automatically calculated if `return_date > due_date`.

#### 13.3 Custom Exceptions

- `BadRequestException` (400)
- `UnauthorizedException` (401)
- `NotFoundException` (404)
- `ConflictException` (409)
- `ServiceException` (500)
- `DataAccessException` (checked) — thrown by DAOs for SQL issues.

---

### 14. Clever Cloud Configuration (Remote DB)

1. Create a MySQL add-on in Clever Cloud and get credentials (`mysql://user:pass@host:3306/db`).
2. Convert the URL to JDBC:
     ```properties
     jdbc:mysql://<host>:3306/<db>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
     ```
3. Update `application.properties` with `db.url`, `db.user`, `db.password`.

---

### 15. Troubleshooting / FAQ

- **"Cannot connect to DB":** Check the JDBC prefix (`jdbc:mysql://`), flags `allowPublicKeyRetrieval` and `useSSL`, credentials.
- **"Duplicate ISBN":** Ensure `BookService` checks `findByIsbn` first.
- **"Rollback not working":** Ensure the service passes the same connection to DAO methods.

---

### 16. Where to Complete Your Resources (Placeholders)

#### 16.1 Example SQL Queries

```sql
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
SELECT * FROM book WHERE author LIKE '%maria%';
```
---

### 17. Example Commands

- Run local DB script:
    ```bash
    mysql -u root -p < sql/library_portal_updated.sql
    ```

- Build:
    ```bash
    mvn clean package
    ```

- Run (IDE):  
    Run the main class `com.LibroNova.app.Main`.

- Run tests:
    ```bash
    mvn test
    ```

---

```

**Example `DBconfig.connect()`:**
```java
public static Connection connect() throws DataAccessException {
        try (InputStream input = DBconfig.class.getClassLoader().getResourceAsStream("config/application.properties")) {
                Properties props = new Properties();
                props.load(input);
                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String pass = props.getProperty("db.password");
                return DriverManager.getConnection(url, user, pass);
        } catch (IOException e) {
                throw new DataAccessException("Error reading configuration", e);
        } catch (SQLException | ClassNotFoundException e) {
                throw new DataAccessException("Database connection error", e);
        }
}
```
🖼️ **Class Diagram Screenshot:**
![Class Diagram](./docs/diagrams/class.png)
---
🖼️ **Loan Functionality Example:**
![Loan Example](./docs/img/loans.png)

🖼️ **User Functionality Example:**
![User Example](./docs/img/users.png)

🖼️ **Admin View Example:**
![Admin Example](./docs/img/viewStaff.png)
### Final Notes

This README provides the complete setup to run, test, and extend the application.
It is recommended to review the source code to understand the business logic and layered architecture.  
