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

## Mini modification exercise solution & explanation
- **Property added**: `elms.app.environment=development` in `application.properties`
- **Class created**: `com.elms.config.AppInfoLogger` implementing `CommandLineRunner` with `@Component` and `@Value("${elms.app.environment}")`.
- **How it works**: When Spring Boot starts, component scanning finds `AppInfoLogger`, creates it as a Spring Bean, injects the `elms.app.environment` value, and executes `run(...)` automatically after context initialization.
