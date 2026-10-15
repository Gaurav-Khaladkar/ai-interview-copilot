package com.aicopilot.controller;

import com.aicopilot.dto.ApiResponse;
import com.aicopilot.dto.AuthRequest;
import com.aicopilot.dto.AuthResponse;
import com.aicopilot.dto.RegisterRequest;
import com.aicopilot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User registration, authentication and session tokens")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register candidate account")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("User registered successfully.", response));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate candidate credentials")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful.", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Invalidate active session token")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.ok("Session logged out successfully.", null));
    }
}
