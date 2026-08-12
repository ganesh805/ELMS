package com.elms.service;

import com.elms.dto.request.LoginRequestDTO;
import com.elms.dto.response.JwtResponseDTO;
import com.elms.dto.response.UserDTO;
import com.elms.entity.User;
import com.elms.exception.BusinessRuleException;
import com.elms.mapper.EntityMapper;
import com.elms.repository.UserRepository;
import com.elms.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public JwtResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessRuleException("Invalid email or password"));

        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword())
                || request.getPassword().equals(user.getPassword());

        if (!matches) {
            throw new BusinessRuleException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user);
        UserDTO userDTO = EntityMapper.toUserDTO(user);

        return JwtResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .user(userDTO)
                .build();
    }
}
