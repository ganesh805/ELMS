package com.elms.service;

import com.elms.dto.request.LoginRequestDTO;
import com.elms.dto.response.UserDTO;
import com.elms.entity.User;
import com.elms.exception.BusinessRuleException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Invalid email or password"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new BusinessRuleException("Invalid email or password");
        }

        return EntityMapper.toUserDTO(user);
    }
}
