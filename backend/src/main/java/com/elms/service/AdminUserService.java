package com.elms.service;

import com.elms.dto.request.UserCreateDTO;
import com.elms.dto.request.UserUpdateDTO;
import com.elms.dto.response.UserDTO;
import com.elms.entity.LeaveBalance;
import com.elms.entity.LeaveType;
import com.elms.entity.User;
import com.elms.exception.BusinessRuleException;
import com.elms.exception.ResourceNotFoundException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.LeaveBalanceRepository;
import com.elms.repository.LeaveTypeRepository;
import com.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(EntityMapper::toUserDTO)
                .toList();
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("User with email " + dto.getEmail() + " already exists");
        }

        User manager = null;
        if (dto.getManagerId() != null) {
            manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.getManagerId()));
        }

        String rawPassword = (dto.getPassword() != null && !dto.getPassword().isBlank()) ? dto.getPassword() : "password123";

        User newUser = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(rawPassword))
                .role(dto.getRole())
                .department(dto.getDepartment())
                .dateOfJoining(dto.getDateOfJoining() != null ? dto.getDateOfJoining() : LocalDate.now())
                .manager(manager)
                .build();

        User savedUser = userRepository.save(newUser);

        // Auto-initialize 2026 leave balances for the newly created user
        int currentYear = LocalDate.now().getYear();
        List<LeaveType> activeLeaveTypes = leaveTypeRepository.findByActiveTrue();
        for (LeaveType leaveType : activeLeaveTypes) {
            leaveBalanceRepository.save(LeaveBalance.builder()
                    .user(savedUser)
                    .leaveType(leaveType)
                    .year(currentYear)
                    .allocated(leaveType.getDefaultAnnualQuota())
                    .used(0)
                    .remaining(leaveType.getDefaultAnnualQuota())
                    .build());
        }

        return EntityMapper.toUserDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(Long userId, UserUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getDepartment() != null && !dto.getDepartment().isBlank()) {
            user.setDepartment(dto.getDepartment());
        }
        if (dto.getManagerId() != null) {
            User manager = userRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + dto.getManagerId()));
            user.setManager(manager);
        }

        User updatedUser = userRepository.save(user);
        return EntityMapper.toUserDTO(updatedUser);
    }
}
