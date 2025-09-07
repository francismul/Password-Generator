# Password-Generator

## Description

A comprehensive password generator application with both console and graphical user interface (GUI) versions. Creates secure, random passwords with customizable character sets and entropy calculation.

## Features

### Core Features
- **Customizable Password Generation**: Choose password length (4-40 characters) and character types (lowercase, uppercase, digits, symbols)
- **Category Coverage**: Ensures at least one character from each selected category for better security
- **Entropy Calculation**: Displays the password entropy in bits for security assessment
- **Secure Random Generation**: Uses Java's SecureRandom for cryptographically secure passwords

### GUI Features
- **Modern Dark Theme**: Equalizer-inspired dark themed interface with neon accents
- **Animated Password Reveal**: Glitch-style animation when generating passwords
- **Real-time Strength Indicator**: Visual progress bar with color-coded strength levels
- **Password History**: Save and manage generated passwords with export functionality
- **Theme Toggle**: Switch between dark and light themes
- **Keyboard Shortcuts**: Enhanced shortcuts for power users (Ctrl+C to copy, Shift+Enter for copy & generate)
- **Copy to Clipboard**: One-click copying of generated passwords

## Building the Project

This project uses Maven for build and dependency management. Ensure you have Maven installed on your system.

**Note:** You may see warnings about deprecated methods in sun.misc.Unsafe when running Maven. These are harmless warnings from Maven's internal dependencies and do not affect the functionality of your application.

### Installation

If you don't have Maven installed, you can install it using Windows Package Manager (winget):

```
winget install --accept-source-agreements Apache.Maven
```

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Compilation

1. **Compile the project:**
   ```bash
   mvn compile
   ```

2. **Build the complete project (compile, test, package):**
   ```bash
   mvn verify
   ```

3. **Clean and build:**
   ```bash
   mvn clean verify
   ```

## Running the Applications

**Note for Windows PowerShell users:** If you encounter Maven lifecycle phase errors, use the PowerShell-specific commands provided below, which properly handle the `-D` parameter formatting.

### Console Application

The console version provides a text-based interface for password generation.

**To run the console application:**
```bash
mvn exec:java -Dexec.mainClass="com.francismul.passwordgenerator.App"
```

**PowerShell (Windows):**
```powershell
mvn exec:java "-Dexec.mainClass=com.francismul.passwordgenerator.App"
```

**Usage:**
1. Choose option 1 to generate a password
2. Enter password length (4-40 characters)
3. Choose which character types to include:
   - Lowercase letters (a-z)
   - Uppercase letters (A-Z)
   - Digits (0-9)
   - Symbols (!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~)
4. The generated password will include at least one character from each selected category
5. View the entropy calculation in bits
6. Choose option 2 to exit

### GUI Application

The GUI version provides a modern, animated interface with additional features.

**Requirements:** A graphical desktop environment (not suitable for headless servers or SSH sessions without X11 forwarding).

**To run the GUI application:**
```bash
mvn exec:java -Dexec.mainClass="com.francismul.passwordgenerator.AppGui"
```

**PowerShell (Windows):**
```powershell
mvn exec:java "-Dexec.mainClass=com.francismul.passwordgenerator.AppGui"
```

**GUI Features:**
- **Password Length Slider**: Drag to set length (4-40 characters)
- **Character Type Checkboxes**: Select/deselect character types
- **Generate Button**: Click to create password with glitch animation
- **Strength Indicator**: Real-time visual feedback with color coding
- **Copy to Clipboard**: Click the password or use Ctrl+C
- **Password History**: View and manage previously generated passwords
- **Save to File**: Export passwords to a secure text file
- **Theme Toggle**: Switch between dark and light themes
- **Keyboard Shortcuts**:
  - Enter: Generate password
  - Ctrl+C: Copy current password
  - Shift+Enter: Copy and generate new password