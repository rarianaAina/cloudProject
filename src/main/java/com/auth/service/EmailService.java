package com.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    
    public void sendVerificationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification");
        message.setText("Please verify your email by clicking the link: \n"
            + "https://localhost:8443/api/auth/verify-email/" + token);
        mailSender.send(message);
    }
    
    public void send2FACode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("2FA Code");
        message.setText("Your 2FA code is: " + code + "\nThis code will expire in 90 seconds.");
        mailSender.send(message);
    }
    
    public void sendResetPasswordEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Reset Password");
        message.setText("Click the following link to reset your password: \n"
            + "https://localhost:8443/api/auth/reset-password/" + token);
        mailSender.send(message);
    }
}