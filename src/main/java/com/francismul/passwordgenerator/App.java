package com.francismul.passwordgenerator;

import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Password Generator App
 * This app generates a random password based on user input.
 * It ensures that the password contains at least one character from each category:
 * lowercase, uppercase, digits, and punctuation.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to Password Generator App");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("1. Generate Password");
                System.out.println("2. Exit");
                System.out.print("Enter your choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("\nInvalid input! Please enter a number (1 or 2).\n");
                    scanner.nextLine();
                    continue;
                }

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        List<String> password = generateRandomPassword(scanner);
                        System.out.print("\nGenerated Password: ");
                        password.forEach(System.out::print);
                        System.out.println("\n");
                    }
                    case 2 -> {
                        System.out.println("\nExiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("\nInvalid choice! Please enter 1 or 2.\n");
                }
            }
        }
    }

    /**
     * Generates a random password based on user input.
     *
     * @param scanner Scanner object for user input
     * @return List of strings representing the generated password
     */
    public static List<String> generateRandomPassword(Scanner scanner) {
        List<String> lowercase = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
        List<String> uppercase = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        List<String> digits = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        List<String> punctuation = new ArrayList<>(Arrays.asList("!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+",
                ",", "-", "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"));

        System.out.print("Enter the number of characters: ");
        int characterNumber;

        while (true) {
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.nextLine();
                continue;
            }

            characterNumber = scanner.nextInt();
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Clear the newline character
            }
            // Validate the input
            // Check if the number is negative or too large
            // Check if the number is less than 8
            if (characterNumber < 0) {
                System.out.println("Number of characters cannot be negative. Try again.");
            } else if (characterNumber > 100) {
                System.out.println("Password length too long. Please enter a number less than or equal to 100.");
            } else if (characterNumber < 8) {
                System.out.println("Password must be at least 8 characters long. Try again.");
            } else {
                break;
            }
        }

        // Ensure the password has at least one character from each category
        SecureRandom rand = new SecureRandom();
        List<String> result = new ArrayList<>(characterNumber);

        // Shuffle individual lists
        Collections.shuffle(lowercase, rand);
        Collections.shuffle(uppercase, rand);
        Collections.shuffle(digits, rand);
        Collections.shuffle(punctuation, rand);

        // Ensure password has a mix of character types
        result.add(lowercase.get(rand.nextInt(lowercase.size())));
        result.add(uppercase.get(rand.nextInt(uppercase.size())));
        result.add(digits.get(rand.nextInt(digits.size())));
        result.add(punctuation.get(rand.nextInt(punctuation.size())));

        // Fill remaining slots with random characters
        List<String> allCharacters = new ArrayList<>();
        allCharacters.addAll(lowercase);
        allCharacters.addAll(uppercase);
        allCharacters.addAll(digits);
        allCharacters.addAll(punctuation);

        Collections.shuffle(allCharacters, rand);

        // Fill the rest of the password with random characters
        // Ensure the password has at least one character from each category
        while (result.size() < characterNumber) {
            result.add(allCharacters.get(rand.nextInt(allCharacters.size())));
        }

        // Shuffle the final password to ensure randomness
        Collections.shuffle(result, rand);

        return result;
    }
}
