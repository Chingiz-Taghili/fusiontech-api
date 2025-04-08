# FusionTech API
**FusionTech API** is a RESTful backend project built with Java and Spring Boot, designed for the sale of electronic products.
This project serves as the server side of an e-commerce system, encompassing key functionalities such as product management, 
categories and user authentication.

## Technologies and Tools Used
- **Java 17**
- **Spring Boot**
- **Spring Security** – JWT-based authentication and authorization
- **PostgreSQL** – database
- **Lombok** – reduces boilerplate code
- **ModelMapper** – entity-to-DTO mapping
- **Swagger/OpenAPI** – API documentation
- **Validation (Jakarta)** – input validation
- **Global Exception Handling** – centralized error management
- **DTO and Custom Response Payloads** – secure and clean data transmission

## Key Features
- User registration and login (JWT token-based)
- Role-based access control (Admin, User, etc.)
- Product CRUD operations
- Category and subcategory management
- Image upload and preview functionality (image storage)
- Swagger UI for testing the API
- Use of DTOs for clean and secure data transfer

## Installation and Setup
> Prerequisites:
> - Java 17+
> - Maven 3.9+
> - PostgreSQL database

1. Clone the repository:
   ```bash
   git clone https://github.com/Chingiz-Taghili/fusiontech-api.git
2. Configure your PostgreSQL settings in application.yml or application.properties:
   spring:
  datasource:
   url: jdbc:postgresql://localhost:5432/fusiontech
   username: your_db_username
   password: your_db_password
3. Run the project via terminal:
   ./mvnw spring-boot:run

## Auth Testing
Default endpoints:
POST /api/auth/register – Register a new user
POST /api/auth/login – Login (retrieve JWT token)
After receiving the token, you can use it to access other protected endpoints (include the token in the Authorization header as Bearer <token>).

## API Documentation
You can test the API through Swagger UI: http://localhost:8080/swagger-ui/index.html

## Author
Chingiz Taghili
📧 chingiz.taghili@gmail.com
🔗 GitHub: @Chingiz-Taghili

## Note
This project is developed for portfolio purposes and demonstrates essential backend principles that can be applied in a real-world work environment.
