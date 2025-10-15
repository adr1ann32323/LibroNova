# README.md

## Portal de Biblioteca — Aplicación de Escritorio en Java (JDBC + JOptionPane)

Proyecto de gestión: usuarios, libros, préstamos, exportes CSV, logs y pruebas.  

---

### 1. Descripción general

Esta aplicación es un sistema de gestión de biblioteca de escritorio, desarrollado en Java.  
Sigue una arquitectura por capas (DAO → Service → Controller → View) e implementa:

- Gestión de usuarios (registro, inicio de sesión, roles, activar/desactivar).
- Catálogo de libros (CRUD, ISBN único, gestión de stock).
- Gestión de préstamos (registro de préstamo, devolución, transacciones, multas, detección de vencimientos).
- Exportación CSV (catálogo y préstamos vencidos).
- Logs en `app.log`.
- Interfaz gráfica simple usando diálogos `JOptionPane` y tablas de texto.
- Pruebas unitarias (JUnit 5) para reglas de negocio principales.

**Intención:** Enseñanza y despliegues pequeños; el código es modular, por lo que puedes reemplazar vistas, DAOs o cambiar la persistencia.

---

### 2. Información del desarrollador 

- **Nombre:** Adrian Alesis Arboleda  
- **Equipo / Clan:** Berners Lee 
- **Correo:** adr1ann32323@gmail.com  
- **Documento / ID:** 1026160655  

---

### 3. Prerrequisitos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- **Java JDK 17 (o 11+):** Verifica con:
    ```bash
    java -version
    ```

- **Maven 3.6+:** Verifica con:
    ```bash
    mvn -v
    ```

- **Servidor MySQL (local) o un proveedor remoto compatible con MySQL**  
    (Clever Cloud).

- **Acceso a internet** para descargar dependencias de Maven (o añade los JARs necesarios manualmente).

- **Opcional:** Cliente MySQL o MySQL Workbench para ejecutar scripts SQL.

---

### 4. Estructura del proyecto

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

### 5. Configuración

#### 5.1 Dependencias de Maven (`pom.xml`)

Asegúrate de incluir al menos:

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

Coloca este archivo en `src/main/resources/config/application.properties`. Ejemplo:

**MySQL local**
```properties
db.url=jdbc:mysql://localhost:3306/library_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=tu_contraseña_local

# Configuración de negocio
diasPrestamo=7
multaPorDia=1500
```

**Clever Cloud (ejemplo)**  
Asegúrate de usar el host/base de datos correctos proporcionados por Clever Cloud:
```properties
db.url=jdbc:mysql://<host>:3306/<database>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=TU_USUARIO_CLEVER
db.password=TU_CONTRASEÑA_CLEVER

diasPrestamo=7
multaPorDia=1500
```

#### 5.3 `DBconfig.java`

El método `DBconfig.connect()` debe leer el archivo de propiedades y usar `DriverManager.getConnection(url, user, pass)`. Opcionalmente, incluye `Class.forName("com.mysql.cj.jdbc.Driver");` antes de conectar.

---

### 6. Configuración de la base de datos

#### 6.1 Scripts SQL incluidos

- `sql/library_Test.sql` — esquema inicial con tablas `user_account`, `book`, `loan`.

Ejecuta el script SQL con un cliente MySQL, Workbench o Clever Cloud:

```bash
# Local
mysql -u root -p < sql/library_Test.sql
```

```bash
# Local
mysql -u root -p < sql/library_test.sql
```

Archivos incluidos:

- `sql/library_test.sql` — esquema actualizado con tablas `user_account`, `book`, `loan`.


---

### 7. Ejecución de la aplicación

#### 7.1 Construir con Maven
```bash
mvn clean package
```

Esto compilará y producirá un JAR (si tu `pom.xml` está configurado para empaquetar un fat JAR).  
De lo contrario, ejecuta desde el IDE.

#### 7.2 Ejecutar desde el IDE

- Importa el proyecto en tu IDE (IntelliJ/Eclipse).
- Asegúrate de que `src/main/resources` esté en el classpath.
- Ejecuta `com.LibroNova.app.Main`.

#### 7.3 Ejecutar desde línea de comandos
```bash
mvn exec:java -Dexec.mainClass="com.LibroNova.app.Main"
```
---

### 8. Pruebas

Ejecuta las pruebas unitarias:
```bash
mvn test
```

Pruebas recomendadas:

- `LoanServiceTest` — cálculo de multas y validación de stock.
- `BookServiceTest` — validación de unicidad de ISBN.

---

### 9. Logging

La aplicación escribe logs en `app.log` en la raíz del proyecto (usando `FileWriter` o `java.util.logging`).  
Los errores se registran con marcas de tiempo y mensajes.

---

### 10. Exportación CSV / Reportes

- `CsvExporter.exportBooks(List<Book>)` → `libros_export.csv`
- `CsvExporter.exportLateLoans(List<Loan>)` → `prestamos_vencidos.csv`

Los archivos se escriben en el directorio de trabajo (raíz del proyecto). Puedes cambiar la ruta en `CsvExporter`.

---

### 11. GUI / Uso de JOptionPane

- `MainView` muestra diálogos de inicio de sesión y redirige a `MemberView` o `StaffView`.
- `StaffView` contiene submenús para libros, usuarios, préstamos, exportes.
- `BookView`, `UserView`, `LoanView` usan `TableFormatter` para presentar tablas en `JOptionPane` (o un `JTextArea` dentro de un `JScrollPane` para salidas grandes).

---

### 12. Diagramas

Añade tus diagramas (imágenes) en `docs/diagrams/` e inclúyelos aquí.

#### 12.1 Diagrama de Clases (UML)

**Descripción:** Principales clases y relaciones: `User`, `Book`, `Loan`, `IUserDao/LoanDao/BookDao`, `UserService/BookService/LoanService`, `UserController/BookController/LoanController`, Vistas y `DBconfig`.

Inserta la imagen en `docs/diagrams/class-diagram.png`.

#### 12.2 Diagrama de Casos de Uso

**Actores:** Member, Assistant, Admin.  
**Casos de uso:** Iniciar sesión, Registrar libro, Prestar libro, Devolver libro, Reportar incidente (opcional), Exportar CSV.

Inserta la imagen en `docs/diagrams/usecase-diagram.png`.

---

### 13. Notas de implementación y detalles

#### 13.1 Transacciones

Operaciones críticas (`LoanService.registerLoan` y `LoanService.returnBook`) usan transacciones manuales:

```java
conn.setAutoCommit(false);
```

#### 13.2 Reglas de negocio

- **ISBN único:** `bookService.createBook()` verifica `bookDao.findByIsbn()` antes de insertar.
- **Validación de stock:** `available_copies` debe ser >= 0 antes de prestar.
- **Usuario activo:** Solo usuarios activos pueden prestar.
- **Multas:** Calculadas automáticamente si `return_date > due_date`.

#### 13.3 Excepciones personalizadas

- `BadRequestException` (400)
- `UnauthorizedException` (401)
- `NotFoundException` (404)
- `ConflictException` (409)
- `ServiceException` (500)
- `DataAccessException` (checked) — lanzada por DAOs para problemas SQL.

---

### 14. Configuración Clever Cloud (DB remota)

1. Crea un add-on MySQL en Clever Cloud y obtén credenciales (`mysql://user:pass@host:3306/db`).
2. Convierte la URL a JDBC:
     ```properties
     jdbc:mysql://<host>:3306/<db>?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
     ```
3. Actualiza `application.properties` con `db.url`, `db.user`, `db.password`.

---

### 15. Solución de problemas / FAQ

- **"No se puede conectar a la DB":** Verifica el prefijo JDBC (`jdbc:mysql://`), flags `allowPublicKeyRetrieval` y `useSSL`, credenciales.
- **"ISBN duplicado":** Asegúrate de que `BookService` verifique `findByIsbn` primero.
- **"Rollback no funciona":** Asegúrate de que el servicio pase la misma conexión a los métodos DAO.

---

### 16. Dónde completar tus recursos (placeholders)

#### 16.1 Consultas SQL de ejemplo

```sql

--   EXAMPLE QUERIES

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

### 17. Comandos de ejemplo

- Ejecutar script DB local:
    ```bash
    mysql -u root -p < sql/library_portal_updated.sql
    ```

- Construir:
    ```bash
    mvn clean package
    ```

- Ejecutar (IDE):  
    Ejecuta la clase principal `com.LibroNova.app.Main`.

- Ejecutar pruebas:
    ```bash
    mvn test
    ```

---

```

**Ejemplo `DBconfig.connect()`:**
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
                throw new DataAccessException("Error leyendo configuración", e);
        } catch (SQLException | ClassNotFoundException e) {
                throw new DataAccessException("Error de conexión a la DB", e);
        }
}
```
🖼️ **Captura del diagrama de clases:**
![Diagrama de Clases](./docs/diagrams/class.png)
---
🖼️ **Ejemplo de funcionamiento prestamos:**
![Ejemplo de clases](./docs/img/loans.png)

🖼️ **Ejemplo de funcionamiento usuarios:**
![Ejemplo de usuarios](./docs/img/users.png)

🖼️ **Ejemplo de funcionamiento vista de admin:**
![Ejemplo de admin](./docs/img/viewStaff.png)
### Notas finales

Este README proporciona la configuración completa para ejecutar, probar y extender la aplicación.
Se recomienda revisar el código fuente para entender la lógica de negocio y la arquitectura en capas.  


