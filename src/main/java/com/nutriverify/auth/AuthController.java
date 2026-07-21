package com.nutriverify.auth;

import com.nutriverify.cli.Console;
import com.nutriverify.cli.InputValidator;
import com.nutriverify.util.AnsiColors;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Interactive authentication menu (Register / Login / About / Exit).
 * Returned {@link AuthSession} drives the rest of the application.
 */
public final class AuthController {
    private static final DateTimeFormatter LOGIN_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Console console;
    private final AuthService authService;

    public AuthController(Console console, AuthService authService) {
        this.console = console;
        this.authService = authService;
    }

    /**
     * Runs the pre-login menu until the user either logs in or exits.
     * Returns the authenticated session, or {@code null} if the user
     * chose to exit.
     */
    public AuthSession run() {
        String[] options = {
            "Register",
            "Login",
            "About NutriVerify",
            "Exit"
        };
        while (true) {
            Integer choice = console.readMenuChoice("NutriVerify - Welcome", options);
            if (choice == null) {
                return null;
            }
            switch (choice) {
                case 1 -> {
                    AuthSession session = handleRegister();
                    if (session != null) return session;
                }
                case 2 -> {
                    AuthSession session = handleLogin();
                    if (session != null) return session;
                }
                case 3 -> printAbout();
                case 4 -> {
                    System.out.println(AnsiColors.success("Goodbye!"));
                    return null;
                }
                default -> System.out.println(AnsiColors.warning("Invalid selection."));
            }
        }
    }

    private AuthSession handleRegister() {
        System.out.println(AnsiColors.info("\n── Register New Account ──"));
        String name = console.readValidated("Full name", InputValidator.nonBlank());
        if (name == null) return null;

        String email = console.readValidated("Email", InputValidator.email());
        if (email == null) return null;

        String password = console.readPassword("Password (min 6 chars)");
        if (password == null) return null;
        if (password.length() < 6) {
            System.out.println(AnsiColors.warning("Password must be at least 6 characters."));
            return null;
        }

        String confirm = console.readPassword("Confirm password");
        if (confirm == null) return null;

        try {
            User user = authService.register(name, email, password, confirm);
            System.out.println(AnsiColors.success("  ✔ Account created successfully for "
                + user.getName() + "."));
            System.out.println(AnsiColors.info("  Please log in with your new credentials."));
            return null;
        } catch (Exception ex) {
            System.out.println(AnsiColors.danger("  ✘ " + ex.getMessage()));
            return null;
        }
    }

    private AuthSession handleLogin() {
        System.out.println(AnsiColors.info("\n── Login ──"));
        String email = console.readValidated("Email", InputValidator.email());
        if (email == null) return null;

        String password = console.readPassword("Password");
        if (password == null) return null;

        try {
            AuthSession session = authService.login(email, password);
            System.out.println();
            System.out.println(AnsiColors.success("  ✔ Login successful."));
            System.out.println(AnsiColors.info("  Welcome back, "
                + AnsiColors.bold(AnsiColors.CYAN + session.displayName() + AnsiColors.RESET) + "."));
            System.out.println(AnsiColors.info("  Session started at "
                + session.getLoginAt().format(LOGIN_FMT) + "."));
            return session;
        } catch (Exception ex) {
            System.out.println(AnsiColors.danger("  ✘ " + ex.getMessage()));
            return null;
        }
    }

    private void printAbout() {
        System.out.println();
        System.out.println(AnsiColors.info("── About NutriVerify ──"));
        System.out.println("NutriVerify is an AI-powered Food Label Authenticity Analyzer.");
        System.out.println("It verifies nutritional claims, detects misleading information,");
        System.out.println("analyzes ingredients, evaluates health risks, and generates a");
        System.out.println("professional authenticity report - all from your terminal.");
        System.out.println();
        System.out.println(AnsiColors.accent("Enterprise CLI Edition"));
        System.out.println();
    }
}
