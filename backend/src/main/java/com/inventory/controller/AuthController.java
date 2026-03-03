package com.inventory.controller;

import com.inventory.dto.ApiResponse;
import com.inventory.dto.AuthResponse;
import com.inventory.dto.LoginRequest;
import com.inventory.model.User;
import com.inventory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for authentication operations
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    /**
     * User login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("Login attempt for user: {}", loginRequest.getUsername());
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username or password"));
        }
    }

    /**
     * User registration endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@Valid @RequestBody User user) {
        try {
            log.info("Registration attempt for user: {}", user.getUsername());
            User registeredUser = authService.register(user);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully", registeredUser));
        } catch (IllegalArgumentException e) {
            log.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get current user endpoint
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<User>> getCurrentUser() {
        try {
            User currentUser = authService.getCurrentUser();
            return ResponseEntity.ok(ApiResponse.success(currentUser));
        } catch (Exception e) {
            log.error("Failed to get current user", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("User not found"));
        }
    }
}
