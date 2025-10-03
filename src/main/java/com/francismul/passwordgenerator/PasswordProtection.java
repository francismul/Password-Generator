package com.francismul.passwordgenerator;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Manages password protection for accessing password history.
 * Uses salted SHA-256 hashing for secure password storage.
 */
public class PasswordProtection {
    private static final String PROTECTION_DIR = System.getProperty("user.home") + File.separator + ".passwordgenerator";
    private static final String PASSWORD_FILE = PROTECTION_DIR + File.separator + "master.dat";
    private static final int SALT_LENGTH = 16;

    /**
     * Checks if a master password has been set.
     */
    public static boolean isMasterPasswordSet() {
        File file = new File(PASSWORD_FILE);
        return file.exists() && file.length() > 0;
    }

    /**
     * Sets the master password (hashed with salt).
     */
    public static void setMasterPassword(String password) throws IOException, NoSuchAlgorithmException {
        ensureDirectoryExists();
        
        // Generate random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        
        // Hash the password with salt
        byte[] hashedPassword = hashPassword(password, salt);
        
        // Store salt + hash
        try (FileOutputStream fos = new FileOutputStream(PASSWORD_FILE)) {
            fos.write(salt);
            fos.write(hashedPassword);
        }
    }

    /**
     * Verifies if the provided password matches the stored master password.
     */
    public static boolean verifyMasterPassword(String password) throws IOException, NoSuchAlgorithmException {
        if (!isMasterPasswordSet()) {
            return false;
        }

        // Read salt + hash from file
        byte[] fileData;
        try (FileInputStream fis = new FileInputStream(PASSWORD_FILE)) {
            fileData = fis.readAllBytes();
        }

        if (fileData.length < SALT_LENGTH) {
            return false;
        }

        // Extract salt and stored hash
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(fileData, 0, salt, 0, SALT_LENGTH);
        
        byte[] storedHash = new byte[fileData.length - SALT_LENGTH];
        System.arraycopy(fileData, SALT_LENGTH, storedHash, 0, storedHash.length);

        // Hash the provided password with the same salt
        byte[] providedHash = hashPassword(password, salt);

        // Compare hashes
        return MessageDigest.isEqual(storedHash, providedHash);
    }

    /**
     * Resets the master password (deletes the password file).
     * @throws IOException if the file exists but cannot be deleted
     */
    public static void resetMasterPassword() throws IOException {
        File file = new File(PASSWORD_FILE);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Failed to delete master password file: " + PASSWORD_FILE);
            }
        }
    }

    /**
     * Hashes a password with the given salt using SHA-256.
     */
    private static byte[] hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        return md.digest(password.getBytes());
    }

    /**
     * Ensures the protection directory exists.
     */
    private static void ensureDirectoryExists() throws IOException {
        Path dir = Paths.get(PROTECTION_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }
}
