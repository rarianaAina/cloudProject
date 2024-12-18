package com.auth.utils;

import java.util.Random;

public class PINGenerator {

    public static String generatePin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000); // Génère un nombre entre 100000 et 999999
        return String.valueOf(pin);
    }
}

