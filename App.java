import java.util.*;

class App {
    public static void main(String[] args) {
        System.out.println("Welcome to Password Generator App");

        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n1. Generate Password");
                System.out.println("2. Exit");
                System.out.print("Enter your choice: ");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a number (1 or 2).");
                    scanner.next(); // Consume the buffer left over by nextInt
                    continue;
                }

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        List<String> password = generateRandomPassword(scanner);
                        System.out.print("Generated Password: ");
                        password.forEach(System.out::print);
                        System.out.println();
                    }
                    case 2 -> {
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice! Please enter 1 or 2.");
                }
            }
        }
    }

    public static List<String> generateRandomPassword(Scanner scanner) {
        List<String> lowercase = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
        List<String> uppercase = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        List<String> digits = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));
        List<String> punctuation = new ArrayList<>(Arrays.asList("!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~"));

        System.out.print("Enter the number of characters: ");
        int characterNumber;

        while (true) {
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a valid number.");
                scanner.next(); // consume the leftover buffer
                continue;
            }

            characterNumber = scanner.nextInt();
            if (characterNumber < 4) {
                System.out.println("Password must be at least 4 characters long. Try again.");
            } else {
                break;
            }
        }

        Random rand = new Random();
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

        while (result.size() < characterNumber) {
            result.add(allCharacters.get(rand.nextInt(allCharacters.size())));
        }

        // Final shuffle for randomness
        Collections.shuffle(result, rand);

        return result;
    }
}
