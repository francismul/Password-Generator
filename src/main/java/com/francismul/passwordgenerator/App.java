package com.francismul.passwordgenerator;

import java.util.Scanner;

/**
 * Console version of the Password Generator.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to Password Generator Console App");

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
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1: {
                        int length = -1;
                        while (true) {
                            System.out.print("Enter password length (4-40): ");
                            if (!scanner.hasNextInt()) {
                                System.out.println("Invalid input! Please enter an integer between 4 and 40.");
                                scanner.nextLine(); // consume invalid input
                                continue;
                            }
                            length = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            if (length < 4 || length > 40) {
                                System.out.println("Invalid length! Please enter a value between 4 and 40.");
                                continue;
                            }
                            break;
                        }

                        System.out.print("Include lowercase? (y/n): ");
                        boolean lower = scanner.nextLine().toLowerCase().startsWith("y");

                        System.out.print("Include uppercase? (y/n): ");
                        boolean upper = scanner.nextLine().toLowerCase().startsWith("y");

                        System.out.print("Include digits? (y/n): ");
                        boolean digits = scanner.nextLine().toLowerCase().startsWith("y");

                        System.out.print("Include symbols? (y/n): ");
                        boolean symbols = scanner.nextLine().toLowerCase().startsWith("y");

                        try {
                            String password = PasswordGenerator.generate(length, lower, upper, digits, symbols);
                            System.out.println("\nGenerated Password: " + password);
                            double entropy = PasswordGenerator.entropyBits(password, lower, upper, digits, symbols);
                            System.out.println("Entropy: " + String.format("%.1f", entropy) + " bits\n");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage() + "\n");
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("\nExiting...");
                        System.exit(0);
                    }
                    default:
                        System.out.println("\nInvalid choice! Please enter 1 or 2.\n");
                }
            }
        }
    }
}
