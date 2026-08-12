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
- Implemented `DataInitializer` bean to populate initial database seed records on application startup.
- Created sample seed users (HR Admin, Manager, 2 Employees), 3 leave types, public holidays, annual balances for 2026, and sample leave requests (`APPROVED`, `PENDING`, `REJECTED`).

## Why this feature is needed
Repositories abstract SQL queries into clean Java methods. Seed data ensures developer and evaluator environments have realistic test data ready immediately after cloning.

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
> "In Commit 04, I created Spring Data JPA repositories and a DataInitializer startup component. This automatically populates seed accounts, leave categories, public holidays, yearly quotas, and sample requests so the application works out of the box."

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
