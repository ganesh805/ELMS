# ELMS Architecture & System Design

## 1. System Overview
The **Employee Leave Management System (ELMS)** is designed using a modern full-stack decoupled architecture:
- **Frontend**: Single Page Application (SPA) built with **Angular 17** (Standalone Component Architecture, RxJS, TypeScript 5, Vanilla CSS Glassmorphism Design System).
- **Backend**: RESTful API service built with **Spring Boot 3.3.2** and **Java 21 LTS**.
- **Database**: Relational database storage using **MySQL 8.4 LTS** via **Spring Data JPA / Hibernate**.
- **Security**: **Spring Security** with **JSON Web Tokens (JWT)** and **Role-Based Access Control (RBAC)**.

---

## 2. High-Level System Architecture Diagram

```
+-----------------------------------------------------------------------+
|                         Angular 17 SPA Frontend                       |
|  (Navbar, Sidebar, AuthGuard, JwtInterceptor, RxJS State Management)  |
+-----------------------------------------------------------------------+
                                   |
                          HTTP / REST API Calls
                      Authorization: Bearer <JWT>
                          X-User-Id: <UserId>
                                   v
+-----------------------------------------------------------------------+
|                       Spring Boot 3.3.2 Backend                       |
|                                                                       |
|  +-----------------------------------------------------------------+  |
|  |                     JwtAuthenticationFilter                     |  |
|  +-----------------------------------------------------------------+  |
|                                   |                                   |
|  +-----------------------------------------------------------------+  |
|  |                       REST Controllers                          |  |
|  | (AuthController, EmployeeLeaveController, ManagerLeaveController|  |
|  |  AdminController, LeaveTypeController, HolidayController)       |  |
|  +-----------------------------------------------------------------+  |
|                                   |                                   |
|  +-----------------------------------------------------------------+  |
|  |                         Service Layer                           |  |
|  | (WorkingDayService, LeaveRequestService, AuthService, etc.)     |  |
|  +-----------------------------------------------------------------+  |
|                                   |                                   |
|  +-----------------------------------------------------------------+  |
|  |                  Spring Data JPA Repositories                   |  |
|  +-----------------------------------------------------------------+  |
+-----------------------------------------------------------------------+
                                   |
                            Hibernate / JDBC
                                   v
+-----------------------------------------------------------------------+
|                          MySQL 8.4 Database                           |
|      (users, leave_types, leave_balances, leave_requests, holidays)   |
+-----------------------------------------------------------------------+
```

---

## 3. Technology Stack Summary
- **JDK Version**: Java 21 LTS
- **Framework**: Spring Boot 3.3.2
- **Persistence**: Spring Data JPA + Hibernate
- **Database**: MySQL 8.4 LTS (`elms_db`)
- **Security**: Spring Security + JJWT 0.12.5 (HMAC-SHA384)
- **API Specs**: SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui/index.html`)
- **Frontend Framework**: Angular 17 (Standalone components, Signals/RxJS)
- **Styling**: Custom CSS Design System (Glassmorphism Dark Theme)

---

## 4. Layered Architecture Principles
1. **Controller Layer**: Handles HTTP requests, validates DTO inputs, and delegates business operations to services.
2. **Service Layer**: Implements core domain business rules (net working day calculations, overlap checks, state machine transitions, automatic quota deductions).
3. **Repository Layer**: Provides database persistence via Spring Data JPA interfaces.
4. **Security Filter Layer**: Validates JWT signatures statelessly and enforces role authorization (`ROLE_HR_ADMIN`, `ROLE_MANAGER`, `ROLE_EMPLOYEE`).
