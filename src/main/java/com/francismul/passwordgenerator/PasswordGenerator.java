package com.francismul.passwordgenerator;

import java.security.SecureRandom;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility class for generating secure passwords.
 */
public class PasswordGenerator {
    private static final SecureRandom RAND = new SecureRandom();
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public static String generate(int length, boolean lower, boolean upper, boolean digits, boolean symbols) {
        if (length < 4)
            throw new IllegalArgumentException("Length must be >= 4");
        StringBuilder pool = new StringBuilder();
        List<Character> required = new ArrayList<>();
        if (lower) {
            pool.append(LOWER);
            required.add(randomChar(LOWER));
        }
        if (upper) {
            pool.append(UPPER);
            required.add(randomChar(UPPER));
        }
        if (digits) {
            pool.append(DIGITS);
            required.add(randomChar(DIGITS));
        }
        if (symbols) {
            pool.append(SYMBOLS);
            required.add(randomChar(SYMBOLS));
        }
        if (pool.length() == 0)
            throw new IllegalArgumentException("Select at least one character set.");
        List<Character> chars = new ArrayList<>(length);
        // Ensure category coverage
        for (Character c : required)
            chars.add(c);
        // Fill remaining
        for (int i = chars.size(); i < length; i++) {
            chars.add(pool.charAt(RAND.nextInt(pool.length())));
        }
        // Shuffle
        Collections.shuffle(chars, RAND);
        StringBuilder out = new StringBuilder(length);
        for (Character c : chars)
            out.append(c);
        return out.toString();
    }

    private static char randomChar(String s) {
        return s.charAt(RAND.nextInt(s.length()));
    }

    public static double entropyBits(String password, boolean lower, boolean upper, boolean digits, boolean symbols) {
        int pool = 0;
        if (lower)
            pool += LOWER.length();
        if (upper)
            pool += UPPER.length();
        if (digits)
            pool += DIGITS.length();
        if (symbols)
            pool += SYMBOLS.length();
        if (pool == 0)
            return 0;
        return password.length() * (Math.log(pool) / Math.log(2));
    }
}
