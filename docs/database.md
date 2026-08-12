# ELMS Database Schema & Data Dictionary

Database: **MySQL 8.4 LTS** (`elms_db`)

---

## 1. Entity Relationship Diagram (ERD)

```
 +------------------+           +------------------+
 |      users       |1        * |  leave_balances  |
 +------------------+-----------+------------------+
 | id (PK)          |           | id (PK)          |
 | full_name        |           | user_id (FK)     |
 | email (UNIQUE)   |           | leave_type_id(FK)|
 | password         |           | year             |
 | role             |           | allocated        |
 | department       |           | used             |
 | manager_id (FK)  |--+        | remaining        |
 +------------------+  |        +------------------+
        | 1            | (Self Ref)
        |              |
        | *            v
 +------------------+  |
 |  leave_requests  |--+
 +------------------+
 | id (PK)          |
 | user_id (FK)     |           +------------------+
 | leave_type_id(FK)|*         1|   leave_types    |
 | start_date       |-----------+------------------+
 | end_date         |           | id (PK)          |
 | number_of_days   |           | name (UNIQUE)    |
 | status           |           | default_quota    |
 | approver_id (FK) |           | active           |
 +------------------+           +------------------+

 +------------------+
 |     holidays     |
 +------------------+
 | id (PK)          |
 | date (UNIQUE)    |
 | name             |
 | description      |
 +------------------+
```

---

## 2. Table Schemas

### Table: `users`
- `id` (BIGINT, Primary Key, Auto Increment)
- `full_name` (VARCHAR 255, NOT NULL)
- `email` (VARCHAR 255, UNIQUE, NOT NULL)
- `password` (VARCHAR 255, NOT NULL - BCrypt encoded)
- `role` (ENUM: `'EMPLOYEE'`, `'MANAGER'`, `'HR_ADMIN'`, NOT NULL)
- `department` (VARCHAR 255)
- `date_of_joining` (DATE)
- `manager_id` (BIGINT, Foreign Key referencing `users(id)`, NULLable)

### Table: `leave_types`
- `id` (BIGINT, Primary Key, Auto Increment)
- `name` (VARCHAR 255, UNIQUE, NOT NULL)
- `default_annual_quota` (INT, NOT NULL)
- `description` (TEXT)
- `active` (BOOLEAN, DEFAULT TRUE)
- `requires_approval` (BOOLEAN, DEFAULT TRUE)

### Table: `leave_balances`
- `id` (BIGINT, Primary Key, Auto Increment)
- `user_id` (BIGINT, Foreign Key referencing `users(id)`, NOT NULL)
- `leave_type_id` (BIGINT, Foreign Key referencing `leave_types(id)`, NOT NULL)
- `year` (INT, NOT NULL)
- `allocated` (INT, NOT NULL)
- `used` (INT, DEFAULT 0)
- `remaining` (INT, NOT NULL)
- *Composite Unique Constraint*: `(user_id, leave_type_id, year)`

### Table: `leave_requests`
- `id` (BIGINT, Primary Key, Auto Increment)
- `user_id` (BIGINT, Foreign Key referencing `users(id)`, NOT NULL)
- `leave_type_id` (BIGINT, Foreign Key referencing `leave_types(id)`, NOT NULL)
- `start_date` (DATE, NOT NULL)
- `end_date` (DATE, NOT NULL)
- `number_of_days` (INT, NOT NULL)
- `reason` (TEXT, NOT NULL)
- `status` (ENUM: `'PENDING'`, `'APPROVED'`, `'REJECTED'`, `'CANCELLED'`, NOT NULL)
- `attachment_file_name` (VARCHAR 255)
- `applied_on` (DATETIME, NOT NULL)
- `approver_id` (BIGINT, Foreign Key referencing `users(id)`)
- `decision_comment` (TEXT)
- `decision_date` (DATETIME)

### Table: `holidays`
- `id` (BIGINT, Primary Key, Auto Increment)
- `date` (DATE, UNIQUE, NOT NULL)
- `name` (VARCHAR 255, NOT NULL)
- `description` (TEXT)
