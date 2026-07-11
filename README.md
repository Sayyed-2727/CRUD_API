# CRUD API – Technical Test

REST API for managing a product catalog.

This project has been developed using:

- Java 17  
- Spring Boot 3.x  
- MySQL 8  
- Flyway for database migrations  
- Maven Wrapper  

The focus of the implementation is clean architecture, proper separation of concerns, validation, and structured error handling.

---

## Project Structure

```
com.Solution.CRUDAPI
 ├── application        Business logic (services)
 ├── domain             Entities and repositories
 ├── infrastructure     Exception handling
 └── interfaces
      ├── controller    REST controllers
      └── dto           API DTOs
```

Layers are clearly separated:

- Controller: HTTP layer only  
- Service: Business logic  
- Repository: Data access  
- Flyway: Database schema management  

---

## Requirements

- Java 17 installed and configured  
- Docker (recommended) or a local MySQL 8 installation  

To verify Java version:

```
java -version
```

It must display version 17.

---

## How to Run the Application

### 1. Start MySQL 8 with Docker Compose

The project includes a `compose.yaml` file to simplify the database startup.

From the project root, run:

```
docker compose up -d
```

To check that the container is running:

```
docker compose ps
```

To inspect the MySQL logs:

```
docker compose logs -f mysql
```

Wait until the following message appears:

```
ready for connections
```

Do not start the Spring Boot application before this message is shown, otherwise the database connection may fail.

---

### 2. Run the Spring Boot Application

From the project root directory:

On Windows (PowerShell):

```
.\mvnw.cmd spring-boot:run
```

On macOS/Linux:

```
./mvnw spring-boot:run
```

When the application starts successfully, it will be available at:

```
http://localhost:8080
```

Flyway will automatically execute the migration script:

```
V1__create_product_table.sql
```

---

## API Endpoints

Base URL:

```
http://localhost:8080/api/products
```

### Create Product

POST `/api/products`

Request body (JSON):

```json
{
  "name": "Laptop",
  "description": "Gaming laptop",
  "price": 1200,
  "stock": 10
}
```

Response: 201 Created

---

### Get All Products (with pagination)

GET:

```
/api/products?page=0&size=10
```

---

### Get Product by ID

GET:

```
/api/products/{id}
```

Returns 404 if the product does not exist.

---

### Update Product

PUT:

```
/api/products/{id}
```

Request body (JSON):

```json
{
  "name": "Laptop Pro",
  "description": "RTX Edition",
  "price": 1500,
  "stock": 8
}
```

---

### Delete Product

DELETE:

```
/api/products/{id}
```

Response: 204 No Content

---

## Validation and Error Handling

- Invalid input returns 400 Bad Request  
- Non-existing resources return 404 Not Found  
- Errors are returned in a structured JSON format  
- Validation is implemented using Bean Validation  
- Exceptions are handled centrally using @ControllerAdvice  

---

## Database Configuration

Database name: `crud_api`  
Username: `root`  
Password: `root`  
Port: `3307`  

The schema is managed exclusively through Flyway migrations. Hibernate is configured with `ddl-auto=validate`.

---

## Running Tests

To execute unit tests:

```
.\mvnw.cmd test
```

---

## Notes

If you need to reset the environment:

```
docker compose down
```

To remove the volume and start again from a clean database:

```
docker compose down -v
```

Then repeat the steps described above.

This project is intended to be executed locally and does not require any additional configuration beyond what is described in this document.
