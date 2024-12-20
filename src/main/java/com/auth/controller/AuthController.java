/**
 * AuthController is a REST controller that handles authentication-related
 * operations such as user signup, login, email verification, two-factor
 * authentication (2FA) verification, and user information updates.
 * 
 * This controller provides the following endpoints:
 * 
 * - POST /api/auth/signup: Registers a new user.
 * - POST /api/auth/login: Authenticates a user and sends a PIN code to their email.
 * - GET /api/auth/verify-email/{token}: Verifies the user's email address using a token.
 * - POST /api/auth/verify-2fa/{email}/{code}: Verifies the 2FA PIN code for the user.
 * - PUT /api/auth/update: Updates the user's information.
 * 
 * Note: Some endpoints are commented out and not currently in use.
 * 
 * This controller uses the AuthService to perform the actual authentication
 * operations.
 * 
 * Annotations:
 * - @RestController: Indicates that this class is a REST controller.
 * - @RequestMapping("/api/auth"): Maps HTTP requests to /api/auth to this controller.
 * - @RequiredArgsConstructor: Generates a constructor with required arguments.
 * - @Tag: Adds a tag for Swagger API documentation.
 * 
 * Each endpoint method is annotated with:
 * - @PostMapping, @GetMapping, or @PutMapping: Maps HTTP methods to the endpoint.
 * - @Operation: Provides a summary for Swagger API documentation.
 * - @Valid: Validates the request body.
 * 
 * Dependencies:
 * - AuthService: The service that handles authentication operations.
 */
package com.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.dto.LoginRequest;
import com.auth.dto.SignupRequest;
import com.auth.dto.UserUpdateRequest;
import com.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management API")
public class AuthController {

    private final AuthService authService;

    /**
     * Inscription d'un utilisateur.
     * 
     * @param request contient l'email, le mot de passe, le prénom et le nom de
     *                l'utilisateur.
     * @return une réponse OK avec un message de confirmation.
     */
    @PostMapping("/signup")
    @Operation(summary = "Inscription d'un utilisateur")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok("Votre insription est prise en compte et un mail de validation vous a été envoyée.");
    }

    /**
     * Authentification de l'utilisateur.
     * 
     * @param request contient l'email et le mot de passe de l'utilisateur.
     * @return une réponse OK avec un message de confirmation si l'authentification
     *         réussit.
     */
    @PostMapping("/login")
    @Operation(summary = "Authentification de l'utilisateur")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        authService.loginFirst(request);
        return ResponseEntity.ok("Un code PIN a été envoyé à votre adresse email pour confirmer votre connexion");
    }

    /*
     * @PostMapping("/loginConfirm/{code}")
     * 
     * @Operation(summary = "Confirmation de login")
     * public ResponseEntity<String> loginConfirm(@Valid @RequestBody LoginRequest
     * request, @PathVariable String code) {
     * authService.loginConfirm(request, code);
     * return ResponseEntity.ok("Vous êtes bien logués");
     * }
     */

    /**
     * Vérification de l'adresse email pour confirmer l'inscription.
     * 
     * @param token le token de vérification envoyé par email.
     * @return une réponse OK avec un message de confirmation si la vérification
     *         réussit.
     */
    @GetMapping("/verify-email/{token}")
    @Operation(summary = "Vérification de l'adresse email pour confirmer l'inscription")
    public ResponseEntity<String> verifyEmail(@PathVariable String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully.");
    }

    /**
     * Vérification et Utilisation du code PIN.
     * 
     * @param email l'email de l'utilisateur.
     * @param code  le code PIN envoyé par email.
     * @return une réponse OK avec un message de confirmation si la vérification
     *         réussit.
     */
    @PostMapping("/verify-2fa/{email}/{code}")
    @Operation(summary = "Vérification et Utilisation du code PIN")
    public ResponseEntity<String> verify2FA(@PathVariable String email, @PathVariable String code) {
        authService.verify2FA(email, code);
        return ResponseEntity.ok("La vérification en deux facteurs est un succès");
    }

    /*
     * @PostMapping("/reset-password")
     * 
     * @Operation(summary = "Reset password")
     * public ResponseEntity<String> resetPasswordFirst(@PathVariable String
     * token, @RequestParam String email) {
     * authService.resetPasswordFirst(email);
     * return ResponseEntity.ok("Token envoyé par email");
     * }
     * 
     * @PostMapping("/reset-password/{token}")
     * 
     * @Operation(summary = "Reset password")
     * public ResponseEntity<String> resetPassword(@PathVariable String
     * token, @RequestParam String newPassword) {
     * authService.resetPassword(token, newPassword);
     * return ResponseEntity.ok("Password reset successfully.");
     * }
     */
    /**
     * @param email
     * @param request
     * @return
     */
    @PutMapping("/update")
    @Operation(summary = "Mise à jour des informations d'un utilisateur")
    public ResponseEntity<String> updateUser(@RequestParam String email,
            @Valid @RequestBody UserUpdateRequest request) {
        authService.updateUser(email, request);
        return ResponseEntity.ok("Vos informations ont été mises à jour correctement");
    }

}
