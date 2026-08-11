# Employee Leave Management System (ELMS)

The **Employee Leave Management System (ELMS)** is an enterprise full-stack web application designed for managing employee leave requests, balance tracking, manager approval workflows, and HR administrative controls.

## Technology Stack

- **Backend**: Java 21 LTS, Spring Boot 3.3.2, Spring Data JPA, Hibernate, Bean Validation, MySQL 8.4 LTS, springdoc-openapi (Swagger UI)
- **Frontend**: Angular 17+, TypeScript, Angular Material, RxJS, Reactive Forms
- **Database**: MySQL 8.4 LTS

## Project Structure

```
ELMS/
├── backend/            # Spring Boot REST API Application
│   ├── pom.xml
│   └── src/
├── frontend/           # Angular 17+ Application (Commit 18 onwards)
├── docs/               # Architecture, API & Viva Documentation
└── .gitignore
```

## Quick Start (Backend)

1. Create a MySQL database named `elms_db`.
2. Copy `backend/src/main/resources/application-local.example.properties` to `backend/src/main/resources/application-local.properties`.
3. Set your local database password in `application-local.properties`:
   ```properties
   spring.datasource.password=YOUR_LOCAL_PASSWORD
   ```
4. Build and run the Spring Boot application:
   ```bash
   cd backend
   mvn spring-boot:run
   ```

## Development Roadmap & Git History

Development follows a 33-step incremental commit roadmap documented in `docs/learning-log.md`.
