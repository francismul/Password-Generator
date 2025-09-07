package com.francismul.passwordgenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Enhanced Equalizer-inspired dark themed password generator GUI.
 * Features beautiful animations, glowing effects, and modern styling.
 */
public class AppGui extends JFrame {

    // ===== UI Components =====
    private final JSlider lengthSlider = new JSlider(JSlider.VERTICAL, 4, 40, 16);
    private final JLabel lengthValueLabel = neonLabel("16");
    private final JCheckBox includeLetters = styledCheck("Letters");
    private final JCheckBox toggleLower = styledSubCheck("Lowercase");
    private final JCheckBox toggleUpper = styledSubCheck("Uppercase");
    private final JCheckBox includeNumbers = styledCheck("Numbers");
    private final JCheckBox includeSymbols = styledCheck("Symbols");
    private final JButton generateBtn = glowButton("GENERATE");
    private final JLabel passwordLabel = new JLabel("Click Generate to create password", SwingConstants.CENTER);
    private final CustomProgressBar strengthBar = new CustomProgressBar(0, 100);
    private final JLabel strengthText = new JLabel("Strength: Ready to generate");
    private final JButton themeToggle = smallButton("☀ Light");
    private final JButton saveBtn = smallButton("💾 Save");
    private final JButton copyBtn = smallButton("📋 Copy");
    private final JButton historyBtn = smallButton("📜 History");
    private final Deque<String> history = new ArrayDeque<>();
    private final JPanel historyListPanel = new JPanel();

    // Theme colors
    private final Color neonAccent = new Color(0, 255, 200);
    private final Color neonAccentAlt = new Color(255, 100, 255);
    private final Color neonDanger = new Color(255, 80, 80);
    private final Color neonWarning = new Color(255, 200, 0);
    private final Color neonSuccess = new Color(100, 255, 100);
    private boolean darkTheme = true;

    private javax.swing.Timer glitchTimer;
    private javax.swing.Timer pulseTimer;
    private String targetPassword = "";
    private static final SecureRandom RAND = new SecureRandom();
    private int pulseValue = 0;

    public AppGui() {
        super("🔐 Quantum Password Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(950, 600));

        applyGlobalFonts();
        initSlider();
        initPasswordDisplay();
        initStrengthBar();
        initHistoryPanel();
        startPulseAnimation();

        JPanel controls = buildControlsColumn();
        JPanel center = buildCenterPanel();

        add(controls, BorderLayout.WEST);
        add(center, BorderLayout.CENTER);
        add(buildBottomBar(), BorderLayout.SOUTH);

        setupActions();
        applyTheme();
        pack();
        setLocationRelativeTo(null);

        // Initial enable states
        includeLetters.setSelected(true);
        toggleLower.setSelected(true);
        toggleUpper.setSelected(true);
        includeNumbers.setSelected(true);
        includeSymbols.setSelected(true);
        updateGenerateState();
    }

    private void startPulseAnimation() {
        pulseTimer = new javax.swing.Timer(100, e -> {
            pulseValue = (pulseValue + 5) % 100;
            generateBtn.repaint();
        });
        pulseTimer.start();
    }

    private void applyGlobalFonts() {
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("CheckBox.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ToggleButton.font", new Font("Segoe UI", Font.PLAIN, 13));
    }

    private void initSlider() {
        lengthSlider.setPaintTicks(true);
        lengthSlider.setMajorTickSpacing(8);
        lengthSlider.setMinorTickSpacing(1);
        lengthSlider.setOpaque(false);
        lengthSlider.setUI(new NeonSliderUI(lengthSlider));
        lengthSlider.addChangeListener(e -> {
            lengthValueLabel.setText(String.valueOf(lengthSlider.getValue()));
        });
    }

    private void initPasswordDisplay() {
        passwordLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 24));
        passwordLabel.setForeground(Color.GRAY);
        passwordLabel.setBorder(new EmptyBorder(30, 30, 30, 30));
        passwordLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passwordLabel.setToolTipText("Click to copy password");
        passwordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                copyCurrentPassword();
            }
        });
    }

    private void initStrengthBar() {
        strengthBar.setStringPainted(false);
        strengthBar.setPreferredSize(new Dimension(300, 20));
        strengthBar.setBorder(null);
        strengthBar.setOpaque(false);
    }

    private void initHistoryPanel() {
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(historyListPanel);
        scroll.setPreferredSize(new Dimension(240, 160));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        historyListPanel.setBorder(new EmptyBorder(10, 5, 5, 5));
    }

    private JPanel buildControlsColumn() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(new EmptyBorder(20, 20, 20, 20));
        side.setOpaque(false);

        JLabel lenLabel = neonLabel("Password Length");
        lenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        lengthValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        side.add(lenLabel);
        side.add(Box.createVerticalStrut(8));
        side.add(lengthValueLabel);
        side.add(Box.createVerticalStrut(8));
        side.add(lengthSlider);
        side.add(Box.createVerticalStrut(30));

        side.add(sectionLabel("⚡ CHARACTER SETS"));
        includeLetters.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleLower.setAlignmentX(Component.LEFT_ALIGNMENT);
        toggleUpper.setAlignmentX(Component.LEFT_ALIGNMENT);
        includeNumbers.setAlignmentX(Component.LEFT_ALIGNMENT);
        includeSymbols.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(includeLetters);
        JPanel sub = new JPanel();
        sub.setLayout(new BoxLayout(sub, BoxLayout.Y_AXIS));
        sub.setOpaque(false);
        sub.add(toggleLower);
        sub.add(toggleUpper);
        sub.setBorder(new EmptyBorder(0, 20, 0, 0));
        side.add(sub);
        side.add(includeNumbers);
        side.add(includeSymbols);
        side.add(Box.createVerticalStrut(20));

        generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        side.add(generateBtn);

        side.add(Box.createVerticalStrut(20));
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(themeToggle);
        buttonRow.add(copyBtn);
        buttonRow.add(saveBtn);
        buttonRow.add(historyBtn);
        side.add(buttonRow);

        return side;
    }

    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Password display with fancy border
        JPanel displayPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animated border
                float alpha = 0.3f + 0.4f * (float) Math.sin(pulseValue * 0.1);
                alpha = Math.max(0f, Math.min(1f, alpha));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(neonAccent);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
                g2.dispose();
            }
        };
        displayPanel.setOpaque(false);
        displayPanel.add(passwordLabel, BorderLayout.CENTER);
        center.add(displayPanel, BorderLayout.CENTER);

        JPanel strengthPanel = new JPanel();
        strengthPanel.setOpaque(false);
        strengthPanel.setLayout(new BoxLayout(strengthPanel, BoxLayout.Y_AXIS));

        strengthText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        strengthText.setForeground(Color.GRAY);
        strengthText.setAlignmentX(Component.CENTER_ALIGNMENT);
        strengthPanel.add(strengthText);
        strengthPanel.add(Box.createVerticalStrut(8));

        strengthBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        strengthPanel.add(strengthBar);

        center.add(strengthPanel, BorderLayout.SOUTH);
        return center;
    }

    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setOpaque(false);
        bar.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel hint = new JLabel(
                "💡 Enter = Generate | Ctrl+C = Copy | Click password to copy | Shift+Enter = Copy & Generate");
        hint.setForeground(new Color(150, 150, 150));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        hint.setBorder(new EmptyBorder(5, 15, 5, 10));
        bar.add(hint, BorderLayout.WEST);

        JLabel version = new JLabel("v2.0 Enhanced");
        version.setForeground(new Color(100, 100, 100));
        version.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        version.setBorder(new EmptyBorder(5, 10, 5, 15));
        bar.add(version, BorderLayout.EAST);

        return bar;
    }

    private static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(new Color(180, 180, 180));
        l.setBorder(new EmptyBorder(15, 0, 8, 0));
        return l;
    }

    private static JCheckBox styledCheck(String label) {
        JCheckBox cb = new JCheckBox(label);
        cb.setOpaque(false);
        cb.setForeground(new Color(220, 220, 220));
        cb.setFocusPainted(false);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return cb;
    }

    private static JCheckBox styledSubCheck(String label) {
        JCheckBox cb = styledCheck(label);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setForeground(new Color(200, 200, 200));
        return cb;
    }

    private JButton glowButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                float alpha = 0.7f + 0.3f * (float) Math.sin(pulseValue * 0.15);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 80, 80), 0, getHeight(),
                        new Color(0, 120, 120));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                g2.setColor(neonAccent);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                g2.dispose();
                g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(getText(), x + 1, y + 1);
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(12, 25, 12, 25));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                b.setBorder(new EmptyBorder(13, 26, 11, 24));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                b.setBorder(new EmptyBorder(12, 25, 12, 25));
            }
        });
        return b;
    }

    private static JButton smallButton(String text) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                double t = System.nanoTime() / 1_0e8;
                float alpha = 0.6f + 0.25f * (float) Math.sin(t);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 90, 95), 0, getHeight(),
                        new Color(25, 120, 125));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
                g2.setColor(new Color(0, 255, 200));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);
                g2.dispose();
                g2 = (Graphics2D) g.create();
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setColor(Color.WHITE);
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static JLabel neonLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(new Color(0, 255, 200));
        return l;
    }

    // Custom Progress Bar with cool animations
    private class CustomProgressBar extends JProgressBar {
        public CustomProgressBar(int min, int max) {
            super(min, max);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Background
            g2.setColor(new Color(30, 30, 30));
            g2.fillRoundRect(0, 0, width, height, height / 2, height / 2);

            // Progress fill
            double progress = (double) getValue() / getMaximum();
            int fillWidth = (int) (width * progress);

            if (fillWidth > 0) {
                Color startColor, endColor;
                if (getValue() < 30) {
                    startColor = neonDanger;
                    endColor = neonDanger.darker();
                } else if (getValue() < 60) {
                    startColor = neonWarning;
                    endColor = neonWarning.darker();
                } else {
                    startColor = neonSuccess;
                    endColor = neonSuccess.darker();
                }

                GradientPaint gp = new GradientPaint(0, 0, startColor, fillWidth, 0, endColor);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, fillWidth, height, height / 2, height / 2);

                // Glowing effect
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.setColor(startColor.brighter());
                g2.fillRoundRect(0, 0, fillWidth, height, height / 2, height / 2);
            }

            // Border
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(0, 0, width - 1, height - 1, height / 2, height / 2);

            g2.dispose();
        }
    }

    private void setupActions() {
        includeLetters.addActionListener(e -> {
            boolean enabled = includeLetters.isSelected();
            toggleLower.setEnabled(enabled);
            toggleUpper.setEnabled(enabled);
            if (!enabled) {
                toggleLower.setSelected(false);
                toggleUpper.setSelected(false);
            } else {
                if (!toggleLower.isSelected() && !toggleUpper.isSelected()) {
                    toggleLower.setSelected(true);
                }
            }
            updateGenerateState();
        });

        ActionListener recalc = e -> updateGenerateState();
        toggleLower.addActionListener(recalc);
        toggleUpper.addActionListener(recalc);
        includeNumbers.addActionListener(recalc);
        includeSymbols.addActionListener(recalc);

        generateBtn.addActionListener(e -> doGenerate());
        copyBtn.addActionListener(e -> copyCurrentPassword());
        historyBtn.addActionListener(e -> openHistoryDialog());

        themeToggle.addActionListener(e -> {
            darkTheme = !darkTheme;
            themeToggle.setText(darkTheme ? "☀ Light" : "🌙 Dark");
            applyTheme();
        });

        saveBtn.addActionListener(e -> savePasswords());

        // Enhanced key bindings
        getRootPane().setDefaultButton(generateBtn);
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()),
                "copyPwd");
        am.put("copyPwd", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                copyCurrentPassword();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK), "copyAndGenerate");
        am.put("copyAndGenerate", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                copyCurrentPassword();
                doGenerate();
            }
        });
    }

    private void updateGenerateState() {
        boolean any = (includeNumbers.isSelected() || includeSymbols.isSelected() ||
                (includeLetters.isSelected() && (toggleLower.isSelected() || toggleUpper.isSelected())));
        generateBtn.setEnabled(any);

        if (!any) {
            strengthText.setText("⚠️ Select at least one character type");
            strengthText.setForeground(neonDanger);
            strengthBar.setValue(0);
        } else if (targetPassword.isEmpty()) {
            strengthText.setText("Ready to generate secure password");
            strengthText.setForeground(Color.GRAY);
            strengthBar.setValue(0);
        }
    }

    private void doGenerate() {
        try {
            int length = lengthSlider.getValue();
            boolean lower = includeLetters.isSelected() && toggleLower.isSelected();
            boolean upper = includeLetters.isSelected() && toggleUpper.isSelected();
            boolean digits = includeNumbers.isSelected();
            boolean symbols = includeSymbols.isSelected();

            String pwd = PasswordGenerator.generate(length, lower, upper, digits, symbols);
            targetPassword = pwd;
            addToHistory(pwd);
            animatePasswordReveal(pwd);
            updateStrength(pwd, lower, upper, digits, symbols);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Generation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateStrength(String pwd, boolean lower, boolean upper, boolean digits, boolean symbols) {
        double bits = PasswordGenerator.entropyBits(pwd, lower, upper, digits, symbols);
        int maxBits = 256;
        int percent = (int) Math.min(100, Math.round((bits / maxBits) * 100));

        strengthBar.setValue(percent);

        String strengthLevel;
        Color strengthColor;

        if (bits < 28) {
            strengthLevel = "😰 Very Weak";
            strengthColor = neonDanger;
        } else if (bits < 40) {
            strengthLevel = "😟 Weak";
            strengthColor = neonDanger;
        } else if (bits < 60) {
            strengthLevel = "😐 Fair";
            strengthColor = neonWarning;
        } else if (bits < 80) {
            strengthLevel = "😊 Good";
            strengthColor = neonSuccess;
        } else if (bits < 100) {
            strengthLevel = "😁 Strong";
            strengthColor = neonSuccess;
        } else {
            strengthLevel = "🔥 Excellent";
            strengthColor = neonAccent;
        }

        strengthText.setText(String.format("%s (%.1f bits entropy)", strengthLevel, bits));
        strengthText.setForeground(strengthColor);
    }

    private void animatePasswordReveal(String pwd) {
        if (glitchTimer != null && glitchTimer.isRunning())
            glitchTimer.stop();

        final int steps = 30;
        final char[] finalChars = pwd.toCharArray();
        final char[] working = new char[finalChars.length];
        Arrays.fill(working, '█');

        passwordLabel.setForeground(darkTheme ? neonAccent : new Color(40, 40, 40));

        glitchTimer = new javax.swing.Timer(40, null);
        glitchTimer.addActionListener(new ActionListener() {
            int tick = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                double progress = Math.min(1.0, tick / (double) steps);
                int reveal = (int) (finalChars.length * Math.pow(progress, 0.7)); // Ease-out curve

                for (int i = 0; i < finalChars.length; i++) {
                    if (i < reveal) {
                        working[i] = finalChars[i];
                    } else if (i < reveal + 3) {
                        working[i] = randomGlitchChar();
                    } else {
                        working[i] = '█';
                    }
                }

                passwordLabel.setText(new String(working));

                if (tick++ >= steps) {
                    passwordLabel.setText(pwd);
                    passwordLabel.setForeground(darkTheme ? neonAccent : new Color(20, 20, 20));
                    glitchTimer.stop();

                    // Brief highlight effect
                    javax.swing.Timer highlight = new javax.swing.Timer(100, null);
                    final int[] fade = { 255 };
                    highlight.addActionListener(ev -> {
                        fade[0] -= 15;
                        if (fade[0] <= 0) {
                            passwordLabel.setForeground(darkTheme ? neonAccent : new Color(20, 20, 20));
                            highlight.stop();
                        } else {
                            Color current = darkTheme ? neonAccent : new Color(20, 20, 20);
                            int alpha = Math.max(0, Math.min(255, fade[0]));
                            passwordLabel.setForeground(new Color(
                                    Math.min(255, current.getRed() + alpha / 3),
                                    Math.min(255, current.getGreen() + alpha / 3),
                                    Math.min(255, current.getBlue() + alpha / 3)));
                        }
                        passwordLabel.repaint();
                    });
                    highlight.start();
                }
            }
        });
        glitchTimer.start();
    }

    private char randomGlitchChar() {
        String pool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;:,.<>?█▓▒░";
        return pool.charAt(RAND.nextInt(pool.length()));
    }

    private void addToHistory(String pwd) {
        if (pwd.isEmpty())
            return;
        history.addFirst(pwd);
        while (history.size() > 10) // Increased history size
            history.removeLast();
        refreshHistoryUI();
    }

    private void refreshHistoryUI() {
        // retained for dialog population
        historyListPanel.removeAll();
        int index = 1;
        for (String p : history) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setOpaque(false);
            itemPanel.setBorder(new EmptyBorder(5, 8, 5, 8));
            JLabel indexLabel = new JLabel(String.valueOf(index++));
            indexLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
            indexLabel.setForeground(new Color(120, 120, 120));
            indexLabel.setPreferredSize(new Dimension(20, 20));
            JLabel item = new JLabel(p);
            item.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            item.setForeground(darkTheme ? new Color(180, 180, 180) : new Color(50, 50, 50));
            item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            item.setToolTipText("Click to copy: " + p);
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    item.setForeground(darkTheme ? neonAccentAlt : new Color(0, 90, 160));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    item.setForeground(darkTheme ? new Color(180, 180, 180) : new Color(50, 50, 50));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    copyToClipboard(p);
                    showTransientOverlay("📋 Copied!", item, 1200);
                }
            });
            itemPanel.add(indexLabel, BorderLayout.WEST);
            itemPanel.add(item, BorderLayout.CENTER);
            historyListPanel.add(itemPanel);
        }
        historyListPanel.revalidate();
        historyListPanel.repaint();
    }

    private void openHistoryDialog() {
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No history yet.", "History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        refreshHistoryUI();
        JDialog dlg = new JDialog(this, "Password History", false);
        dlg.setLayout(new BorderLayout());
        JScrollPane scroll = new JScrollPane(historyListPanel);
        scroll.setPreferredSize(new Dimension(480, 260));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        dlg.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        JButton saveHistoryBtn = smallButton("💾 Save");
        JButton closeBtn = smallButton("✖ Close");
        saveHistoryBtn.addActionListener(e -> {
            savePasswords();
        });
        closeBtn.addActionListener(e -> dlg.dispose());
        bottom.add(saveHistoryBtn);
        bottom.add(closeBtn);
        dlg.add(bottom, BorderLayout.SOUTH);

        dlg.getContentPane().setBackground(getContentPane().getBackground());
        dlg.pack();
        dlg.setLocationRelativeTo(this);
        dlg.setVisible(true);
    }

    private void copyCurrentPassword() {
        if (targetPassword == null || targetPassword.isEmpty()) {
            showTransientOverlay("⚠️ Generate a password first!", generateBtn, 1500);
            return;
        }
        copyToClipboard(targetPassword);
        showTransientOverlay("🎉 Password copied!", passwordLabel, 1200);
    }

    private void copyToClipboard(String text) {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(new StringSelection(text), null);
    }

    private void showTransientOverlay(String msg, Component relative, int durationMs) {
        JWindow toast = new JWindow(this);
        JLabel label = new JLabel(msg);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setOpaque(true);
        label.setBackground(new Color(20, 20, 20, 240));
        label.setForeground(neonAccent);
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(neonAccent, 1),
                new EmptyBorder(8, 16, 8, 16)));

        toast.add(label);
        toast.pack();
        Point p = relative.getLocationOnScreen();
        toast.setLocation(p.x + relative.getWidth() / 2 - toast.getWidth() / 2, p.y - toast.getHeight() - 10);
        toast.setVisible(true);

        // Fade out animation
        javax.swing.Timer fadeTimer = new javax.swing.Timer(50, null);
        final float[] alpha = { 1.0f };
        fadeTimer.addActionListener(e -> {
            alpha[0] -= 0.05f;
            if (alpha[0] <= 0) {
                toast.setVisible(false);
                toast.dispose();
                fadeTimer.stop();
            } else {
                toast.setOpacity(Math.max(0, alpha[0]));
            }
        });

        new javax.swing.Timer(durationMs - 1000, ev -> fadeTimer.start()) {
            {
                setRepeats(false);
            }
        }.start();

        new javax.swing.Timer(durationMs, ev -> {
            toast.setVisible(false);
            toast.dispose();
        }) {
            {
                setRepeats(false);
            }
        }.start();
    }

    private void savePasswords() {
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(this, "📝 No passwords in history to save.", "Nothing to Save",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("secure_passwords_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")) + ".txt"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, true))) {
                bw.write("=".repeat(60));
                bw.newLine();
                bw.write("🔐 Quantum Password Generator - Exported Passwords");
                bw.newLine();
                bw.write("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                bw.newLine();
                bw.write("=".repeat(60));
                bw.newLine();
                bw.newLine();

                int index = 1;
                for (String p : history) {
                    bw.write(String.format("%d. %s", index++, p));
                    bw.newLine();
                }

                bw.newLine();
                bw.write("⚠️  SECURITY NOTE: Store this file securely and delete when no longer needed.");
                bw.newLine();

                showTransientOverlay("💾 Saved to " + f.getName(), saveBtn, 2000);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error saving file: " + ex.getMessage(),
                        "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void applyTheme() {
        Color bg = darkTheme ? new Color(15, 15, 20) : new Color(248, 248, 252);
        Color text = darkTheme ? new Color(220, 220, 220) : new Color(40, 40, 40);
        Color accent = darkTheme ? neonAccent : new Color(0, 120, 180);
        getContentPane().setBackground(bg);
        for (Component c : getAllComponents()) {
            if (c instanceof JPanel)
                c.setBackground(bg);
            if (c instanceof JCheckBox)
                c.setForeground(text);
            if (c instanceof JLabel && c != passwordLabel && c != lengthValueLabel && c != strengthText)
                c.setForeground(text);
        }
        lengthValueLabel.setForeground(accent);
        repaint();
    }

    private List<Component> getAllComponents() {
        List<Component> list = new ArrayList<>();
        collectComponents(getContentPane(), list);
        return list;
    }

    private void collectComponents(Container c, List<Component> list) {
        for (Component comp : c.getComponents()) {
            list.add(comp);
            if (comp instanceof Container ct)
                collectComponents(ct, list);
        }
    }

    // ===== Enhanced Slider UI =====
    private class NeonSliderUI extends BasicSliderUI {
        public NeonSliderUI(JSlider b) {
            super(b);
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = 10;
            int x = trackRect.x + (trackRect.width - w) / 2;

            // Background track
            g2.setColor(new Color(40, 40, 40));
            g2.fillRoundRect(x, trackRect.y, w, trackRect.height, 8, 8);

            // Gradient fill
            float progress = (float) (slider.getValue() - slider.getMinimum()) /
                    (slider.getMaximum() - slider.getMinimum());
            int fillHeight = (int) (trackRect.height * progress);
            int fillY = trackRect.y + trackRect.height - fillHeight;

            if (fillHeight > 0) {
                GradientPaint gp = new GradientPaint(
                        0, fillY, neonAccent,
                        0, trackRect.y + trackRect.height, neonAccentAlt);
                g2.setPaint(gp);
                g2.fillRoundRect(x, fillY, w, fillHeight, 8, 8);

                // Glow effect
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.setColor(neonAccent);
                g2.fillRoundRect(x - 1, fillY, w + 2, fillHeight, 8, 8);
            }

            g2.dispose();
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 24;
            int cx = thumbRect.x + thumbRect.width / 2;
            int cy = thumbRect.y + thumbRect.height / 2;

            // Shadow
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            g2.setColor(Color.BLACK);
            g2.fillOval(cx - size / 2 + 2, cy - size / 2 + 2, size, size);

            // Main thumb
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            GradientPaint gp = new GradientPaint(
                    cx - size / 2, cy - size / 2, new Color(60, 60, 65),
                    cx + size / 2, cy + size / 2, new Color(40, 40, 45));
            g2.setPaint(gp);
            g2.fillOval(cx - size / 2, cy - size / 2, size, size);

            // Glowing border
            g2.setColor(neonAccent);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(cx - size / 2, cy - size / 2, size, size);

            // Inner highlight
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g2.setColor(Color.WHITE);
            g2.fillOval(cx - 4, cy - 6, 8, 6);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            new AppGui().setVisible(true);
        });
    }
}
