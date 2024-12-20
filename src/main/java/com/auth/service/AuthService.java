/**
 * Service class for handling authentication-related operations.
 * 
 * <p>This service provides methods for user signup, login, email verification,
 * two-factor authentication (2FA), and user information updates. It interacts
 * with various repositories to manage user data, verification tokens, and 2FA codes.
 * </p>
 * 
 * <p>Note: Some methods and fields related to session management and password reset
 * are commented out and not currently in use.</p>
 * 
 * <p>Dependencies:</p>
 * <ul>
 *   <li>{@link UserRepository}</li>
 *   <li>{@link TwoFactorCodeRepository}</li>
 *   <li>{@link VerificationTokenRepository}</li>
 *   <li>{@link PasswordEncoder}</li>
 *   <li>{@link EmailService}</li>
 * </ul>
 * 
 * <p>Logging is provided by SLF4J.</p>
 * 
 * <p>Transactional annotations are used to ensure atomicity of operations.</p>
 * 
 * @see UserRepository
 * @see TwoFactorCodeRepository
 * @see VerificationTokenRepository
 * @see PasswordEncoder
 * @see EmailService
 */
package com.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TwoFactorCodeRepository twoFactorCodeRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    // private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    /**
     * Signs up a new user.
     * 
     * @param request The data required for signing up a new user, including email,
     *                password, first name, and last name.
     * 
     * @throws RuntimeException if the email is already registered.
     */
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

    /**
     * Performs the first step of the login process: verifies the user and password.
     * If the password is incorrect, increments the login attempts counter and saves
     * the user. If the login attempts counter reaches 3, sends a 2FA code to the
     * user
     * via email.
     * 
     * @param request The data required for logging in, including email and
     *                password.
     */
    @Transactional
    public void loginFirst(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            if (user.getLoginAttempts() < 3) {
                user.setLoginAttempts(user.getLoginAttempts() + 1);
                userRepository.save(user);
            } else if (user.getLoginAttempts() >= 3) {
                userRepository.save(user);
                String twoFactorToken = JwtTokenGenerator.generateToken(user.getEmail(), 90000); // 90 secondes

                emailService.send2FACode(user.getEmail(), twoFactorToken);
            }
        }
        String twoFactorCode = createTwoFactorCode(user);
        emailService.send2FACode(user.getEmail(), "Your verification PIN: " + twoFactorCode);
    }

    /*
     * @Transactional
     * public void loginConfirm(LoginRequest request, String code) {
     * User user = userRepository.findByEmail(request.getEmail())
     * .orElseThrow(() -> new RuntimeException("User not found"));
     * 
     * }
     */

    /**
     * Generates a new two-factor authentication code for the specified user.
     *
     * This method checks if there is an existing active two-factor code for the
     * user.
     * If an active code is found, a warning is logged. A new code is then
     * generated,
     * saved to the repository, and returned. The code is set to expire in 90
     * seconds.
     *
     * @param user the user for whom the two-factor code is being generated
     * @return the newly generated two-factor authentication code
     * @throws IllegalArgumentException if the user is null
     */

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

    /**
     * Creates a new verification token for the specified user.
     * 
     * @param user  the user for whom the verification token is being created
     * @param token the token to associate with the user
     */
    private void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        verificationTokenRepository.save(verificationToken);
    }

    /**
     * Verifies the user's email using the provided verification token.
     * 
     * <p>
     * This method checks if the provided token is valid and not expired.
     * If the token is valid, the user's email is marked as verified and
     * the token is deleted from the repository.
     * </p>
     * 
     * @param token The verification token sent to the user's email.
     * 
     * @throws RuntimeException if the token is invalid or expired.
     */

    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true); // Marque l'email comme vérifié
        userRepository.save(user);

        verificationTokenRepository.delete(verificationToken); // Supprimer le token après utilisation
    }

    /**
     * Verifies the user's email using the provided 2FA code.
     * 
     * <p>
     * This method checks if the provided code is valid and not expired.
     * If the code is valid, the user's account is enabled and the code is
     * deleted from the repository.
     * </p>
     * 
     * @param email The user's email address.
     * @param code  The 2FA code sent to the user's email.
     * 
     * @throws RuntimeException if the code is invalid or expired.
     */
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
         * createOrUpdateSession(user);
         */

        twoFactorCodeRepository.delete(twoFactorCode); // Supprimer le code après validation
    }

    /*
     * private void createOrUpdateSession(User user) {
     * 
     * // Vérifiez si une session existe déjà pour cet utilisateur
     * Optional<Session> existingSession = sessionRepository.findByUser(user);
     * 
     * Session session = existingSession.orElse(new Session()); // Si aucune
     * session, créer une nouvelle
     * String token = JwtTokenGenerator.generateToken(user.getEmail(), 300000);
     * 
     * session.setToken(token);
     * session.setExpireLe(LocalDateTime.now().plusMinutes(5)); // Expiration dans 5
     * minutes
     * session.setCreeLe(LocalDateTime.now());
     * session.setUser(user);
     * 
     * // Sauvegarder la session (création ou mise à jour)
     * sessionRepository.save(session);
     * }
     */
    /*
     * @Transactional
     * public void resetPasswordFirst(String email) {
     * 
     * }
     * 
     * @Transactional
     * public void resetPassword(String token, String newPassword) {
     * String email = JwtTokenGenerator.validateToken(token); // Décode et valide le
     * jeton
     * 
     * User user = userRepository.findByEmail(email)
     * .orElseThrow(() -> new RuntimeException("User not found"));
     * 
     * user.setPassword(passwordEncoder.encode(newPassword)); // Mettre à jour le
     * mot de passe
     * userRepository.save(user);
     * }
     */

    /**
     * Updates the user information based on the provided email and update request.
     * 
     * <p>
     * This method finds the user by email and updates the user's details such as
     * first name, last name, and password. It checks if the user is enabled before
     * proceeding with the update. If the user is not enabled or not found, an
     * exception is thrown.
     * </p>
     * 
     * @param email   The email address of the user to be updated.
     * @param request The request object containing the new user information.
     * 
     * @throws RuntimeException if the user is not found or not enabled.
     */
    @Transactional
    public void updateUser(String email, UserUpdateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("User is not enabled");
        }

        /*
         * if (request.getEmail() != null &&
         * !request.getEmail().equals(user.getEmail())) {
         * if (userRepository.existsByEmail(request.getEmail())) {
         * throw new RuntimeException("Email already in use");
         * }
         * user.setEmail(request.getEmail());
         * }
         */

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