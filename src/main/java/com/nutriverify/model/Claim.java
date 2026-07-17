package com.nutriverify.model;

/**
 * A single marketing claim attached to a food label.
 */
public class Claim {
    private final ClaimType type;
    private final String displayText;

    public Claim(ClaimType type, String displayText) {
        this.type = type;
        this.displayText = displayText;
    }

    public ClaimType getType() {
        return type;
    }

    public String getDisplayText() {
        return displayText;
    }
}
