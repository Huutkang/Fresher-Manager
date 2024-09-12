
# Fresher Manager Application

This project is a **Spring Boot** application designed for managing **Fresher** trainees. The app allows users to create, update, and manage freshers, centers, and projects. It also provides tools for tracking fresher performance and statistics, as well as email notifications for project assignments.

## Technologies Used
- **Java 17**
- **Spring Boot** (including JPA, Spring Security, Spring Mail)
- **PostgreSQL** (as the database)
- **Swagger** (for API documentation)
- **Log4J2** (for logging)

## Features
- **Fresher Management**:
  - View all freshers.
  - Add, update, and delete freshers.
  - Track fresher performance across three large exercises (calculate average scores).
  - Search for freshers by name, programming language, or email.
- **Center Management**:
  - View, add, update, and delete centers.
  - Assign freshers to centers.
- **Project Management**:
  - View, add, update, and delete projects.
  - Manage project details (code, name, center, manager, start/end date, language, status).
  - Assign or remove freshers from projects (with email notifications).
  - Fresher can search for their projects.
- **Statistics**:
  - View fresher statistics by center and score.
  - Statistics on fresher performance based on grades.
- **Authentication**:
  - Token-based authentication (Bearer tokens).
- **Dashboard**: 
  - Overview of fresher counts and performance.

## Installation
1. Clone the repository from GitHub:
   ```
   git clone https://github.com/Huutkang/Fresher-Manager.git
   ```
2. Navigate to the project directory and set up the environment.

3. Update the PostgreSQL database configuration in `application.properties`.

4. Run the project:
   ```
   mvn spring-boot:run
   ```

## Testing the Application
- To access the API documentation, navigate to `http://localhost:8081/swagger-ui/index.html`.
- For testing, navigate to `http://localhost:8081/`.
- Use the following credentials:
  - **Username**: `admin`
  - **Password**: `123456`
  
You can test the APIs using tools like Postman.

## Configuration Notes
- Replace the email addresses in the email sending module with your own for testing purposes.
- This project is configured to use **PostgreSQL**. Make sure your database is set up and the credentials in the project are correctly configured.
