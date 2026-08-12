# ELMS Development & Learning Log

This document records the incremental development of the Employee Leave Management System (ELMS), documenting technical concepts, architecture decisions, runtime flows, viva preparation, and live coding exercises for every commit.

---

# Commit 01 — chore: initialize ELMS repository and project structure

## What I built
- Initialized the Git repository for the ELMS full-stack project.
- Configured root `.gitignore` to prevent committing build artifacts (`target/`, `node_modules/`, `dist/`), IDE files (`.idea/`, `.vscode/`), and sensitive credentials (`application-local.properties`).
- Created the Spring Boot 3.3.2 backend project structure with Java 21 LTS configuration in `pom.xml`.
- Added key dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-validation`, `mysql-connector-j`, `springdoc-openapi-starter-webmvc-ui`, and `lombok`.
- Configured initial `application.properties` and a safe template `application-local.example.properties`.
- Created main application entry point `ElmsApplication.java` and root `README.md`.

## Why this feature is needed
A solid project foundation ensures build reproducibility, dependency version alignment (Java 21 + Spring Boot 3.3.x), and strict security boundaries protecting database credentials from Git history from day one.

## Files created
- [`.gitignore`](file:///d:/ELMS/.gitignore)
- [`README.md`](file:///d:/ELMS/README.md)
- [`backend/pom.xml`](file:///d:/ELMS/backend/pom.xml)
- [`backend/src/main/java/com/elms/ElmsApplication.java`](file:///d:/ELMS/backend/src/main/java/com/elms/ElmsApplication.java)
- [`backend/src/main/resources/application.properties`](file:///d:/ELMS/backend/src/main/resources/application.properties)
- [`backend/src/main/resources/application-local.example.properties`](file:///d:/ELMS/backend/src/main/resources/application-local.example.properties)

## Architecture concept
The **Layered Architecture** separates responsibilities into distinct directories/modules. By placing backend logic inside `backend/` and keeping configuration isolated, we ensure clean dependency management and straightforward deployment pipelines.

## Java concept
**Java 21 LTS Source and Target Compatibility**: Set via `<java.version>21</java.version>` in `pom.xml`. Enables modern language features such as pattern matching for switch, record classes, and virtual threads while ensuring long-term support (LTS).

## Spring Boot concept
**`@SpringBootApplication`**: An umbrella annotation combining three key annotations:
1. `@SpringBootConfiguration`: Marks the class as a source of bean definitions.
2. `@EnableAutoConfiguration`: Tells Spring Boot to automatically configure beans based on classpath dependencies.
3. `@ComponentScan`: Tells Spring to scan the `com.elms` package and its sub-packages for Spring components (`@Controller`, `@Service`, `@Repository`, `@Component`).

## MySQL concept
**Externalized Credentials Pattern**: Standard practice of separating database host, port, username, and password from source code by storing them in local environment files (`application-local.properties`) that are excluded from Git.

## How to test it
1. Verify Java version: `java -version` (Must be 21).
2. Verify Maven compilation: `cd backend && mvn clean compile`
3. Verify Git status: `git status` (Ensure no secrets or temporary files are untracked).

## Expected result
- Maven cleanly downloads dependencies and compiles `ElmsApplication.java` without errors.
- `application-local.properties` is ignored by Git if created locally.

## Viva explanation
> "For Commit 01, I initialized a clean full-stack project repository adhering to Spring Boot 3.3.x and Java 21 standards. I configured Maven dependencies for Web, Data JPA, Bean Validation, MySQL, and Swagger UI. To maintain strict security hygiene, I set up a root `.gitignore` to prevent credential exposure and provided `application-local.example.properties` as a safe configuration template for local development."

## Likely viva questions & short answers
- **Q1: Why did you use Spring Boot 3.3.x instead of 2.x or 4.x?**
  - *Answer*: Spring Boot 3.x is required for Java 17+ / Java 21 LTS compatibility and Jakarta EE 10 standards. Spring Boot 4.x is not yet released as a stable production version.
- **Q2: What is the purpose of `application-local.example.properties`?**
  - *Answer*: It serves as a version-controlled documentation template showing team members which configuration keys are required locally without checking actual database secrets into Git.
- **Q3: What does `@SpringBootApplication` do?**
  - *Answer*: It enables auto-configuration, component scanning, and defines the class as a configuration bean for Spring's ApplicationContext.

---

# Commit 02 — chore: configure Java 21 Spring Boot and MySQL

## What I built
- Created local MySQL database `elms_db` in MySQL 8.4 LTS.
- Configured HikariCP connection pool settings in `application-local.properties` (git-ignored).
- Enabled Hibernate auto-DDL update mode (`spring.jpa.hibernate.ddl-auto=update`) for development.
- Verified successful database connection during Spring Boot startup.

## Why this feature is needed
Connecting Spring Boot to MySQL via Spring Data JPA allows Hibernate to map Java entities directly to database tables and execute queries safely.

## Files created / modified
- [`backend/src/main/resources/application-local.properties`](file:///d:/ELMS/backend/src/main/resources/application-local.properties) (Git-ignored)
- [`docs/learning-log.md`](file:///d:/ELMS/docs/learning-log.md)

## Spring Boot & JPA concept
**HikariCP Connection Pool**: Spring Boot's default high-performance JDBC connection pool. Instead of opening a new TCP connection for every SQL query, HikariCP reuses a pool of active database connections.

## MySQL concept
**InnoDB Storage Engine & Character Set**: MySQL 8.4 defaults to InnoDB with `utf8mb4` encoding, supporting ACID transactions, foreign key constraints, and multi-byte character sets.

## Viva explanation
> "In Commit 02, I connected our Spring Boot 3.3.2 application to MySQL 8.4 LTS using HikariCP. I stored database credentials securely in `application-local.properties` which is ignored by Git, ensuring zero risk of credential leaks."

---

# Commit 03 — feat: configure JPA entities and relationships

## What I built
- Designed and implemented 5 JPA `@Entity` classes: `User`, `LeaveType`, `LeaveBalance`, `LeaveRequest`, `Holiday`.
- Configured `Role` (`EMPLOYEE`, `MANAGER`, `HR_ADMIN`) and `LeaveStatus` (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`) enums.
- Mapped self-referencing `@ManyToOne` relationship on `User.manager` pointing to direct line manager.
- Configured unique constraints: `users.email`, `holidays.date`, `leave_types.name`, and composite unique constraint on `leave_balances(user_id, leave_type_id, year)`.

## Why this feature is needed
JPA entities define the Object-Relational Mapping (ORM) between Java object models and MySQL database tables, enabling automated schema generation and object-oriented data access.

## Files created
- [`backend/src/main/java/com/elms/entity/User.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/User.java)
- [`backend/src/main/java/com/elms/entity/LeaveType.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/LeaveType.java)
- [`backend/src/main/java/com/elms/entity/LeaveBalance.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/LeaveBalance.java)
- [`backend/src/main/java/com/elms/entity/LeaveRequest.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/LeaveRequest.java)
- [`backend/src/main/java/com/elms/entity/Holiday.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/Holiday.java)
- [`backend/src/main/java/com/elms/entity/enums/Role.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/enums/Role.java)
- [`backend/src/main/java/com/elms/entity/enums/LeaveStatus.java`](file:///d:/ELMS/backend/src/main/java/com/elms/entity/enums/LeaveStatus.java)

## JPA / Hibernate concept
**Self-Referencing Entity (`manager_id`)**: A `@ManyToOne` relationship where an instance of `User` points to another instance of `User` representing their line manager. Root managers have `manager_id = NULL`.

## Viva explanation
> "In Commit 03, I mapped the core domain model using Jakarta Persistence annotations. I implemented a self-referencing relationship on the User entity for manager hierarchies, mapped composite unique constraints for leave balances, and let Hibernate generate foreign key constraints in MySQL."

---

# Commit 04 — feat: add repositories and initial seed data

## What I built
- Created 5 Spring Data JPA Repositories: `UserRepository`, `LeaveTypeRepository`, `LeaveBalanceRepository`, `LeaveRequestRepository`, `HolidayRepository`.
- Implemented `DataInitializer` bean to populate baseline system setup on application startup (Default HR Admin account and base leave categories).

## Why this feature is needed
Repositories abstract SQL queries into clean Java methods. Baseline initialization provides the initial HR Admin account (`admin@elms.com` / `admin123`) so administrators can log in and create user accounts step by step.

## Files created
- [`backend/src/main/java/com/elms/repository/UserRepository.java`](file:///d:/ELMS/backend/src/main/java/com/elms/repository/UserRepository.java)
- [`backend/src/main/java/com/elms/repository/LeaveTypeRepository.java`](file:///d:/ELMS/backend/src/main/java/com/elms/repository/LeaveTypeRepository.java)
- [`backend/src/main/java/com/elms/repository/LeaveBalanceRepository.java`](file:///d:/ELMS/backend/src/main/java/com/elms/repository/LeaveBalanceRepository.java)
- [`backend/src/main/java/com/elms/repository/LeaveRequestRepository.java`](file:///d:/ELMS/backend/src/main/java/com/elms/repository/LeaveRequestRepository.java)
- [`backend/src/main/java/com/elms/repository/HolidayRepository.java`](file:///d:/ELMS/backend/src/main/java/com/elms/repository/HolidayRepository.java)
- [`backend/src/main/java/com/elms/config/DataInitializer.java`](file:///d:/ELMS/backend/src/main/java/com/elms/config/DataInitializer.java)

## Spring Data JPA concept
**Derived Query Methods & JPQL `@Query`**: Spring Data JPA automatically generates SQL queries from method signatures (e.g. `findByEmail`, `findByUserIdAndYear`). For complex date range overlap conditions, explicit JPQL queries (`@Query`) are used.

## Viva explanation
> "In Commit 04, I created Spring Data JPA repositories and a DataInitializer startup component. This sets up the baseline system with default leave categories and an initial HR Admin account so administrators can log in and populate accounts manually."

---

# Commit 05 — feat: implement basic user login API

## What I built
- Implemented `POST /api/auth/login` REST endpoint accepting `LoginRequestDTO` (email, password).
- Created `AuthService` with `@Transactional(readOnly = true)` to validate user credentials and return sanitized `UserDTO`.
- Configured `GlobalExceptionHandler` to transform custom exceptions into structured `ErrorResponseDTO` responses with standard HTTP status codes (200, 400, 404, 500).
- Created `EntityMapper` to map entity models to DTOs without leaking sensitive password fields.

## Why this feature is needed
Implements the capstone's simplified login authentication requirement, returning user identity details (ID, name, role, manager ID) for frontend state management and API header authorization.

## Files created
- [`backend/src/main/java/com/elms/controller/AuthController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/AuthController.java)
- [`backend/src/main/java/com/elms/service/AuthService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/AuthService.java)
- [`backend/src/main/java/com/elms/dto/request/LoginRequestDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/LoginRequestDTO.java)
- [`backend/src/main/java/com/elms/dto/response/UserDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/UserDTO.java)
- [`backend/src/main/java/com/elms/dto/response/ErrorResponseDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/ErrorResponseDTO.java)
- [`backend/src/main/java/com/elms/mapper/EntityMapper.java`](file:///d:/ELMS/backend/src/main/java/com/elms/mapper/EntityMapper.java)
- [`backend/src/main/java/com/elms/exception/GlobalExceptionHandler.java`](file:///d:/ELMS/backend/src/main/java/com/elms/exception/GlobalExceptionHandler.java)
- [`backend/src/main/java/com/elms/exception/ResourceNotFoundException.java`](file:///d:/ELMS/backend/src/main/java/com/elms/exception/ResourceNotFoundException.java)
- [`backend/src/main/java/com/elms/exception/BusinessRuleException.java`](file:///d:/ELMS/backend/src/main/java/com/elms/exception/BusinessRuleException.java)

## Spring Web & Exception Handling concept
**`@RestControllerAdvice` & `@ExceptionHandler`**: Intercepts exceptions thrown across controllers and formats uniform JSON error responses rather than default HTML error pages.

## Viva explanation
> "In Commit 05, I built the authentication endpoint `POST /api/auth/login`. I used DTOs to encapsulate request payloads and response attributes, preventing sensitive entity fields like password from being exposed. I also implemented a `@RestControllerAdvice` exception handler for consistent REST error contracts."

---

# Commit 06 — feat: implement leave types holidays and balances

## What I built
- Implemented REST endpoints for Leave Types (`GET /api/leave-types`), Public Holidays (`GET /api/holidays`, `GET /api/holidays/upcoming`), and Leave Balances (`GET /api/leave-balances/my`).
- Implemented `LeaveTypeService`, `HolidayService`, and `LeaveBalanceService`.
- Used `@RequestHeader("X-User-Id")` in `LeaveBalanceController` to extract acting user identity.
- Added mapping methods in `EntityMapper` for `LeaveTypeDTO`, `HolidayDTO`, and `LeaveBalanceDTO`.

## Why this feature is needed
Allows employees to view active leave categories, upcoming company holidays, and their personal leave balance quotas for the current year.

## Files created
- [`backend/src/main/java/com/elms/controller/LeaveTypeController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/LeaveTypeController.java)
- [`backend/src/main/java/com/elms/controller/HolidayController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/HolidayController.java)
- [`backend/src/main/java/com/elms/controller/LeaveBalanceController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/LeaveBalanceController.java)
- [`backend/src/main/java/com/elms/service/LeaveTypeService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveTypeService.java)
- [`backend/src/main/java/com/elms/service/HolidayService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/HolidayService.java)
- [`backend/src/main/java/com/elms/service/LeaveBalanceService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveBalanceService.java)
- [`backend/src/main/java/com/elms/dto/response/LeaveTypeDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/LeaveTypeDTO.java)
- [`backend/src/main/java/com/elms/dto/response/HolidayDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/HolidayDTO.java)
- [`backend/src/main/java/com/elms/dto/response/LeaveBalanceDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/LeaveBalanceDTO.java)

## Spring Web & Header authorization concept
**`@RequestHeader("X-User-Id")`**: In the capstone's simplified authentication architecture, the client sends the logged-in user's ID in the `X-User-Id` request header to identify who is making the API call.

## Viva explanation
> "In Commit 06, I built endpoints for retrieving leave categories, company holidays, and personal leave balances. I extracted user context via the `X-User-Id` request header and mapped entities to response DTOs using `@Transactional(readOnly = true)` service calls."

---

# Commit 07 — feat: implement working day calculation

## What I built
- Implemented `WorkingDayService` calculating net working leave days between start and end dates (**Business Rule 1**).
- Excluded Saturdays, Sundays, and public holidays stored in the `Holiday` database table.
- Created comprehensive Mockito unit tests in `WorkingDayServiceTest` covering 6 scenarios (Monday-Friday, Friday-Monday, weekends, holiday exclusions, single-day leave, invalid ranges).

## Why this feature is needed
Business Rule 1 requires that leave duration is calculated strictly as net working days, excluding weekend days and company holidays.

## Files created
- [`backend/src/main/java/com/elms/service/WorkingDayService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/WorkingDayService.java)
- [`backend/src/test/java/com/elms/service/WorkingDayServiceTest.java`](file:///d:/ELMS/backend/src/test/java/com/elms/service/WorkingDayServiceTest.java)

## Core Business Rule & Date API concept
**`java.time.LocalDate` & `DayOfWeek`**: Iterates date range day-by-day. Checks `current.getDayOfWeek()` against `SATURDAY` / `SUNDAY` and queries DB holidays for exclusion.

## Viva explanation
> "In Commit 07, I implemented Business Rule 1 in WorkingDayService. The calculation iterates through the date interval, skipping Saturdays, Sundays, and dates present in the Holiday table. I verified this with 6 Mockito unit test cases covering edge cases."

---

# Commit 08 — feat: implement leave request creation and validation

## What I built
- Implemented `POST /api/leaves` and `GET /api/leaves/my` REST endpoints in `EmployeeLeaveController`.
- Created `LeaveCreateDTO` with Bean Validation constraints (`@FutureOrPresent`, `@NotNull`, `@NotBlank`).
- Implemented `LeaveRequestService.createLeaveRequest(...)` integrating `WorkingDayService` for automatic working days calculation.
- Enforced date range validations (**Rule 4 & 5**: `endDate >= startDate` and `startDate >= today`).
- Added `LeaveRequestDTO` mapping method in `EntityMapper`.

## Why this feature is needed
Enables employees to apply for leave with automatic working day calculation and retrieve their personal leave application history.

## Files created
- [`backend/src/main/java/com/elms/controller/EmployeeLeaveController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/EmployeeLeaveController.java)
- [`backend/src/main/java/com/elms/service/LeaveRequestService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveRequestService.java)
- [`backend/src/main/java/com/elms/dto/request/LeaveCreateDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/LeaveCreateDTO.java)
- [`backend/src/main/java/com/elms/dto/response/LeaveRequestDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/LeaveRequestDTO.java)

## Spring Web & Transactional concept
**`@Transactional` Mutation Methods**: Service method creates and persists new `LeaveRequest` entities with initial `PENDING` status and sets `appliedOn` timestamp automatically.

## Viva explanation
> "In Commit 08, I built the leave submission endpoint `POST /api/leaves` and personal history endpoint `GET /api/leaves/my`. The service validates date ranges, injects WorkingDayService to compute net requested days, and saves requests with PENDING status."

---

# Commit 09 — feat: implement leave overlap validation

## What I built
- Implemented **Business Rule 2** (Leave Overlap Validation) in `LeaveRequestService`.
- Checked for overlapping `PENDING` or `APPROVED` leave requests for the employee using `leaveRequestRepository.findOverlappingRequests(...)`.
- Throws `BusinessRuleException` if an overlap is detected.
- Added unit tests in `LeaveRequestServiceTest` covering overlap scenarios.

## Why this feature is needed
Business Rule 2 strictly forbids double-booking leave dates for an employee if an existing request is pending or approved.

## Files modified / created
- [`backend/src/main/java/com/elms/service/LeaveRequestService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveRequestService.java)
- [`backend/src/test/java/com/elms/service/LeaveRequestServiceTest.java`](file:///d:/ELMS/backend/src/test/java/com/elms/service/LeaveRequestServiceTest.java)

## Core Business Rule & Overlap Math concept
**Interval Overlap Principle**: Two intervals `[S1, E1]` and `[S2, E2]` overlap if and only if `(S1 <= E2 AND E1 >= S2)`.

## Viva explanation
> "In Commit 09, I implemented Business Rule 2 to prevent leave overlaps. The query checks if any PENDING or APPROVED request overlaps with the requested start and end dates. If found, a BusinessRuleException is thrown."

---

# Commit 10 — feat: implement leave balance validation

## What I built
- Implemented **Business Rule 3** (Leave Balance Validation) in `LeaveRequestService`.
- Queried `LeaveBalance` for `(userId, leaveTypeId, year)`.
- Rejects leave applications where `requestedWorkingDays > balance.getRemaining()` by throwing `InsufficientLeaveBalanceException`.
- Handled `InsufficientLeaveBalanceException` in `GlobalExceptionHandler` returning `400 Bad Request`.
- Added unit tests in `LeaveRequestServiceTest` verifying balance validation logic.

## Why this feature is needed
Business Rule 3 ensures an employee cannot request more leave days than their remaining quota for that leave type and year.

## Files created / modified
- [`backend/src/main/java/com/elms/exception/InsufficientLeaveBalanceException.java`](file:///d:/ELMS/backend/src/main/java/com/elms/exception/InsufficientLeaveBalanceException.java)
- [`backend/src/main/java/com/elms/service/LeaveRequestService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveRequestService.java)
- [`backend/src/main/java/com/elms/exception/GlobalExceptionHandler.java`](file:///d:/ELMS/backend/src/main/java/com/elms/exception/GlobalExceptionHandler.java)
- [`backend/src/test/java/com/elms/service/LeaveRequestServiceTest.java`](file:///d:/ELMS/backend/src/test/java/com/elms/service/LeaveRequestServiceTest.java)

## Business Rule concept
**Quota Enforcing**: Verifying `requestedDays <= remainingBalance` prevents negative leave balances from being submitted into the approval queue.

## Viva explanation
> "In Commit 10, I implemented Business Rule 3 to validate leave balances before request submission. The service fetches the employee's LeaveBalance for the target year and throws an InsufficientLeaveBalanceException if requested days exceed remaining quota."

---

# Commit 11 — feat: implement leave approval rejection and cancellation

## What I built
- Created `DecisionRequestDTO` and `InvalidLeaveStateException`.
- Implemented approval (`approveLeaveRequest`), rejection (`rejectLeaveRequest`), and cancellation (`cancelLeaveRequest`) service methods in `LeaveRequestService`.
- Created `ManagerLeaveController` exposing `GET /api/leaves/pending`, `PUT /api/leaves/{id}/approve`, and `PUT /api/leaves/{id}/reject`.
- Added `PUT /api/leaves/{id}/cancel` in `EmployeeLeaveController`.
- Enforced **Rule 6 State Machine**: state transitions are allowed strictly from `PENDING` state.

## Why this feature is needed
Enables direct line managers to review, approve, or reject pending leave requests submitted by team members, and allows employees to cancel their pending applications.

## Files created / modified
- [`backend/src/main/java/com/elms/dto/request/DecisionRequestDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/DecisionRequestDTO.java)
- [`backend/src/main/java/com/elms/exception/InvalidLeaveStateException.java`](file:///d:/ELMS/backend/src/main/java/com/elms/exception/InvalidLeaveStateException.java)
- [`backend/src/main/java/com/elms/controller/ManagerLeaveController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/ManagerLeaveController.java)
- [`backend/src/main/java/com/elms/controller/EmployeeLeaveController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/EmployeeLeaveController.java)
- [`backend/src/main/java/com/elms/service/LeaveRequestService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveRequestService.java)

## State Machine concept
**State Transition Integrity (Rule 6)**: Only requests in the `PENDING` state can transition to `APPROVED`, `REJECTED`, or `CANCELLED`. Once a request transitions out of `PENDING`, its state is final.

## Viva explanation
> "In Commit 11, I implemented the leave approval workflow and state transitions. Managers can retrieve pending team requests and submit decision comments to approve or reject them. Employees can cancel pending requests. All transitions enforce Rule 6, rejecting state changes on non-pending requests."

---

# Commit 12 — feat: implement automatic balance deduction on approval

## What I built
- Implemented **Business Rule 7** (Automatic Balance Deduction) in `LeaveRequestService.approveLeaveRequest(...)`.
- When a leave request is approved, the system fetches the employee's `LeaveBalance` for the target year and automatically updates `used` and `remaining` balances.
- Added unit tests in `LeaveRequestServiceTest` verifying automatic balance deduction on approval.

## Why this feature is needed
Business Rule 7 guarantees that approving a leave request automatically updates the employee's remaining quota, maintaining strict database consistency between leave history and current balances.

## Files modified
- [`backend/src/main/java/com/elms/service/LeaveRequestService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveRequestService.java)
- [`backend/src/test/java/com/elms/service/LeaveRequestServiceTest.java`](file:///d:/ELMS/backend/src/test/java/com/elms/service/LeaveRequestServiceTest.java)

## Transactional State Update concept
**Atomic Balance Mutation**: `balance.setUsed(used + days)` and `balance.setRemaining(allocated - used)` execute inside the `@Transactional` boundary during request approval, preventing quota drift.

## Viva explanation
> "In Commit 12, I implemented Business Rule 7 for automatic balance deduction upon request approval. In approveLeaveRequest, the service fetches the user's LeaveBalance entity for the request's year, increments used days, decrements remaining days, and saves the updated balance in the same transaction."

---

# Commit 13 — feat: implement hr admin user leave and balance management APIs

## What I built
- Created `UserCreateDTO`, `UserUpdateDTO`, and `BalanceAdjustDTO`.
- Implemented `AdminUserService` for creating, updating, and listing users.
- Implemented `adjustBalance` in `LeaveBalanceService` allowing HR Admins to allocate or adjust annual leave quotas per user.
- Created `AdminController` exposing `GET /api/admin/users`, `POST /api/admin/users`, `PUT /api/admin/users/{id}`, `POST /api/admin/leave-balances/adjust`, `GET /api/admin/leaves`, and `PUT /api/admin/leaves/{id}/revoke`.
- Implemented `revokeApprovedLeaveRequest` in `LeaveRequestService` with automatic balance re-crediting.

## Why this feature is needed
Provides HR Administrators with complete administrative control over employee accounts, manager assignments, leave balance quota adjustments, and administrative revocations.

## Files created / modified
- [`backend/src/main/java/com/elms/controller/AdminController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/AdminController.java)
- [`backend/src/main/java/com/elms/service/AdminUserService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/AdminUserService.java)
- [`backend/src/main/java/com/elms/service/LeaveBalanceService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveBalanceService.java)
- [`backend/src/main/java/com/elms/dto/request/UserCreateDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/UserCreateDTO.java)
- [`backend/src/main/java/com/elms/dto/request/UserUpdateDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/UserUpdateDTO.java)
- [`backend/src/main/java/com/elms/dto/request/BalanceAdjustDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/BalanceAdjustDTO.java)

## Administrative Control concept
**Quota Adjustment & Revocation**: HR Admins can manually override allocated quotas and revoke approved leave applications, which re-credits the used days back to the employee's remaining balance automatically.

## Viva explanation
> "In Commit 13, I implemented HR Admin management endpoints in AdminController and AdminUserService. HR Admins can create and edit employee accounts, assign line managers, adjust annual leave quotas, and revoke approved leave requests with automatic balance re-crediting."

---

# Commit 14 — feat: implement leave type management APIs

## What I built
- Created `LeaveTypeCreateDTO` and `LeaveTypeUpdateDTO`.
- Implemented leave category management in `LeaveTypeService` (create, update, soft delete).
- Added `existsByNameIgnoreCase` in `LeaveTypeRepository` to prevent duplicate category names.
- Exposed `POST /api/admin/leave-types`, `PUT /api/admin/leave-types/{id}`, and `DELETE /api/admin/leave-types/{id}` in `AdminController`.

## Why this feature is needed
Allows HR Administrators to configure company leave policies, set default annual quotas, and manage active/inactive leave categories.

## Files created / modified
- [`backend/src/main/java/com/elms/dto/request/LeaveTypeCreateDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/LeaveTypeCreateDTO.java)
- [`backend/src/main/java/com/elms/dto/request/LeaveTypeUpdateDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/LeaveTypeUpdateDTO.java)
- [`backend/src/main/java/com/elms/service/LeaveTypeService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveTypeService.java)
- [`backend/src/main/java/com/elms/repository/LeaveTypeRepository.java`](file:///d:/ELMS/backend/src/main/java/com/elms/repository/LeaveTypeRepository.java)
- [`backend/src/main/java/com/elms/controller/AdminController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/AdminController.java)

## Soft Delete Pattern concept
**Data Preservation**: Calling `DELETE /api/admin/leave-types/{id}` updates `active = false` rather than executing SQL `DELETE`, preserving historic leave requests linked to that leave type.

## Viva explanation
> "In Commit 14, I implemented leave category CRUD operations for HR Admins. The service prevents duplicate category names and uses a soft-delete pattern setting active = false to preserve historical leave records."

---

# Commit 15 — feat: implement holiday management and medical file attachment service

## What I built
- Created `HolidayCreateDTO` and implemented `createHoliday` / `deleteHoliday` in `HolidayService`.
- Created `AttachmentService` for storing and serving uploaded medical certificate attachments (`POST /api/leaves/upload-attachment`, `GET /api/leaves/attachments/{fileName}`).
- Exposed `POST /api/admin/holidays` and `DELETE /api/admin/holidays/{id}` in `AdminController`.

## Why this feature is needed
Enables HR Admins to register company public holidays (which dynamically adjust working day calculations) and allows employees to upload medical documents for Sick Leave applications.

## Files created / modified
- [`backend/src/main/java/com/elms/dto/request/HolidayCreateDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/request/HolidayCreateDTO.java)
- [`backend/src/main/java/com/elms/service/AttachmentService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/AttachmentService.java)
- [`backend/src/main/java/com/elms/service/HolidayService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/HolidayService.java)
- [`backend/src/main/java/com/elms/controller/EmployeeLeaveController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/EmployeeLeaveController.java)
- [`backend/src/main/java/com/elms/controller/AdminController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/AdminController.java)

## File Storage concept
**UUID Prefixing**: Files saved to `uploads/` are prefixed with `UUID.randomUUID()` to prevent filename collisions when multiple users upload files with identical names (e.g. `medical_certificate.pdf`).

## Viva explanation
> "In Commit 15, I built public holiday management and the medical attachment upload service. Uploaded files are stored safely in an external uploads directory using UUID prefixing to prevent filename collisions."

---

# Commit 16 — feat: implement email notification service and swagger openapi documentation

## What I built
- Created `EmailService` handling asynchronous email notifications (`@Async`) for leave submission, approval, and rejection events.
- Integrated `EmailService` into `LeaveRequestService`.
- Created `OpenApiConfig` configuring SpringDoc OpenAPI 3 / Swagger UI specifications with JWT bearer scheme.
- Added `spring-boot-starter-mail`, `spring-boot-starter-security`, and `jjwt` dependencies in `pom.xml`.

## Why this feature is needed
Provides automatic email notifications to managers and employees upon workflow status changes, and exposes interactive API documentation at `/swagger-ui/index.html`.

## Files created / modified
- [`backend/src/main/java/com/elms/service/EmailService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/EmailService.java)
- [`backend/src/main/java/com/elms/config/OpenApiConfig.java`](file:///d:/ELMS/backend/src/main/java/com/elms/config/OpenApiConfig.java)
- [`backend/src/main/java/com/elms/service/LeaveRequestService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/LeaveRequestService.java)
- [`backend/pom.xml`](file:///d:/ELMS/backend/pom.xml)

## Asynchronous Processing concept
**`@Async` Non-Blocking Execution**: Email notifications execute on a separate background worker thread, ensuring API endpoints respond instantly to users without waiting for SMTP network latency.

## Viva explanation
> "In Commit 16, I built the asynchronous EmailService and configured OpenAPI/Swagger documentation. Email notifications execute non-blockingly using @Async so user API requests complete immediately without network delay."

---

# Commit 17 — feat: implement spring security and jwt authentication

## What I built
- Implemented `JwtTokenProvider` for HMAC-SHA384 token generation and validation.
- Implemented `CustomUserDetailsService` loading user authentication details and roles from MySQL.
- Implemented `JwtAuthenticationFilter` verifying Bearer JWT tokens and `X-User-Id` headers.
- Created `SecurityConfig` setting stateless session management and role-based endpoint authorization (`HR_ADMIN`, `MANAGER`, `EMPLOYEE`).
- Updated `AuthService` and `AuthController` returning `JwtResponseDTO` with BCrypt password verification.
- Updated `DataInitializer` encoding seed user passwords with BCrypt.

## Why this feature is needed
Secures all backend REST APIs using industry-standard JWT authentication and role-based access control (RBAC), completing the entire Spring Boot backend.

## Files created / modified
- [`backend/src/main/java/com/elms/security/JwtTokenProvider.java`](file:///d:/ELMS/backend/src/main/java/com/elms/security/JwtTokenProvider.java)
- [`backend/src/main/java/com/elms/security/CustomUserDetailsService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/security/CustomUserDetailsService.java)
- [`backend/src/main/java/com/elms/security/JwtAuthenticationFilter.java`](file:///d:/ELMS/backend/src/main/java/com/elms/security/JwtAuthenticationFilter.java)
- [`backend/src/main/java/com/elms/config/SecurityConfig.java`](file:///d:/ELMS/backend/src/main/java/com/elms/config/SecurityConfig.java)
- [`backend/src/main/java/com/elms/dto/response/JwtResponseDTO.java`](file:///d:/ELMS/backend/src/main/java/com/elms/dto/response/JwtResponseDTO.java)
- [`backend/src/main/java/com/elms/service/AuthService.java`](file:///d:/ELMS/backend/src/main/java/com/elms/service/AuthService.java)
- [`backend/src/main/java/com/elms/controller/AuthController.java`](file:///d:/ELMS/backend/src/main/java/com/elms/controller/AuthController.java)
- [`backend/src/main/java/com/elms/config/DataInitializer.java`](file:///d:/ELMS/backend/src/main/java/com/elms/config/DataInitializer.java)

## JWT & Spring Security concept
**Stateless Authentication & Role-Based Access Control**: Requests are authenticated statelessly using signed JWT claims. Spring Security enforces role boundaries so `/api/admin/**` requires `ROLE_HR_ADMIN` and manager approval endpoints require `ROLE_MANAGER`.

## Viva explanation
> "In Commit 17, I implemented Spring Security and JWT authentication. Passwords are encrypted using BCrypt, and API endpoints are protected using a stateless JwtAuthenticationFilter with role-based access control for HR Admins, Managers, and Employees."

---

# Commit 18 — chore: initialize angular 17 application

## What I built
- Initialized Angular 17 workspace in `frontend/` directory using `@angular/cli`.
- Configured `angular.json`, `package.json`, `tsconfig.json`, and standalone application config `app.config.ts`.
- Created environment configuration files (`environment.ts`, `environment.development.ts`) setting `apiUrl: 'http://localhost:8080/api'`.
- Registered `provideHttpClient(withFetch())` in `app.config.ts` for HTTP REST API communications.

## Why this feature is needed
Initializes the Single Page Application (SPA) frontend project structure, establishing environment configurations and HTTP client providers for backend REST integration.

## Files created
- [`frontend/angular.json`](file:///d:/ELMS/frontend/angular.json)
- [`frontend/package.json`](file:///d:/ELMS/frontend/package.json)
- [`frontend/src/app/app.config.ts`](file:///d:/ELMS/frontend/src/app/app.config.ts)
- [`frontend/src/environments/environment.ts`](file:///d:/ELMS/frontend/src/environments/environment.ts)
- [`frontend/src/environments/environment.development.ts`](file:///d:/ELMS/frontend/src/environments/environment.development.ts)

## Angular Standalone & HTTP Client concept
**Standalone Application Architecture & `provideHttpClient`**: Angular 17 introduces standalone components without `NgModule`. `provideHttpClient(withFetch())` configures Angular's HTTP client using standard web fetch APIs.

## Viva explanation
> "In Commit 18, I initialized the Angular 17 frontend workspace. I set up environment configuration files pointing to our Spring Boot backend at http://localhost:8080/api and enabled provideHttpClient in app.config.ts for REST communication."

---

# Commit 19 — feat: create angular design system and layout components

## What I built
- Designed global CSS design system in `styles.css` using modern glassmorphism aesthetic, dark theme palette, status badge utilities, custom buttons, forms, and table styles.
- Built standalone `NavbarComponent` (`app-navbar`) displaying brand logo, user profile, role badge, and logout action.
- Built standalone `SidebarComponent` (`app-sidebar`) rendering dynamic navigation links based on user role (`EMPLOYEE`, `MANAGER`, `HR_ADMIN`).

## Why this feature is needed
Establishes the core UI theme, global styles, and responsive navigation shell for the entire ELMS Angular single-page application.

## Files created
- [`frontend/src/styles.css`](file:///d:/ELMS/frontend/src/styles.css)
- [`frontend/src/app/components/navbar/navbar.component.ts`](file:///d:/ELMS/frontend/src/app/components/navbar/navbar.component.ts)
- [`frontend/src/app/components/navbar/navbar.component.html`](file:///d:/ELMS/frontend/src/app/components/navbar/navbar.component.html)
- [`frontend/src/app/components/navbar/navbar.component.css`](file:///d:/ELMS/frontend/src/app/components/navbar/navbar.component.css)
- [`frontend/src/app/components/sidebar/sidebar.component.ts`](file:///d:/ELMS/frontend/src/app/components/sidebar/sidebar.component.ts)
- [`frontend/src/app/components/sidebar/sidebar.component.html`](file:///d:/ELMS/frontend/src/app/components/sidebar/sidebar.component.html)
- [`frontend/src/app/components/sidebar/sidebar.component.css`](file:///d:/ELMS/frontend/src/app/components/sidebar/sidebar.component.css)

## Glassmorphism UI & Role-Based Navigation concept
**Modern Glassmorphism & Conditional Navigation**: Uses CSS `backdrop-filter: blur(...)` and semi-transparent layers for high visual quality. The sidebar uses Angular `*ngIf="role === 'HR_ADMIN'"` directives to display administration menus conditionally.

## Viva explanation
> "In Commit 19, I built the Angular design system and layout shell components. I created a dark-mode glassmorphism design system in styles.css and implemented Navbar and Sidebar components with dynamic role-based navigation menus."

---

# Commit 20 — feat: implement angular authentication service and login component

## What I built
- Created `AuthService` with RxJS `BehaviorSubject` for user state management, local storage persistence, and authentication status.
- Implemented `jwtInterceptor` automatically attaching Bearer JWT token and `X-User-Id` headers to outgoing HTTP requests.
- Implemented `authGuard` protecting routes and enforcing role permissions.
- Built standalone `LoginComponent` with glassmorphism UI card, form validation, and instant quick-login demo buttons for all 4 roles.

## Why this feature is needed
Enables users to sign in, stores session tokens, protects restricted Angular client routes, and automatically authorizes REST API calls.

## Files created / modified
- [`frontend/src/app/services/auth.service.ts`](file:///d:/ELMS/frontend/src/app/services/auth.service.ts)
- [`frontend/src/app/interceptors/jwt.interceptor.ts`](file:///d:/ELMS/frontend/src/app/interceptors/jwt.interceptor.ts)
- [`frontend/src/app/guards/auth.guard.ts`](file:///d:/ELMS/frontend/src/app/guards/auth.guard.ts)
- [`frontend/src/app/components/login/login.component.ts`](file:///d:/ELMS/frontend/src/app/components/login/login.component.ts)
- [`frontend/src/app/components/login/login.component.html`](file:///d:/ELMS/frontend/src/app/components/login/login.component.html)
- [`frontend/src/app/components/login/login.component.css`](file:///d:/ELMS/frontend/src/app/components/login/login.component.css)
- [`frontend/src/app/app.config.ts`](file:///d:/ELMS/frontend/src/app/app.config.ts)

## RxJS BehaviorSubject & HTTP Interceptor concept
**Reactive Auth State & HTTP Request Interception**: `BehaviorSubject` emits current user state across all components. `jwtInterceptor` clones outgoing HTTP requests to append `Authorization: Bearer <token>` seamlessly.

## Viva explanation
> "In Commit 20, I built the Angular authentication architecture. I created AuthService with RxJS BehaviorSubject for reactive session tracking, a functional jwtInterceptor to attach Bearer tokens to REST calls, an authGuard for route protection, and a glassmorphism LoginComponent with quick-login demo buttons."

---

# Commit 21 — feat: implement employee dashboard component

## What I built
- Created `LeaveBalanceService`, `LeaveRequestService`, `HolidayService`, and `LeaveTypeService` frontend RxJS API client services.
- Built standalone `DashboardComponent` rendering user welcome banner, annual leave balance cards with visual progress bars, recent leave applications summary table, and upcoming public holidays widget.
- Configured application routes (`/login`, `/dashboard`) and main `AppComponent` layout shell.
- Adjusted Angular budget configurations in `angular.json`.

## Why this feature is needed
Provides employees with a central, interactive dashboard to monitor their leave balances, upcoming holidays, and recent leave application statuses.

## Files created / modified
- [`frontend/src/app/services/leave-balance.service.ts`](file:///d:/ELMS/frontend/src/app/services/leave-balance.service.ts)
- [`frontend/src/app/services/leave-request.service.ts`](file:///d:/ELMS/frontend/src/app/services/leave-request.service.ts)
- [`frontend/src/app/services/holiday.service.ts`](file:///d:/ELMS/frontend/src/app/services/holiday.service.ts)
- [`frontend/src/app/services/leave-type.service.ts`](file:///d:/ELMS/frontend/src/app/services/leave-type.service.ts)
- [`frontend/src/app/components/dashboard/dashboard.component.ts`](file:///d:/ELMS/frontend/src/app/components/dashboard/dashboard.component.ts)
- [`frontend/src/app/components/dashboard/dashboard.component.html`](file:///d:/ELMS/frontend/src/app/components/dashboard/dashboard.component.html)
- [`frontend/src/app/components/dashboard/dashboard.component.css`](file:///d:/ELMS/frontend/src/app/components/dashboard/dashboard.component.css)
- [`frontend/src/app/app.routes.ts`](file:///d:/ELMS/frontend/src/app/app.routes.ts)
- [`frontend/src/app/app.component.ts`](file:///d:/ELMS/frontend/src/app/app.component.ts)
- [`frontend/src/app/app.component.html`](file:///d:/ELMS/frontend/src/app/app.component.html)
- [`frontend/src/app/app.component.css`](file:///d:/ELMS/frontend/src/app/app.component.css)
- [`frontend/angular.json`](file:///d:/ELMS/frontend/angular.json)

## RxJS Observables & Reactive UI Cards concept
**Reactive Dashboard Integration**: DashboardComponent calls `leaveBalanceService.getMyLeaveBalances()` and `holidayService.getUpcomingHolidays()` asynchronously, calculating percentage utilization and updating progress bar widths dynamically.

## Viva explanation
> "In Commit 21, I built the Employee Dashboard component and RxJS API services. The dashboard presents leave quota cards with visual progress bars, recent leave request status badges, and upcoming company public holidays."

---

# Commit 22 — feat: implement leave application form component

## What I built
- Built standalone `ApplyLeaveComponent` featuring interactive leave category select, start & end date pickers, reason input, and medical attachment file picker.
- Implemented real-time client-side Working Day Calculation engine (**Rule 1**) excluding Saturdays, Sundays, and public holidays automatically as dates change.
- Integrated file upload integration with `AttachmentService` (`POST /api/leaves/upload-attachment`).
- Configured `/apply-leave` route in `app.routes.ts`.

## Why this feature is needed
Enables employees to apply for leave with dynamic working day calculations, instant quota feedback, and optional medical attachment uploads.

## Files created / modified
- [`frontend/src/app/components/apply-leave/apply-leave.component.ts`](file:///d:/ELMS/frontend/src/app/components/apply-leave/apply-leave.component.ts)
- [`frontend/src/app/components/apply-leave/apply-leave.component.html`](file:///d:/ELMS/frontend/src/app/components/apply-leave/apply-leave.component.html)
- [`frontend/src/app/components/apply-leave/apply-leave.component.css`](file:///d:/ELMS/frontend/src/app/components/apply-leave/apply-leave.component.css)
- [`frontend/src/app/app.routes.ts`](file:///d:/ELMS/frontend/src/app/app.routes.ts)

## Client-Side Business Rule Engine concept
**Dynamic Working Day Engine**: `recalculateWorkingDays()` iterates the date range in memory, cross-referencing public holiday dates returned by `HolidayService` to present net requested working days in real time before form submission.

## Viva explanation
> "In Commit 22, I built the ApplyLeaveComponent with real-time working day calculations and file upload capabilities. As the employee selects dates, the component dynamically filters weekends and public holidays, displaying net working days prior to API submission."

---

# Commit 23 — feat: implement my leave requests history component

## What I built
- Built standalone `MyLeavesComponent` displaying a filterable history table of personal leave applications.
- Added status filter buttons (`ALL`, `PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`).
- Added action button for canceling pending leave requests (`PUT /api/leaves/{id}/cancel`).
- Integrated medical attachment download links.
- Configured `/my-leaves` route in `app.routes.ts`.

## Why this feature is needed
Enables employees to review their complete application history, check manager decision comments, download medical certificates, and cancel pending applications.

## Files created / modified
- [`frontend/src/app/components/my-leaves/my-leaves.component.ts`](file:///d:/ELMS/frontend/src/app/components/my-leaves/my-leaves.component.ts)
- [`frontend/src/app/components/my-leaves/my-leaves.component.html`](file:///d:/ELMS/frontend/src/app/components/my-leaves/my-leaves.component.html)
- [`frontend/src/app/components/my-leaves/my-leaves.component.css`](file:///d:/ELMS/frontend/src/app/components/my-leaves/my-leaves.component.css)
- [`frontend/src/app/app.routes.ts`](file:///d:/ELMS/frontend/src/app/app.routes.ts)

## Single Page History Filtering concept
**Client-Side In-Memory Filtering**: `applyFilter(filter)` filters the in-memory array of requests without issuing extra network calls, providing instant UI response when clicking status badges.

## Viva explanation
> "In Commit 23, I built the MyLeavesComponent for personal leave history management. It includes status filter pills, manager decision comments, attachment download links, and a cancel action for pending requests."

---

# Commit 24 — feat: implement manager pending approvals component

## What I built
- Built standalone `PendingApprovalsComponent` for line managers to review leave applications submitted by direct report team members.
- Created decision modal dialog for submitting feedback comments and approving (`PUT /api/leaves/{id}/approve`) or rejecting (`PUT /api/leaves/{id}/reject`) applications.
- Configured `/pending-approvals` route in `app.routes.ts`.

## Why this feature is needed
Enables managers to process pending leave applications, enforce project workload constraints, and submit decision comments.

## Files created / modified
- [`frontend/src/app/components/pending-approvals/pending-approvals.component.ts`](file:///d:/ELMS/frontend/src/app/components/pending-approvals/pending-approvals.component.ts)
- [`frontend/src/app/components/pending-approvals/pending-approvals.component.html`](file:///d:/ELMS/frontend/src/app/components/pending-approvals/pending-approvals.component.html)
- [`frontend/src/app/components/pending-approvals/pending-approvals.component.css`](file:///d:/ELMS/frontend/src/app/components/pending-approvals/pending-approvals.component.css)
- [`frontend/src/app/app.routes.ts`](file:///d:/ELMS/frontend/src/app/app.routes.ts)

## Manager Decision Workflow concept
**Modal Dialog & State Machine Enforcement**: Clicking "Review & Decide" opens a glassmorphism modal where managers input feedback. Submitting the decision invokes `approveLeaveRequest` or `rejectLeaveRequest`, triggering backend state transitions and balance updates.

## Viva explanation
> "In Commit 24, I built the PendingApprovalsComponent for line managers. Managers can view pending applications from direct report team members, open a decision modal to write feedback comments, and approve or reject applications."

---

# Commit 25 — feat: implement hr admin views

## What I built
- Built standalone `AdminUsersComponent` for HR Admin user account management, role assignment, and manual leave balance quota adjustments.
- Built standalone `AdminLeaveTypesComponent` for leave category policy configuration and public holiday registrations.
- Configured `/admin/users` and `/admin/leave-types` routes in `app.routes.ts`.

## Why this feature is needed
Provides HR Administrators with complete visual control over system users, manager assignments, yearly quota overrides, leave category policies, and public holiday calendars.

## Files created / modified
- [`frontend/src/app/components/admin-users/admin-users.component.ts`](file:///d:/ELMS/frontend/src/app/components/admin-users/admin-users.component.ts)
- [`frontend/src/app/components/admin-users/admin-users.component.html`](file:///d:/ELMS/frontend/src/app/components/admin-users/admin-users.component.html)
- [`frontend/src/app/components/admin-users/admin-users.component.css`](file:///d:/ELMS/frontend/src/app/components/admin-users/admin-users.component.css)
- [`frontend/src/app/components/admin-leave-types/admin-leave-types.component.ts`](file:///d:/ELMS/frontend/src/app/components/admin-leave-types/admin-leave-types.component.html)
- [`frontend/src/app/components/admin-leave-types/admin-leave-types.component.html`](file:///d:/ELMS/frontend/src/app/components/admin-leave-types/admin-leave-types.component.html)
- [`frontend/src/app/components/admin-leave-types/admin-leave-types.component.css`](file:///d:/ELMS/frontend/src/app/components/admin-leave-types/admin-leave-types.component.css)
- [`frontend/src/app/app.routes.ts`](file:///d:/ELMS/frontend/src/app/app.routes.ts)

## Administrative Control UI concept
**HR Admin Control Panel**: Provides complete administrative CRUD capabilities over users, manager assignments, quota overrides, soft-deletable leave categories, and public holiday calendars.

## Viva explanation
> "In Commit 25, I built the HR Admin views (AdminUsersComponent and AdminLeaveTypesComponent). These components allow HR Administrators to manage employee accounts, assign line managers, override leave quotas, configure leave category policies, and register company public holidays."

---

# Commit 26 — test: write comprehensive end to end integration tests

## What I built
- Implemented `IntegrationTest` in `backend/src/test/java/com/elms/integration/IntegrationTest.java`.
- Configured end-to-end integration tests using Spring Boot `MockMvc` testing framework.
- Verified the complete full-stack flow:
  1. Employee sign-in & JWT token acquisition (`POST /api/auth/login`).
  2. Initial quota retrieval (`GET /api/leave-balances/my`).
  3. Leave application creation (`POST /api/leaves`).
  4. Overlap error validation (**Rule 2**).
  5. Manager authentication & pending queue retrieval (`GET /api/leaves/pending`).
  6. Manager approval execution (**Rule 6**).
  7. Automatic leave balance deduction verification (**Rule 7**).

## Why this feature is needed
Automated integration testing validates that all system components (Security, Controllers, Services, Business Rule Engine, Repositories, Database) operate together seamlessly without regressions.

## Files created
- [`backend/src/test/java/com/elms/integration/IntegrationTest.java`](file:///d:/ELMS/backend/src/test/java/com/elms/integration/IntegrationTest.java)

## Integration Testing & MockMvc concept
**End-to-End REST Simulation**: `MockMvc` executes HTTP requests through the full Spring Security filter chain and Controller dispatchers, validating REST contracts and status codes against a live test context.

## Viva explanation
> "In Commit 26, I wrote comprehensive end-to-end integration tests using Spring Boot MockMvc. The test suite exercises the complete lifecycle from user login and leave submission to overlap prevention, manager approval, and automatic balance deduction."
