/**
 * The PINGenerator class provides a method to generate a random 6-digit PIN code.
 * 
 * <p>This class uses the {@link java.util.Random} class to generate a random integer
 * within the range of 100000 to 999999, ensuring that the PIN is always 6 digits long.
 * 
 * <p>Example usage:
 * <pre>
 * {@code
 * String pin = PINGenerator.generatePin();
 * System.out.println("Generated PIN: " + pin);
 * }
 * </pre>
 * 
 * <p>Note: This class is designed to be used as a utility class and therefore has a private constructor
 * to prevent instantiation.
 * 
 * @see java.util.Random
 */
package com.auth.utils;

import java.util.Random;

public class PINGenerator {

    /**
     * Génère un code PIN aléatoire de 6 chiffres.
     * 
     * @return le code PIN sous forme de chaîne de caractères.
     */
    public static String generatePin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000); // Génère un nombre entre 100000 et 999999
        return String.valueOf(pin);
    }
}
