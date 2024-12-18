package com.auth.controller;

import com.auth.dto.LoginRequest;
import com.auth.dto.SignupRequest;
import com.auth.dto.UserUpdateRequest;
import com.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("User registered successfully. Please check your email for verification.");
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        authService.login(request);
        return ResponseEntity.ok("2FA code sent to your email.");
    }

    @GetMapping("/verify-email/{token}")
    @Operation(summary = "Verify email address")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/verify-2fa/{email}/{code}")
    @Operation(summary = "Verify 2FA code")
    public ResponseEntity<String> verify2FA(@PathVariable String email, @PathVariable String code) {
        authService.verify2FA(email, code);
        return ResponseEntity.ok("2FA verification successful.");
    }
/*
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    public ResponseEntity<String> resetPasswordFirst(@PathVariable String token, @RequestParam String email) {
        authService.resetPasswordFirst(email);
        return ResponseEntity.ok("Token envoyé par email");
    }

    @PostMapping("/reset-password/{token}")
    @Operation(summary = "Reset password")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestParam String newPassword) {
        authService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successfully.");
    }*/
    @PutMapping("/update")
    @Operation(summary = "Update user information")
    public ResponseEntity<String> updateUser(@RequestParam String email, @Valid @RequestBody UserUpdateRequest request) {
        authService.updateUser(email, request);
        return ResponseEntity.ok("User information updated successfully.");
    }

}
