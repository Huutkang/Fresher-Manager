
# Fresher Management Application

## Introduction

This is a Spring Boot application designed for managing fresher employees. It allows users to perform operations such as viewing, adding, updating, and deleting freshers, as well as managing centers and projects that freshers are involved in.

## Features

### Fresher Management:
- View the total number of freshers.
- Add new freshers.
- Delete old freshers.
- Update fresher details.
- Calculate fresher scores based on 3 major assignments, with the final score as the average of the three.
- Search for freshers by name, programming language, or email.

### Center Management:
- View all centers.
- Create new centers.
- Delete centers.
- Update center details.
- Assign freshers to centers.

### Dashboard:
- View statistics of the number of freshers in each center.
- View statistics of freshers by their scores.

### Project Management:
- View project details (ID, name, center, manager, start date, end date, language, status).
- Add, edit, or delete projects.
- Assign or remove freshers from projects, with email notifications for each action.
- Allow freshers to search for projects they are involved in.

## Technologies Used
- **Java 17**
- **Spring Boot**
- **Spring JPA**
- **PostgreSQL / MySQL**
- **Swagger** for API documentation
- **Postman** for API testing
- **Token Authentication (Bearer)**
- **Controller Advice** for exception handling
- **Log4J2** for logging

## Prerequisites

1. **Java 17**
2. **Maven**
3. **PostgreSQL** or **MySQL**
4. **Postman** (optional for testing)

## How to Run

1. Clone the repository:

   ```bash
   git clone https://github.com/your-repo/fresher-management.git
   ```

2. Navigate to the project directory:

   ```bash
   cd fresher-management
   ```

3. Update the database configuration in `application.properties` to match your PostgreSQL/MySQL settings:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/fresher_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password
   ```

4. Build the project:

   ```bash
   mvn clean install
   ```

5. Run the application:

   ```bash
   mvn spring-boot:run
   ```

6. Open a browser and navigate to:

   ```
   http://localhost:8081
   ```

   Use the following credentials to log in:
   - **Username**: `admin`
   - **Password**: `123456`

## API Documentation

You can access the Swagger API documentation at:

```
http://localhost:8081/swagger-ui/index.html
```

## Testing with Postman

You can use Postman to test the API endpoints. Here are some examples of endpoints you can use:

- **Get Fresher by ID**: `GET /users/{id}`
- **Update Fresher**: `PUT /users/{id}`
- **Delete Fresher**: `DELETE /users/{id}`
- **Create Center**: `POST /centers`
- **Assign Fresher to Center**: `PUT /centers/{centerId}/add-fresher/{fresherId}`

For authorization, use the Bearer Token from your login response.

## License

This project is licensed under the MIT License.
