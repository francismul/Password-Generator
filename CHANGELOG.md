# Changelog

All notable changes to the Password Generator project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [6.0.0] - 2025-10-03

### Added
- 🔐 **Persistent Password History** - All generated passwords are now automatically saved with timestamps across all sessions
- 🔒 **Master Password Protection** - First-time history access requires setting a master password for security
- 📊 **Enhanced History Viewer** - Beautiful dialog displaying all password history with timestamps in format: `position. timestamp, password`
- 📋 **Click-to-Copy in History** - Click any password in the history viewer to instantly copy it to clipboard
- 💾 **Export History Feature** - Export complete password history to a timestamped text file
- 🗑️ **Clear History Option** - Safely clear all saved passwords with confirmation dialog
- 🔐 **Secure Master Password Storage** - Master password stored using SHA-256 hashing with random salt
- 🛡️ **Password Verification** - Three-attempt limit for master password authentication
- 📁 **New PasswordHistory Class** - Manages persistent storage of password history in `~/.passwordgenerator/history.dat`
- 🔑 **New PasswordProtection Class** - Handles secure master password storage and verification in `~/.passwordgenerator/master.dat`
- ⏰ **Timestamp Recording** - Each generated password is saved with precise timestamp (YYYY-MM-DD HH:MM:SS format)
- 🎨 **History Entry Hover Effects** - Visual feedback when hovering over history entries
- 📜 **Scrollable History View** - Smooth scrolling for viewing large password histories

### Changed
- Updated version number from 5.0.0 to 6.0.0
- Modified password generation flow to automatically save to persistent history
- Enhanced "📜 History" button to trigger password-protected history viewer
- Updated bottom bar version label to "v6.0 - Password History"
- History now sorted from newest to oldest by timestamp
- Improved history dialog with professional layout and theming

### Security
- Master password stored with cryptographic hashing (SHA-256 + salt) instead of plain text
- Password verification limited to 3 attempts to prevent brute force attacks
- History files stored securely in user's home directory (`~/.passwordgenerator/`)
- No sensitive data logged or exposed in error messages

### Technical Details
- Added `PasswordHistory.java` - Core history persistence engine
- Added `PasswordProtection.java` - Security layer for history access
- Modified `AppGui.java` - Integrated history tracking and password protection UI
- Storage location: `~/.passwordgenerator/` (platform-independent)
- History format: Pipe-delimited timestamp and password entries

---

## [5.0.0] - Previous Release

### Features
- Dark/Light theme toggle
- Enhanced Equalizer-inspired GUI design
- Neon-style animations and glowing effects
- Animated password reveal with glitch effect
- Real-time password strength indicator
- Custom slider UI with gradient effects
- Vertical password length slider (4-40 characters)
- Character set customization (Letters, Numbers, Symbols)
- Separate lowercase/uppercase toggles
- Copy to clipboard functionality
- Save passwords to file feature
- Session-based history (non-persistent)
- Keyboard shortcuts (Enter, Ctrl+C, Shift+Enter)
- Transient toast notifications
- Password strength meter with visual feedback
- Custom progress bar with animations
- Responsive UI with modern styling

---

## Version History Summary

| Version | Date | Key Features |
|---------|------|--------------|
| 6.0.0 | 2025-10-03 | Password History with Master Password Protection |
| 5.0.0 | Previous | Enhanced GUI with Animations and Theming |

---

## Upgrade Notes

### Upgrading to 6.0.0
- **First Run**: You will be prompted to set a master password when accessing history for the first time
- **Security**: Remember your master password! There is no recovery mechanism (by design for security)
- **Storage**: History files are stored in `~/.passwordgenerator/` directory
- **Privacy**: If you want to reset your master password, delete `~/.passwordgenerator/master.dat`
- **Clear Data**: To clear all history, use the "Clear All" button in the history viewer or delete `~/.passwordgenerator/history.dat`

---

## Future Roadmap

### Planned Features
- Password history search and filter
- Export to different formats (CSV, JSON)
- Password strength history analytics
- Master password change functionality
- Password categories/tags
- Backup and restore functionality
- Configurable history retention period
- Password history encryption at rest

---

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

See LICENSE file for details.

## Contact

- Repository: [francismul/Password-Generator](https://github.com/francismul/Password-Generator)
- Issues: [GitHub Issues](https://github.com/francismul/Password-Generator/issues)
