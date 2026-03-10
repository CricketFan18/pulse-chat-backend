package com.app.ChatApplication.controller;

import com.app.ChatApplication.dto.AuthResponse;
import com.app.ChatApplication.dto.LoginRequest;
import com.app.ChatApplication.dto.RegisterRequest;
import com.app.ChatApplication.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request)
    {
        userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.loginUser(request);
            // Equivalent to res.status(200).json(response)
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            // Equivalent to res.status(401).send()
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
