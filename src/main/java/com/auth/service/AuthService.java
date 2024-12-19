package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.SignupRequest;
import com.auth.dto.UserUpdateRequest;
/*import com.auth.entity.Session;*/
import com.auth.entity.TwoFactorCode;
import com.auth.entity.User;
import com.auth.entity.VerificationToken;
import com.auth.repository.TwoFactorCodeRepository;
import com.auth.repository.UserRepository;
import com.auth.repository.VerificationTokenRepository;
//import com.auth.repository.SessionRepository;
import com.auth.utils.JwtTokenGenerator;
import com.auth.utils.PINGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    //private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

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
    public void loginFirst(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            if (user.getLoginAttempts() < 3) {
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userRepository.save(user);
            } else if (user.getLoginAttempts() >= 3){
                userRepository.save(user);
                String twoFactorToken = JwtTokenGenerator.generateToken(user.getEmail(), 90000); // 90 secondes

                emailService.send2FACode(user.getEmail(), twoFactorToken);
            }
        }
        String twoFactorCode = createTwoFactorCode(user);
        emailService.send2FACode(user.getEmail(), "Your verification PIN: " + twoFactorCode);
        }
/*    @Transactional
    public void loginConfirm(LoginRequest request, String code) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

    }*/
    private String createTwoFactorCode(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        log.info("Checking for active two-factor code for user: {}", user.getId());
        Optional<TwoFactorCode> existingCode = twoFactorCodeRepository.findActiveCodeByUser(user);
        if (existingCode.isPresent()) {
            log.warn("Active two-factor code found: {}", existingCode.get());
        }

        // Générer un nouveau code
        String code = PINGenerator.generatePin();

        TwoFactorCode twoFactorCode = new TwoFactorCode();
        twoFactorCode.setCode(code);
        twoFactorCode.setUser(user);
        twoFactorCode.setExpiryDate(LocalDateTime.now().plusSeconds(90));
        twoFactorCode.setUsed(false); // Assurez-vous que la valeur par défaut est bien définie
        twoFactorCodeRepository.save(twoFactorCode);

        return code;
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
        user.setEnabled(true);
        userRepository.save(user);

/*
        createOrUpdateSession(user);
*/

        twoFactorCodeRepository.delete(twoFactorCode); // Supprimer le code après validation
    }

/*    private void createOrUpdateSession(User user) {

        // Vérifiez si une session existe déjà pour cet utilisateur
        Optional<Session> existingSession = sessionRepository.findByUser(user);

        Session session = existingSession.orElse(new Session()); // Si aucune session, créer une nouvelle
        String token = JwtTokenGenerator.generateToken(user.getEmail(), 300000);

        session.setToken(token);
        session.setExpireLe(LocalDateTime.now().plusMinutes(5)); // Expiration dans 5 minutes
        session.setCreeLe(LocalDateTime.now());
        session.setUser(user);

        // Sauvegarder la session (création ou mise à jour)
        sessionRepository.save(session);
    }*/
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