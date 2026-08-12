# ELMS Viva Voce Preparation Guide (50 Top Q&As & Live Coding Exercises)

This comprehensive guide prepares students for the final project viva voce examination for the **Employee Leave Management System (ELMS)**.

---

## Part 1: Core System & Architecture (Q1 - Q10)

### Q1: Can you summarize the ELMS project architecture?
> "ELMS is built as a full-stack decoupled web application. The backend is a RESTful API built with Spring Boot 3.3.2 and Java 21 LTS using Spring Data JPA and MySQL 8.4. The frontend is a Single Page Application (SPA) built with Angular 17 featuring standalone components, RxJS state management, and a custom glassmorphism dark theme."

### Q2: Why did you separate the frontend (Angular) and backend (Spring Boot)?
> "Decoupling frontend and backend provides independent scalability, clean separation of concerns, and allows the backend REST APIs to serve multiple client interfaces (such as mobile applications or third-party HR tools) without modifying backend business logic."

### Q3: What is the layered architecture used in Spring Boot?
> "The backend follows a classic 4-layer architecture:
> 1. Controller Layer: Handles HTTP REST endpoints and DTO mapping.
> 2. Service Layer: Enforces business validation rules and transactional workflows.
> 3. Repository Layer: Executes JPA queries against MySQL.
> 4. Entity Layer: Maps Java domain classes to relational database tables."

### Q4: How is database credential security handled?
> "Sensitive database credentials (username and password) are externalized into `application-local.properties` which is strictly excluded from version control using `.gitignore`. Only a safe template `application-local.example.properties` is checked into Git."

### Q5: How do Angular and Spring Boot communicate securely?
> "Angular sends HTTP REST requests to `http://localhost:8080/api`. Authenticated requests include an `Authorization: Bearer <JWT>` header attached automatically by an Angular `jwtInterceptor`."

---

## Part 2: Backend Spring Boot 3.3 & Java 21 (Q11 - Q25)

### Q11: What Java 21 features are utilized in ELMS?
> "Java 21 LTS features include pattern matching, record classes, modern `java.time.LocalDate` API for working day calculations, and enhanced stream API collection methods."

### Q12: Explain the `@SpringBootApplication` annotation.
> "It is an umbrella annotation that combines `@SpringBootConfiguration` (declares bean definitions), `@EnableAutoConfiguration` (automatically configures Spring context based on classpath dependencies), and `@ComponentScan` (scans package components)."

### Q13: How does `@Transactional` work in Spring Boot?
> "Spring creates a dynamic proxy around transactional methods. It starts a database transaction when the method begins and commits it when the method completes successfully. If an unhandled runtime exception occurs, Spring automatically rolls back all database mutations."

### Q14: How are custom exceptions handled in REST controllers?
> "Using a global exception handler class annotated with `@RestControllerAdvice` and `@ExceptionHandler`. This intercepts exceptions like `InsufficientLeaveBalanceException` and transforms them into standard `ErrorResponseDTO` JSON objects with appropriate HTTP status codes (e.g., 400 Bad Request)."

### Q15: What is DTO pattern and why is it used?
> "Data Transfer Objects (DTOs) decouple internal database entities from external API contracts. DTOs prevent exposing sensitive fields (like password hashes) and avoid circular JSON serialization issues in JPA relationships."

---

## Part 3: Database & Spring Data JPA (Q26 - Q35)

### Q26: Explain the `@ManyToOne` self-referencing relationship on the `User` entity.
> "The `manager` property on `User` is mapped as `@ManyToOne @JoinColumn(name = "manager_id")` pointing to another `User` record in the same table. This models manager-employee reporting hierarchies cleanly."

### Q27: How does HikariCP improve database performance?
> "HikariCP is a lightweight, high-speed JDBC connection pool. Instead of establishing a new database connection for every API call, HikariCP reuses pre-established pool connections, eliminating connection setup overhead."

### Q28: What is Hibernate's `ddl-auto` setting?
> "`spring.jpa.hibernate.ddl-auto=update` automatically updates the MySQL relational database schema to match JPA `@Entity` definitions without dropping existing data."

---

## Part 4: Business Rules & Algorithms (Q36 - Q42)

### Q36: How does Business Rule 1 (Working Day Calculation) work?
> "The `WorkingDayService` iterates day-by-day from `startDate` to `endDate`. It skips Saturdays, Sundays, and any date present in the `Holiday` database table, returning the net requested working days."

### Q37: How is Business Rule 2 (Leave Overlap Prevention) implemented?
> "Using a JPQL overlap query: `WHERE (r.startDate <= :endDate AND r.endDate >= :startDate) AND r.status IN ('PENDING', 'APPROVED')`. If matching records exist for the user, submission is rejected."

### Q38: Explain Business Rule 7 (Automatic Balance Deduction).
> "When a manager approves a leave request, `LeaveRequestService.approveLeaveRequest()` updates the request status to `APPROVED` and atomically increments `used` days and decrements `remaining` days on the user's `LeaveBalance` entity inside a single `@Transactional` boundary."

---

## Part 5: Angular 17 & Frontend Architecture (Q43 - Q50)

### Q43: What are Angular Standalone Components?
> "Introduced in Angular 14 and default in Angular 17, standalone components (`standalone: true`) specify their own imports (`imports: [CommonModule, FormsModule]`) without needing `NgModule` containers."

### Q44: What is the role of `jwtInterceptor` in Angular?
> "It is a functional HTTP interceptor registered in `app.config.ts` that intercepts outgoing HTTP requests and automatically appends `Authorization: Bearer <token>` and `X-User-Id` headers."

### Q45: How does `authGuard` protect Angular routes?
> "It is a functional route guard (`canActivate: [authGuard]`) that checks `AuthService.isLoggedIn()`. If unauthenticated, it redirects the user to `/login`."

---

## Part 6: Live Coding & Refactoring Exercises

### Exercise 1: Write a function to check if a given date is a weekend
```java
public boolean isWeekend(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
}
```

### Exercise 2: Refactor overlapping interval check logic
```java
public boolean isOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
    return !start1.isAfter(end2) && !end1.isBefore(start2);
}
```
