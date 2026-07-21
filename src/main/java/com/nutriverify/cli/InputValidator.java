package com.nutriverify.cli;

/**
 * Functional interface for validating a single console input string.
 * Implementations return a {@link ValidationResult} describing either
 * acceptance or the error to display to the user.
 */
@FunctionalInterface
public interface InputValidator {
    ValidationResult validate(String input);

    /**
     * Convenience: a validator that only accepts non-blank input.
     */
    static InputValidator nonBlank() {
        return input -> input == null || input.isBlank()
            ? new ValidationResult(false, "Input cannot be empty.")
            : ValidationResult.ok();
    }

    /**
     * Convenience: a validator that checks an email-like pattern.
     */
    static InputValidator email() {
        return input -> {
            if (input == null || input.isBlank()) {
                return new ValidationResult(false, "Email cannot be empty.");
            }
            String trimmed = input.trim();
            int at = trimmed.indexOf('@');
            if (at <= 0 || at == trimmed.length() - 1
                    || trimmed.indexOf('@', at + 1) != -1
                    || !trimmed.substring(at + 1).contains(".")) {
                return new ValidationResult(false,
                    "Please enter a valid email (e.g. name@example.com).");
            }
            return ValidationResult.ok();
        };
    }

    /**
     * Convenience: a validator enforcing a minimum length.
     */
    static InputValidator minLength(int min) {
        return input -> (input == null || input.length() < min)
            ? new ValidationResult(false, "Must be at least " + min + " characters.")
            : ValidationResult.ok();
    }
}
