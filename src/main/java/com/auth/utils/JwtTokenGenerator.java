/**
 * Utility class for generating and validating JWT tokens.
 * <p>
 * This class provides methods to generate JWT tokens for user authentication,
 * validate tokens, and extract the subject from tokens.
 * </p>
 * <p>
 * The tokens are signed using the HS256 algorithm and a secret key.
 * </p>
 */
package com.auth.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtTokenGenerator {

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token that can be used to authenticate a user.
     *
     * @param subject            The subject of the token, typically a user ID or
     *                           email.
     * @param expirationInMillis The time in milliseconds until the token expires.
     * @return The generated token.
     */
    public static String generateToken(String subject, long expirationInMillis) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the subject of a JWT token, which is the value of the "sub" claim.
     *
     * @param token The JWT token to extract the subject from.
     * @return The subject of the token, or null if the token is invalid.
     */
    public static String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getSubject();
    }
}
