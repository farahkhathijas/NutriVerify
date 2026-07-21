package com.nutriverify.cli;

/**
 * Immutable result of validating a single console input.
 */
public record ValidationResult(boolean valid, String error) {
    public static ValidationResult ok() {
        return new ValidationResult(true, "");
    }
}
