package com.elms.controller;

import com.elms.dto.request.LoginRequestDTO;
import com.elms.dto.response.UserDTO;
import com.elms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        UserDTO userDTO = authService.login(request);
        return ResponseEntity.ok(userDTO);
    }
}
