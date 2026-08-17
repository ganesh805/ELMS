package com.elms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RootApiController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getApiRootStatus() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "Vista Tech Employee Leave Management System (ELMS) REST API",
                "version", "1.0.0",
                "swaggerUi", "/swagger-ui/index.html",
                "documentation", "/docs"
        ));
    }
}
