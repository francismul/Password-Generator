# Password-Generator

## Description

A console-based password generator application that creates secure, random passwords with customizable character sets and entropy calculation.

## Features

- **Customizable Password Generation**: Choose password length (4-40 characters) and character types (lowercase, uppercase, digits, symbols)
- **Category Coverage**: Ensures at least one character from each selected category for better security
- **Entropy Calculation**: Displays the password entropy in bits for security assessment
- **Secure Random Generation**: Uses Java's SecureRandom for cryptographically secure passwords

## Running the Application

This project uses Maven for build and dependency management. Ensure you have Maven installed on your system.

### Installation

If you don't have Maven installed, you can install it using Windows Package Manager (winget):

```
winget install --accept-source-agreements Apache.Maven
```

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Commands

- **Compile the project:**
  ```
  mvn compile
  ```

- **Run the application:**
  ```
  mvn exec:java
  ```

- **Build the project (compile, test, package):**
  ```
  mvn verify
  ```

- **Clean and build:**
  ```
  mvn clean verify
  ```

The application is a console-based password generator that prompts for user input to generate secure passwords. When generating a password, you'll be asked to:

1. Enter password length (4-40 characters)
2. Choose which character types to include:
   - Lowercase letters (a-z)
   - Uppercase letters (A-Z)
   - Digits (0-9)
   - Symbols (!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~)

The generated password will include at least one character from each selected category and display its entropy in bits.