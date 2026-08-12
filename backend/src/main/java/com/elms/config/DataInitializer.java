package com.elms.config;

import com.elms.entity.*;
import com.elms.entity.enums.LeaveStatus;
import com.elms.entity.enums.Role;
import com.elms.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final HolidayRepository holidayRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Seed data already present. Skipping initialization.");
            return;
        }

        log.info("Seeding initial ELMS data with BCrypt password hashing...");

        User admin = userRepository.save(User.builder()
                .fullName("HR Admin")
                .email("admin@elms.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.HR_ADMIN)
                .department("Human Resources")
                .dateOfJoining(LocalDate.of(2022, 1, 15))
                .build());

        User manager = userRepository.save(User.builder()
                .fullName("Sarah Jenkins")
                .email("manager@elms.com")
                .password(passwordEncoder.encode("manager123"))
                .role(Role.MANAGER)
                .department("Engineering")
                .dateOfJoining(LocalDate.of(2023, 3, 1))
                .build());

        User employee1 = userRepository.save(User.builder()
                .fullName("John Doe")
                .email("employee1@elms.com")
                .password(passwordEncoder.encode("employee123"))
                .role(Role.EMPLOYEE)
                .department("Engineering")
                .dateOfJoining(LocalDate.of(2024, 6, 10))
                .manager(manager)
                .build());

        User employee2 = userRepository.save(User.builder()
                .fullName("Alice Smith")
                .email("employee2@elms.com")
                .password(passwordEncoder.encode("employee123"))
                .role(Role.EMPLOYEE)
                .department("Engineering")
                .dateOfJoining(LocalDate.of(2024, 8, 1))
                .manager(manager)
                .build());

        LeaveType annualLeave = leaveTypeRepository.save(LeaveType.builder()
                .name("Annual Leave")
                .defaultAnnualQuota(18)
                .description("Paid annual leave quota for vacation and rest")
                .active(true)
                .requiresApproval(true)
                .build());

        LeaveType sickLeave = leaveTypeRepository.save(LeaveType.builder()
                .name("Sick Leave")
                .defaultAnnualQuota(12)
                .description("Medical and health leave")
                .active(true)
                .requiresApproval(true)
                .build());

        LeaveType casualLeave = leaveTypeRepository.save(LeaveType.builder()
                .name("Casual Leave")
                .defaultAnnualQuota(6)
                .description("Urgent short personal leave")
                .active(true)
                .requiresApproval(true)
                .build());

        holidayRepository.save(Holiday.builder()
                .date(LocalDate.of(2026, 1, 1))
                .name("New Year's Day")
                .description("Public holiday")
                .build());

        holidayRepository.save(Holiday.builder()
                .date(LocalDate.of(2026, 8, 15))
                .name("Independence Day")
                .description("National holiday")
                .build());

        int currentYear = 2026;
        List<User> employeesToSeed = List.of(manager, employee1, employee2);
        List<LeaveType> leaveTypes = List.of(annualLeave, sickLeave, casualLeave);

        for (User user : employeesToSeed) {
            for (LeaveType type : leaveTypes) {
                leaveBalanceRepository.save(LeaveBalance.builder()
                        .user(user)
                        .leaveType(type)
                        .year(currentYear)
                        .allocated(type.getDefaultAnnualQuota())
                        .used(0)
                        .remaining(type.getDefaultAnnualQuota())
                        .build());
            }
        }

        LeaveBalance emp1AnnualBalance = leaveBalanceRepository
                .findByUserIdAndLeaveTypeIdAndYear(employee1.getId(), annualLeave.getId(), currentYear)
                .orElseThrow();
        emp1AnnualBalance.setUsed(3);
        emp1AnnualBalance.setRemaining(15);
        leaveBalanceRepository.save(emp1AnnualBalance);

        leaveRequestRepository.save(LeaveRequest.builder()
                .user(employee1)
                .leaveType(annualLeave)
                .startDate(LocalDate.of(2026, 4, 10))
                .endDate(LocalDate.of(2026, 4, 14))
                .numberOfDays(3)
                .reason("Vacation trip with family")
                .status(LeaveStatus.APPROVED)
                .appliedOn(LocalDateTime.of(2026, 3, 20, 10, 0))
                .approver(manager)
                .decisionComment("Approved. Enjoy your vacation.")
                .decisionDate(LocalDateTime.of(2026, 3, 21, 14, 30))
                .build());

        leaveRequestRepository.save(LeaveRequest.builder()
                .user(employee2)
                .leaveType(sickLeave)
                .startDate(LocalDate.of(2026, 9, 1))
                .endDate(LocalDate.of(2026, 9, 2))
                .numberOfDays(2)
                .reason("Dental treatment appointment")
                .status(LeaveStatus.PENDING)
                .appliedOn(LocalDateTime.now())
                .build());

        leaveRequestRepository.save(LeaveRequest.builder()
                .user(employee1)
                .leaveType(casualLeave)
                .startDate(LocalDate.of(2026, 5, 5))
                .endDate(LocalDate.of(2026, 5, 5))
                .numberOfDays(1)
                .reason("Personal work")
                .status(LeaveStatus.REJECTED)
                .appliedOn(LocalDateTime.of(2026, 4, 28, 9, 15))
                .approver(manager)
                .decisionComment("High project workload on this date. Please reschedule.")
                .decisionDate(LocalDateTime.of(2026, 4, 29, 11, 0))
                .build());

        log.info("ELMS seed data created successfully!");
    }
}
