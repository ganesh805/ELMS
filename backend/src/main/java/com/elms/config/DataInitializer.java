package com.elms.config;

import com.elms.entity.*;
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
            log.info("System initialized. Skipping seed creation.");
            return;
        }

        log.info("Initializing baseline system setup (Default HR Admin & Leave Categories)...");

        // Single baseline HR Admin account for first-time system access
        User admin = userRepository.save(User.builder()
                .fullName("HR Admin")
                .email("admin@elms.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.HR_ADMIN)
                .department("Human Resources")
                .dateOfJoining(LocalDate.of(2025, 1, 1))
                .build());

        // Baseline Leave Categories
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

        // Baseline Company Holidays
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

        // Automatically assign baseline balances for HR Admin
        int currentYear = 2026;
        for (LeaveType type : new LeaveType[]{annualLeave, sickLeave, casualLeave}) {
            leaveBalanceRepository.save(LeaveBalance.builder()
                    .user(admin)
                    .leaveType(type)
                    .year(currentYear)
                    .allocated(type.getDefaultAnnualQuota())
                    .used(0)
                    .remaining(type.getDefaultAnnualQuota())
                    .build());
        }

        log.info("Clean ELMS system baseline initialized successfully!");
    }
}
