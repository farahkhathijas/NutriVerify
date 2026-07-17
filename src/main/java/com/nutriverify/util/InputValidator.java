package com.nutriverify.util;

import java.util.Arrays;
import java.util.List;

/**
 * Helper methods for validating common console inputs.
 */
public final class InputValidator {
    private InputValidator() {}

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static List<String> splitIngredients(String input) {
        return Arrays.stream(input.split(",|\\n"))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .toList();
    }
}
