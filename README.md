# Employee Leave Management System (ELMS)

ELMS is a full-stack, enterprise-grade Employee Leave Management System built using **Spring Boot 3.3.2 (Java 21 LTS)** and **Angular 17 (Standalone Components)** with **MySQL 8.4 LTS**.

---

## 🌟 Key Features & Capstone Requirements
- **JWT & Role-Based Security**: Secure authentication (`ROLE_HR_ADMIN`, `ROLE_MANAGER`, `ROLE_EMPLOYEE`) with BCrypt password hashing.
- **Dynamic Working Day Engine (Rule 1)**: Calculates net leave duration by excluding Saturdays, Sundays, and registered public holidays automatically.
- **Leave Overlap Prevention (Rule 2)**: Rejects overlapping leave applications for the same employee.
- **Quota & Balance Validation (Rule 3)**: Validates remaining leave balance prior to submission.
- **Manager Approval Workflow (Rule 6 & 7)**: Managers review pending team applications and approve or reject with comments, triggering automatic balance updates.
- **HR Administrative Control Panel**: HR Admins can create user accounts, assign line managers, override yearly quotas, configure leave categories, and register company public holidays.
- **Asynchronous Email Notifications**: Non-blocking `@Async` notifications sent upon leave submission, approval, or rejection.
- **Swagger / OpenAPI 3**: Interactive API documentation available at `http://localhost:8080/swagger-ui/index.html`.

---

## 🛠️ Technology Stack
- **Backend**: Java 21 LTS, Spring Boot 3.3.2, Spring Data JPA, Spring Security, JJWT (0.12.5), Lombok, Slf4j.
- **Frontend**: Angular 17 (Standalone Components, RxJS, TypeScript 5, Custom Glassmorphism UI Design System).
- **Database**: MySQL 8.4 LTS (`elms_db`).
- **Build Tools**: Maven (`mvnw.cmd`), Angular CLI (`@angular/cli@17`).

---

## 🚀 Quick Start & Run Instructions

### 1. Database Setup (MySQL 8.4)
Create local MySQL database `elms_db`:
```sql
CREATE DATABASE elms_db;
```
Configure database credentials in `backend/src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/elms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=12345
```

### 2. Backend Startup (Spring Boot)
```bash
cd backend
.\mvnw.cmd spring-boot:run
```
- API Base URL: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### 3. Frontend Startup (Angular 17)
```bash
cd frontend
npm start
```
- Client App URL: `http://localhost:4200`

---

## 🔑 Initial Baseline System Admin Account
Upon initial application startup, the system automatically initializes a clean baseline HR Admin account:
- **Email**: `admin@elms.com`
- **Password**: `admin123`
- **Role**: `HR_ADMIN`

*(Log in as HR Admin to create user accounts, assign managers, and configure custom leave policies step by step!)*

---

## 🧪 Running Automated Tests
- **Backend Unit & Integration Tests**:
  ```bash
  cd backend
  .\mvnw.cmd test
  ```
- **Frontend Angular Build Verification**:
  ```bash
  cd frontend
  npx ng build
  ```

---

## 📚 Project Documentation
- [`docs/learning-log.md`](file:///d:/ELMS/docs/learning-log.md): Commit-by-commit development and concept log.
- [`docs/architecture.md`](file:///d:/ELMS/docs/architecture.md): System architecture and tech stack details.
- [`docs/api.md`](file:///d:/ELMS/docs/api.md): Complete REST API contract specification.
- [`docs/database.md`](file:///d:/ELMS/docs/database.md): MySQL 8.4 schema dictionary and ERD.
- [`docs/business-rules.md`](file:///d:/ELMS/docs/business-rules.md): Core business rules & validation logic.
- [`docs/viva-preparation.md`](file:///d:/ELMS/docs/viva-preparation.md): Top 50 viva Q&As and live coding exercises.
- [`docs/capstone-project-report.md`](file:///d:/ELMS/docs/capstone-project-report.md): Final capstone project report.
