package com.favoriteSongBackend.common;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String NUMBER = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    //랜덤 인증번호 생성
    public static String generateRandomCheckCode(int length) {
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

    //임시비밀번호 생성
    public static String tempRandomPassword(int length){
        // generate temporary password
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        StringBuilder tempPw = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int idx = (int) (charSet.length * Math.random());
            tempPw.append(charSet[idx]);
        }

        return tempPw.toString();
    }
}
