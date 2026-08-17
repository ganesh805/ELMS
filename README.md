# Vista Tech — Employee Leave Management System (ELMS)

**Vista Tech ELMS** is a full-stack, enterprise-grade Employee Leave Management System built using **Spring Boot 3.3.2 (Java 21 LTS)** and **Angular 17 (Standalone Components)** with **MySQL 8.4 LTS**.

---

## Application Screenshots & Visual Showcase

### 1. Sign-In Screen
<img width="1913" height="964" alt="Screenshot 2026-08-17 090618" src="https://github.com/user-attachments/assets/82d21268-7c13-42e3-b952-2a2c6f6e952b" />


### 2. HR Admin Control Center & Quota Overview
<img width="1914" height="966" alt="Screenshot 2026-08-17 090644" src="https://github.com/user-attachments/assets/5aeb9cff-8af7-44be-9ada-fcf48b5623df" />


### 3. Manager Dashboard
<img width="1919" height="961" alt="Screenshot 2026-08-17 090715" src="https://github.com/user-attachments/assets/329cad9e-36ca-4710-96d5-23cdf6c36124" />


### 4. Interactive Working Day Leave Application Form
<img width="1914" height="952" alt="Screenshot 2026-08-17 090735" src="https://github.com/user-attachments/assets/6793b3d4-5a9c-4b9d-ace3-2c00329a46d5" />


---

##  Live Production URLs

| Component | Platform | URL / Location |
| :--- | :--- | :--- |
| **Live Web App (Frontend)** | Vercel | **[https://elms-snowy.vercel.app](https://elms-snowy.vercel.app)** |
| **Live REST API (Backend)** | Render | **[https://elms-mhk7.onrender.com/api](https://elms-mhk7.onrender.com/api)** |
| **Cloud MySQL 8.4 Database** | Aiven | `defaultdb` |
| **Source Code Repository** | GitHub | **[https://github.com/ganesh805/ELMS.git](https://github.com/ganesh805/ELMS.git)** |

---

##  Database Artifacts
- **MySQL 8.4 Schema Script**: [https://github.com/ganesh805/ELMS/blob/main/database/schema.sql](https://github.com/ganesh805/ELMS/blob/main/database/schema.sql)
- **MySQL 8.4 Baseline Seed Script**: [https://github.com/ganesh805/ELMS/blob/main/database/seed.sql](https://github.com/ganesh805/ELMS/blob/main/database/seed.sql)
- **Documented Startup Seeder**: [`DataInitializer.java`](file:///d:/ELMS/backend/src/main/java/com/elms/config/DataInitializer.java)

---

##  API Documentation
- **Interactive Swagger UI**: [https://elms-mhk7.onrender.com/swagger-ui/index.html](https://elms-mhk7.onrender.com/swagger-ui/index.html) *(Redirect: [https://elms-mhk7.onrender.com/docs](https://elms-mhk7.onrender.com/docs))*
- **Direct OpenAPI 3 JSON**: [https://elms-mhk7.onrender.com/v3/api-docs](https://elms-mhk7.onrender.com/v3/api-docs)
- **API Contract Specification**: [https://github.com/ganesh805/ELMS/blob/main/docs/api.md](https://github.com/ganesh805/ELMS/blob/main/docs/api.md)

---

##  Key Features & Capstone Requirements
- **Vista Tech Corporate UI**: Custom modern dark glassmorphism design system integrated with official Vista Tech branding.
- **Dynamic Working Day Engine (Rule 1)**: Calculates net leave duration by excluding Saturdays, Sundays, and registered company public holidays automatically.
- **Leave Overlap Prevention (Rule 2)**: Rejects double-booking leave dates for the same employee.
- **Quota & Balance Enforcement (Rule 3)**: Validates remaining leave balance prior to request submission.
- **Manager Approval Workflow (Rule 6 & 7)**: Managers review pending team applications, submit feedback comments, and trigger automatic balance deductions upon approval.
- **HR Administrative Control Panel**: HR Admins can create user accounts, edit details, assign line managers, override yearly quotas, configure leave categories (with soft delete), and register company holidays.
- **Asynchronous Email Notifications**: Non-blocking `@Async` notifications sent upon leave submission, approval, or rejection.
- **Spring Security & JWT Authentication**: Stateless authentication with HMAC-SHA384 JWT tokens and BCrypt password encryption (*PDF §15 Headline Bonus Achieved*).
- **Interactive API Documentation**: Swagger UI specs accessible at `http://localhost:8080/swagger-ui/index.html`.

---

##  Technology Stack & Versions
- **Backend**: Java 21 LTS, Spring Boot 3.3.2, Spring Data JPA, Spring Security, JJWT 0.12.5, Lombok, Slf4j.
- **Frontend**: Angular 17 (Standalone Components, RxJS, TypeScript 5, Custom Glassmorphism Design System).
- **Database**: MySQL 8.4 LTS (`elms_db`).
- **Build Tools**: Apache Maven (`mvnw.cmd`), Angular CLI (`@angular/cli@17`).

---

##  Execution & Run Commands

### 1. Database Setup (MySQL 8.4)
Create local database:
```sql
CREATE DATABASE elms_db;
```
Configure database credentials in `backend/src/main/resources/application-local.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/elms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=12345
```

### 2. Run Backend (Spring Boot 3.3.2)
Open a terminal in the `backend` directory and run:
```powershell
cd backend
.\mvnw.cmd spring-boot:run
```
- **REST API Base URL**: `http://localhost:8080/api`
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`

### 3. Run Frontend (Angular 17 SPA)
Open a separate terminal in the `frontend` directory and run:
```powershell
cd frontend
npm start
```
- **Application URL**: `http://localhost:4200`

---

## Initial Baseline System Admin Account
Upon initial application startup, the system automatically initializes a clean baseline HR Admin account:
- **Email**: `admin@elms.com`
- **Password**: `admin123`
- **Role**: `HR_ADMIN`

*(Log in as HR Admin to create employee/manager accounts and assign line managers via the `/admin/users` UI).*

---

## Note on Access-Control Simplification & Security Architecture
In accordance with capstone requirements:
- The system supports both **Header-Based Authorization (`X-User-Id`)** for lightweight role/ownership checks and **Spring Security JWT Authentication** with BCrypt password hashing (*Headline Bonus Goal*).
- Backend service layers enforce strict ownership and reporting-line checks so managers cannot approve requests outside their direct report hierarchy, and employees cannot modify another user's leave applications.

---

## Running Automated Tests
- **Backend Unit & Integration Tests**:
  ```powershell
  cd backend
  .\mvnw.cmd test
  ```
- **Frontend Angular Build Verification**:
  ```powershell
  cd frontend
  npx ng build
  ```

---

##  Complete Project Documentation Index
- [`docs/learning-log.md`](file:///d:/ELMS/docs/learning-log.md): Commit-by-commit development and concept log across all 30 commits.
- [`docs/viva-preparation.md`](file:///d:/ELMS/docs/viva-preparation.md): Top 50 viva Q&As and live coding refactoring tasks.
- [`docs/architecture.md`](file:///d:/ELMS/docs/architecture.md): System architecture, tech stack, and layered design.
- [`docs/api.md`](file:///d:/ELMS/docs/api.md): Complete REST API contract specification.
- [`docs/database.md`](file:///d:/ELMS/docs/database.md): MySQL 8.4 schema dictionary and ERD diagram.
- [`docs/business-rules.md`](file:///d:/ELMS/docs/business-rules.md): Core business rules & validation logic.
- [`docs/capstone-project-report.md`](file:///d:/ELMS/docs/capstone-project-report.md): Final capstone project completion report.
- [`docs/candidate-declaration.md`](file:///d:/ELMS/docs/candidate-declaration.md): Signed Appendix A Candidate Declaration for Karanam Ganesh.
