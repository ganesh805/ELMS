# ELMS Core Business Rules & Validation Engine

The Employee Leave Management System enforces 7 core business rules across all workflows.

---

## Rule 1: Net Working Days Calculation
- **Requirement**: Leave duration must equal net working days between `startDate` and `endDate`.
- **Exclusions**:
  - Saturdays (`DayOfWeek.SATURDAY`)
  - Sundays (`DayOfWeek.SUNDAY`)
  - Company Public Holidays registered in `holidays` database table.
- **Implementation**: `WorkingDayService.calculateWorkingDays(LocalDate start, LocalDate end)`

---

## Rule 2: Leave Overlap Prevention
- **Requirement**: An employee cannot submit a leave request if the requested date interval overlaps with any existing `PENDING` or `APPROVED` leave request for that user.
- **Mathematical Condition**: Two date intervals `[S1, E1]` and `[S2, E2]` overlap if and only if:
  $$\text{Overlap} \iff (S1 \le E2) \land (E1 \ge S2)$$
- **Exception**: Throws `BusinessRuleException` with HTTP 400 Bad Request.

---

## Rule 3: Leave Quota & Balance Validation
- **Requirement**: Requested working days cannot exceed the employee's remaining quota for that leave category and year.
- **Validation**: `requestedWorkingDays <= balance.getRemaining()`
- **Exception**: Throws `InsufficientLeaveBalanceException` with HTTP 400 Bad Request.

---

## Rule 4: Date Range Order Integrity
- **Requirement**: `endDate` must be greater than or equal to `startDate` (`endDate >= startDate`).

---

## Rule 5: Future / Present Date Constraint
- **Requirement**: `startDate` cannot be in the past (`startDate >= LocalDate.now()`).

---

## Rule 6: State Machine Transition Integrity
- **Requirement**: State transitions are strictly valid only from `PENDING` state:
  - `PENDING` $\rightarrow$ `APPROVED` (Manager action)
  - `PENDING` $\rightarrow$ `REJECTED` (Manager action)
  - `PENDING` $\rightarrow$ `CANCELLED` (Employee action)
  - `APPROVED` $\rightarrow$ `CANCELLED` (HR Admin revocation only)
- **Constraint**: Re-evaluating or modifying an already `APPROVED` or `REJECTED` request throws `InvalidLeaveStateException`.

---

## Rule 7: Automatic Balance Deduction & Re-crediting
- **Requirement**:
  - Approving a request automatically deducts `numberOfDays` from `remaining` and increments `used`.
  - Revoking an `APPROVED` request automatically re-credits `numberOfDays` back to `remaining` and decrements `used`.
