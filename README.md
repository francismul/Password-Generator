# Password-Generator

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

The application is a console-based password generator that prompts for user input to generate secure passwords.