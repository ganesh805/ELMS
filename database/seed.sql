
--ELMS Initial Baseline Seed Data

USE elms_db;

-- 1. Insert Initial HR Admin User (Password: admin123 hashed via BCrypt)
INSERT INTO users (full_name, email, password, role, department, date_of_joining, manager_id)
VALUES ('HR Admin', 'admin@elms.com', '$2a$10$w09u7uR8yM0XwX7y1mKx1.H6j/Y3.m1V/3x4k5l6m7n8o9p0q', 'HR_ADMIN', 'Human Resources', '2025-01-01', NULL)
ON DUPLICATE KEY UPDATE full_name = VALUES(full_name);

-- 2. Insert Baseline Leave Types
INSERT INTO leave_types (name, default_annual_quota, description, active, requires_approval)
VALUES 
('Annual Leave', 18, 'Paid annual leave quota for vacation and rest', TRUE, TRUE),
('Sick Leave', 12, 'Medical and health leave', TRUE, TRUE),
('Casual Leave', 6, 'Urgent short personal leave', TRUE, TRUE)
ON DUPLICATE KEY UPDATE default_annual_quota = VALUES(default_annual_quota);

-- 3. Insert Baseline 2026 Company Public Holidays
INSERT INTO holidays (date, name, description)
VALUES 
('2026-01-01', 'New Year\'s Day', 'Public holiday'),
('2026-08-15', 'Independence Day', 'National holiday')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 4. Initial Leave Balances for HR Admin
INSERT INTO leave_balances (user_id, leave_type_id, year, allocated, used, remaining)
SELECT u.id, lt.id, 2026, lt.default_annual_quota, 0, lt.default_annual_quota
FROM users u
CROSS JOIN leave_types lt
WHERE u.email = 'admin@elms.com'
ON DUPLICATE KEY UPDATE allocated = VALUES(allocated);
