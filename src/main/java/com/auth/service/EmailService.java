/**
 * Service for sending various types of emails such as verification emails, 2FA codes, and password reset emails.
 */
package com.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    /**
     * Sends an email to the specified recipient with a verification link to verify
     * their email address.
     *
     * @param to    the email address of the recipient
     * @param token the verification token to be included in the email
     */
    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Please verify your email by clicking the link: \n"
                + "https://localhost:8443/api/auth/verify-email/" + token);
        mailSender.send(message);
    }

    /**
     * Sends an email to the specified recipient with a 2FA code.
     * The code will expire in 90 seconds.
     *
     * @param to   the email address of the recipient
     * @param code the 2FA code to be included in the email
     */
    public void send2FACode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("2FA Code");
        message.setText("Your 2FA code is: " + code + "\nThis code will expire in 90 seconds.");
        mailSender.send(message);
    }

    /**
     * Sends an email to the specified recipient with a link to reset their
     * password.
     *
     * @param to    the email address of the recipient
     * @param token the reset password token to be included in the email
     */

    public void sendResetPasswordEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Click the following link to reset your password: \n"
                + "https://localhost:8443/api/auth/reset-password/" + token);
        mailSender.send(message);
    }
}