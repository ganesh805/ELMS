# Vista Tech — Employee Leave Management System (ELMS)

**Vista Tech ELMS** is an enterprise-grade full-stack Employee Leave Management System built with **Spring Boot 3.3.2 (Java 21 LTS)** and **Angular 17 (Standalone Components)** with **MySQL 8.4 LTS**.

---

## 🌟 Key System Features & Business Rules
- **Vista Tech Corporate Branding**: Custom modern dark glassmorphism design system integrated with official Vista Tech branding.
- **Dynamic Working Day Engine (Rule 1)**: Calculates net requested leave duration by automatically excluding Saturdays, Sundays, and registered company public holidays.
- **Leave Overlap Prevention (Rule 2)**: Prevents double-booking leave dates for the same employee.
- **Quota & Balance Enforcement (Rule 3)**: Enforces remaining leave balance constraints before request submission.
- **Manager Approval Workflow (Rule 6 & 7)**: Managers review pending team requests, submit feedback comments, and trigger automatic balance deductions upon approval.
- **HR Admin Control Panel**: HR Administrators manage user accounts, assign line managers, override leave quotas, configure leave categories (with soft delete), and register company holidays.
- **Asynchronous Email Notifications**: Non-blocking `@Async` notifications sent upon leave submission, approval, or rejection.
- **Spring Security & JWT Authentication**: Stateless authentication with HMAC-SHA384 JWT tokens and BCrypt password encryption (*PDF §15 Headline Bonus Achieved*).
- **Interactive API Documentation**: Swagger UI specs accessible at `http://localhost:8080/swagger-ui/index.html`.

---

## 🛠️ Technology Stack & Versions
- **Backend**: Java 21 LTS, Spring Boot 3.3.2, Spring Data JPA, Spring Security, JJWT 0.12.5, Lombok, Slf4j.
- **Frontend**: Angular 17 (Standalone Components, RxJS, TypeScript 5, Custom Glassmorphism UI Design System).
- **Database**: MySQL 8.4 LTS (`elms_db`).
- **Build Tools**: Apache Maven (`mvnw.cmd`), Angular CLI (`@angular/cli@17`).

---

## 🚀 Execution & Run Commands

### 1. Database Configuration (MySQL 8.4)
Create local database:
```sql
CREATE DATABASE elms_db;
```
Database credentials in `backend/src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/elms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=12345
```

### 2. Run Backend (Spring Boot 3.3.2)
Navigate to `backend` directory and run:
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```
- **REST API Base URL**: `http://localhost:8080/api`
- **Swagger UI Specification**: `http://localhost:8080/swagger-ui/index.html`

### 3. Run Frontend (Angular 17 SPA)
In a separate terminal, navigate to `frontend` directory and run:
```powershell
cd frontend
npm start
```
- **Application URL**: `http://localhost:4200`

---

## 🔑 Default Initial Baseline Login
Upon first startup, the database seeds 1 single baseline HR Admin account:
- **Email**: `admin@elms.com`
- **Password**: `admin123`
- **Role**: `HR_ADMIN`

*(Log in as HR Admin to create employee/manager accounts and assign line managers via the `/admin/users` screen).*

---

## 🔒 Note on Access-Control Simplification & Security Architecture (PDF §4 & §15)
In accordance with capstone requirements:
- The system supports both **Header-Based Authorization (`X-User-Id`)** for lightweight role/ownership checks and **Spring Security JWT Authentication** with BCrypt password hashing (*Headline Bonus Goal*).
- Backend service layers enforce strict ownership and reporting-line checks so managers cannot approve requests outside their direct report hierarchy, and employees cannot modify another user's leave applications.

---

## 🧪 Automated Testing Commands
- **Backend Unit & Integration Tests**:
  ```powershell
  cd backend
  .\mvnw.cmd test
  ```
- **Frontend Production Build**:
  ```powershell
  cd frontend
  npx ng build
  ```

---

## 📚 Complete Documentation Suite
- [`docs/learning-log.md`](file:///d:/ELMS/docs/learning-log.md): Commit-by-commit development and concept log across all 30 commits.
- [`docs/viva-preparation.md`](file:///d:/ELMS/docs/viva-preparation.md): 50 Top Viva Q&As and live coding refactoring tasks.
- [`docs/architecture.md`](file:///d:/ELMS/docs/architecture.md): System architecture, technology stack, and layered design.
- [`docs/api.md`](file:///d:/ELMS/docs/api.md): Complete REST API contract specification.
- [`docs/database.md`](file:///d:/ELMS/docs/database.md): MySQL 8.4 schema dictionary and ERD diagram.
- [`docs/business-rules.md`](file:///d:/ELMS/docs/business-rules.md): Core business rules & validation logic.
- [`docs/capstone-project-report.md`](file:///d:/ELMS/docs/capstone-project-report.md): Final capstone project completion report.
