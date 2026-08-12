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
> "In Commit 06, I built endpoints for retrieving leave categories, company holidays, and personal leave balances. I extracted user context via the `X-User-Id` request header and mapped entities to response DTOs using `@Transactional(readOnly = true)` service methods."
