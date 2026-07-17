package com.nutriverify.util;

/**
 * Central ANSI color helper for consistent terminal styling.
 */
public final class AnsiColors {
    private AnsiColors() {}

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";

    public static String success(String value) { return GREEN + value + RESET; }
    public static String warning(String value) { return YELLOW + value + RESET; }
    public static String danger(String value) { return RED + value + RESET; }
    public static String info(String value) { return CYAN + value + RESET; }
    public static String accent(String value) { return BLUE + value + RESET; }
}
