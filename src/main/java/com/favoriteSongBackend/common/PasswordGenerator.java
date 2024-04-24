package com.favoriteSongBackend.common;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String NUMBER = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateRandomPassword(int length) {
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // randomly select a digit from NUMBER
            int rndCharAt = random.nextInt(NUMBER.length());
            char rndChar = NUMBER.charAt(rndCharAt);

            // append the digit to StringBuilder
            sb.append(rndChar);
        }

        return sb.toString();
    }
}
