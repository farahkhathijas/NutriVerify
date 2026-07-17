package com.nutriverify.model;

/**
 * A single nutrition consistency issue or note.
 */
public class NutritionConsistencyFinding {
    private final String message;
    private final boolean problematic;

    public NutritionConsistencyFinding(String message, boolean problematic) {
        this.message = message;
        this.problematic = problematic;
    }

    public String getMessage() {
        return message;
    }

    public boolean isProblematic() {
        return problematic;
    }
}
