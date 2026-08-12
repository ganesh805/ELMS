package com.elms.service;

import com.elms.dto.request.UserCreateDTO;
import com.elms.dto.request.UserUpdateDTO;
import com.elms.dto.response.UserDTO;
import com.elms.entity.User;
import com.elms.exception.BusinessRuleException;
import com.elms.exception.ResourceNotFoundException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

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

        User newUser = User.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(dto.getRole())
                .department(dto.getDepartment())
                .dateOfJoining(dto.getDateOfJoining() != null ? dto.getDateOfJoining() : LocalDate.now())
                .manager(manager)
                .build();

        User savedUser = userRepository.save(newUser);
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
