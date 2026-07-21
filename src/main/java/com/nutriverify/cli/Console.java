package com.nutriverify.cli;

import com.nutriverify.util.AnsiColors;

import java.util.Scanner;

/**
 * Thin wrapper over {@link Scanner} providing styled prompts, validated
 * re-prompting, and EOF-safe reads. All interactive console I/O in the
 * application should go through this class so input handling is uniform
 * and a single bad value never aborts an entire flow.
 */
public final class Console {
    private static final String PROMPT_ARROW = AnsiColors.accent("» ");
    private static final String EOF_MESSAGE = AnsiColors.danger("Input closed (EOF). Returning to previous menu.");

    private final Scanner scanner;

    public Console(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Reads a non-blank line, re-prompting until the user enters one.
     * Returns {@code null} if the stream is closed (EOF), which callers
     * should treat as a signal to abort the current flow.
     */
    public String readLine(String prompt) {
        while (true) {
            System.out.print(PROMPT_ARROW + AnsiColors.info(prompt) + " ");
            if (!scanner.hasNextLine()) {
                System.out.println("\n" + EOF_MESSAGE);
                return null;
            }
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println(AnsiColors.warning("  Input cannot be empty. Please try again."));
        }
    }

    /**
     * Reads a line that must satisfy {@code validator}; re-prompts on
     * invalid input with the validator's error message.
     */
    public String readValidated(String prompt, InputValidator validator) {
        while (true) {
            String line = readLine(prompt);
            if (line == null) {
                return null;
            }
            ValidationResult result = validator.validate(line);
            if (result.valid()) {
                return line;
            }
            System.out.println(AnsiColors.warning("  " + result.error()));
        }
    }

    /**
     * Reads a double in the closed range {@code [min, max]}.
     */
    public Double readDouble(String prompt, double min, double max) {
        while (true) {
            String line = readLine(prompt);
            if (line == null) {
                return null;
            }
            try {
                double value = Double.parseDouble(line);
                if (value < min || value > max) {
                    System.out.println(AnsiColors.warning(
                        "  Value must be between " + min + " and " + max + "."));
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println(AnsiColors.warning("  Please enter a valid number."));
            }
        }
    }

    /**
     * Reads an integer in the closed range {@code [min, max]}.
     */
    public Integer readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            if (line == null) {
                return null;
            }
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.println(AnsiColors.warning(
                        "  Value must be between " + min + " and " + max + "."));
                    continue;
                }
                return value;
            } catch (NumberFormatException ex) {
                System.out.println(AnsiColors.warning("  Please enter a whole number."));
            }
        }
    }

    /**
     * Reads a yes/no answer.
     */
    public Boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt + " (y/n)");
            if (line == null) {
                return null;
            }
            String normalized = line.toLowerCase().trim();
            if (normalized.equals("y") || normalized.equals("yes")) {
                return true;
            }
            if (normalized.equals("n") || normalized.equals("no")) {
                return false;
            }
            System.out.println(AnsiColors.warning("  Please answer 'y' or 'n'."));
        }
    }

    /**
     * Reads a password-style line (echoed as typed; real masking needs
     * {@link System#console()} which is unavailable in many IDEs).
     */
    public String readPassword(String prompt) {
        System.out.print(PROMPT_ARROW + AnsiColors.info(prompt) + " ");
        if (!scanner.hasNextLine()) {
            System.out.println("\n" + EOF_MESSAGE);
            return null;
        }
        return scanner.nextLine().trim();
    }

    /**
     * Prints a boxed menu title and options, returns the selected index
     * (1-based) within {@code [1, options.length]}.
     */
    public Integer readMenuChoice(String title, String[] options) {
        System.out.println();
        System.out.println(AnsiColors.BOLD + AnsiColors.CYAN + "┌─ " + title + " "
                + "─".repeat(Math.max(0, 44 - title.length())) + "┐" + AnsiColors.RESET);
        for (int i = 0; i < options.length; i++) {
            String line = String.format("  %s%2d.%s %s",
                AnsiColors.accent(""), (i + 1), AnsiColors.RESET, options[i]);
            System.out.println(line);
        }
        System.out.println(AnsiColors.BOLD + AnsiColors.CYAN
                + "└" + "─".repeat(47) + "┘" + AnsiColors.RESET);
        return readInt("Select an option", 1, options.length);
    }
}
