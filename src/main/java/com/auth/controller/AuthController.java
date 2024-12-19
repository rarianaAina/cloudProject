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
    @Operation(summary = "Inscription d'un utilisateur")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("Votre insription est prise en compte et un mail de validation vous a été envoyée.");
    }

    @PostMapping("/login")
    @Operation(summary = "Authentification de l'utilisateur")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        authService.loginFirst(request);
        return ResponseEntity.ok("Un code PIN a été envoyé à votre adresse email pour confirmer votre connexion");
    }

/*    @PostMapping("/loginConfirm/{code}")
    @Operation(summary = "Confirmation de login")
    public ResponseEntity<String> loginConfirm(@Valid @RequestBody LoginRequest request, @PathVariable String code) {
        authService.loginConfirm(request, code);
        return ResponseEntity.ok("Vous êtes bien logués");
    }*/

    @GetMapping("/verify-email/{token}")
    @Operation(summary = "Vérification de l'adresse email pour confirmer l'inscription")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    @PostMapping("/verify-2fa/{email}/{code}")
    @Operation(summary = "Vérification et Utilisation du code PIN")
    public ResponseEntity<String> verify2FA(@PathVariable String email, @PathVariable String code) {
        authService.verify2FA(email, code);
        return ResponseEntity.ok("La vérification en deux facteurs est un succès");
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
    @Operation(summary = "Mise à jour des informations d'un utilisateur")
    public ResponseEntity<String> updateUser(@RequestParam String email, @Valid @RequestBody UserUpdateRequest request) {
        authService.updateUser(email, request);
        return ResponseEntity.ok("Vos informations ont été mises à jour correctement");
    }

}
