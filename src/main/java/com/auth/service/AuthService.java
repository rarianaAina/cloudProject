package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.SignupRequest;
import com.auth.dto.UserUpdateRequest;
import com.auth.entity.TwoFactorCode;
import com.auth.entity.User;
import com.auth.entity.VerificationToken;
import com.auth.repository.TwoFactorCodeRepository;
import com.auth.repository.UserRepository;
import com.auth.repository.VerificationTokenRepository;
import com.auth.utils.JwtTokenGenerator;
import com.auth.utils.PINGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);
        
        String token = UUID.randomUUID().toString();
        createVerificationToken(user, token);
        emailService.sendVerificationEmail(user.getEmail(), token);
    }

    @Transactional
    public void login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            if (user.getLoginAttempts() < 3) {
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userRepository.save(user);
            } else if (user.getLoginAttempts() >= 3){
                userRepository.save(user);
                String twoFactorToken = JwtTokenGenerator.generateToken(user.getEmail(), 90000); // 90 secondes
                createTwoFactorCode(user);
                emailService.send2FACode(user.getEmail(), twoFactorToken);
            }
            }
        createTwoFactorCode(user);
        emailService.send2FACode(user.getEmail(), "Your verification PIN: " + PINGenerator.generatePin());
        }

    private void createTwoFactorCode(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Vérifiez s'il y a un code actif pour cet utilisateur
        Optional<TwoFactorCode> existingCode = twoFactorCodeRepository.findActiveCodeByUser(user);
        if (existingCode.isPresent()) {
            throw new IllegalStateException("An active two-factor code already exists for this user.");
        }

        // Générer un nouveau code
        String code = PINGenerator.generatePin();

        TwoFactorCode twoFactorCode = new TwoFactorCode();
        twoFactorCode.setCode(code);
        twoFactorCode.setUser(user);
        twoFactorCode.setExpiryDate(LocalDateTime.now().plusSeconds(90));
        twoFactorCode.setUsed(false); // Assurez-vous que la valeur par défaut est bien définie
        twoFactorCodeRepository.save(twoFactorCode);
    }


    private void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(verificationToken);
    }




    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);  // Marque l'email comme vérifié
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken); // Supprimer le token après utilisation
    }

    @Transactional

    public void verify2FA(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TwoFactorCode twoFactorCode = twoFactorCodeRepository.findByUserAndCodeAndUsedFalse(user, code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired 2FA code"));

        if (twoFactorCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("2FA code has expired");
        }

        twoFactorCodeRepository.delete(twoFactorCode); // Supprimer le code après validation
    }

/*    @Transactional
    public void resetPasswordFirst(String email) {

    }
    @Transactional
    public void resetPassword(String token, String newPassword) {
        String email = JwtTokenGenerator.validateToken(token); // Décode et valide le jeton

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));  // Mettre à jour le mot de passe
        userRepository.save(user);
    }*/
    @Transactional
    public void updateUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("User is not enabled");
        }

/*        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }*/

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword())); // Mettre à jour le mot de passe
        }

        userRepository.save(user);
    }


}