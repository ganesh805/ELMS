# ELMS REST API Specification

All REST API endpoints are prefixed with `/api`. Request and response bodies are formatted as JSON (`Content-Type: application/json`). Authenticated endpoints require the HTTP header `Authorization: Bearer <jwt_token>` and `X-User-Id: <user_id>`.

---

## 1. Authentication Endpoints

### `POST /api/auth/login`
Validates user credentials and returns signed JWT token.
- **Request Body**:
  ```json
  {
    "email": "admin@elms.com",
    "password": "admin123"
  }
  ```
- **Response (200 OK)**:
  ```json
  {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "fullName": "HR Admin",
      "email": "admin@elms.com",
      "role": "HR_ADMIN",
      "department": "Human Resources"
    }
  }
  ```

---

## 2. Leave Request Endpoints (Employee / Manager)

### `POST /api/leaves`
Submits a new leave request with dynamic working day calculation and validation.
- **Request Headers**: `Authorization: Bearer <token>`, `X-User-Id: <user_id>`
- **Request Body**:
  ```json
  {
    "leaveTypeId": 1,
    "startDate": "2026-09-01",
    "endDate": "2026-09-05",
    "reason": "Family trip",
    "attachmentFileName": null
  }
  ```
- **Response (201 Created)**:
  ```json
  {
    "id": 10,
    "userId": 2,
    "userName": "John Employee",
    "leaveTypeId": 1,
    "leaveTypeName": "Annual Leave",
    "startDate": "2026-09-01",
    "endDate": "2026-09-05",
    "numberOfDays": 5,
    "reason": "Family trip",
    "status": "PENDING"
  }
  ```

### `GET /api/leaves/my`
Retrieves personal leave application history for the logged-in employee.

### `PUT /api/leaves/{id}/cancel`
Cancels a pending leave application.

### `GET /api/leaves/pending` (Manager)
Retrieves pending leave requests submitted by direct report team members.

### `PUT /api/leaves/{id}/approve` (Manager)
Approves a pending leave request, deducting remaining balance automatically (**Rule 7**).

### `PUT /api/leaves/{id}/reject` (Manager)
Rejects a pending leave request with a decision comment.

---

## 3. Leave Balances, Types & Holidays

### `GET /api/leave-balances/my`
Retrieves personal annual leave quotas and remaining balances.

### `GET /api/leave-types`
Retrieves active company leave categories.

### `GET /api/holidays`
Retrieves company public holidays.

---

## 4. HR Administrative Management (`/api/admin/**`)

### `GET /api/admin/users`
Retrieves all user accounts.

### `POST /api/admin/users`
Creates a new employee or manager user account and auto-initializes yearly leave quotas.

### `PUT /api/admin/users/{id}`
Updates user details, role, department, or assigned line manager.

### `POST /api/admin/leave-balances/adjust`
Manually adjusts an employee's annual leave quota.

### `POST /api/admin/leave-types`
Creates a new leave category.

### `DELETE /api/admin/leave-types/{id}`
Soft-deletes/deactivates a leave category (`active = false`).

### `POST /api/admin/holidays`
Registers a new company public holiday.

### `DELETE /api/admin/holidays/{id}`
Deletes a company public holiday.
