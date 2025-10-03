package com.francismul.passwordgenerator;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages persistent storage of password generation history.
 * Stores passwords with timestamps in a local file.
 */
public class PasswordHistory {
    private static final String HISTORY_DIR = System.getProperty("user.home") + File.separator + ".passwordgenerator";
    private static final String HISTORY_FILE = HISTORY_DIR + File.separator + "history.dat";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Represents a single password entry in history.
     */
    public static class HistoryEntry {
        private final String password;
        private final LocalDateTime timestamp;

        public HistoryEntry(String password, LocalDateTime timestamp) {
            this.password = password;
            this.timestamp = timestamp;
        }

        public String getPassword() {
            return password;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getFormattedTimestamp() {
            return timestamp.format(FORMATTER);
        }

        @Override
        public String toString() {
            return timestamp.format(FORMATTER) + "|" + password;
        }

        public static HistoryEntry fromString(String line) {
            int separator = line.indexOf('|');
            if (separator == -1) {
                throw new IllegalArgumentException("Invalid history entry format");
            }
            String timestampStr = line.substring(0, separator);
            String password = line.substring(separator + 1);
            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, FORMATTER);
            return new HistoryEntry(password, timestamp);
        }
    }

    /**
     * Ensures the history directory exists.
     */
    private static void ensureDirectoryExists() throws IOException {
        Path dir = Paths.get(HISTORY_DIR);
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
    }

    /**
     * Adds a new password to the history.
     */
    public static void addPassword(String password) throws IOException {
        ensureDirectoryExists();
        HistoryEntry entry = new HistoryEntry(password, LocalDateTime.now());
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE, true))) {
            writer.write(entry.toString());
            writer.newLine();
        }
    }

    /**
     * Retrieves all password history entries, sorted from newest to oldest.
     */
    public static List<HistoryEntry> getAllHistory() throws IOException {
        ensureDirectoryExists();
        
        List<HistoryEntry> entries = new ArrayList<>();
        File file = new File(HISTORY_FILE);
        
        if (!file.exists()) {
            return entries; // Return empty list if no history yet
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        entries.add(HistoryEntry.fromString(line));
                    } catch (Exception e) {
                        // Skip malformed entries
                        System.err.println("Skipping malformed entry: " + line);
                    }
                }
            }
        }
        
        // Sort by timestamp, newest first
        entries.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        
        return entries;
    }

    /**
     * Clears all password history.
     */
    public static void clearHistory() throws IOException {
        File file = new File(HISTORY_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Checks if this is the first time accessing history (file doesn't exist or is empty).
     */
    public static boolean isFirstAccess() {
        File file = new File(HISTORY_FILE);
        return !file.exists() || file.length() == 0;
    }
}
