package com.nutriverify.cli;

import com.nutriverify.config.AppConfig;
import com.nutriverify.util.AnsiColors;

/**
 * Renders the NutriVerify startup banner and boot sequence.
 */
public final class Banner {
    private Banner() {}

    /**
     * Prints the application banner box.
     */
    public static void print() {
        String title = AppConfig.APP_NAME + " v" + AppConfig.APP_VERSION;
        String tagline = "AI Food Label Authenticity Analyzer";
        String edition = "Enterprise CLI Edition";

        int width = 54;
        String top = "╔" + "═".repeat(width) + "╗";
        String bottom = "╚" + "═".repeat(width) + "╝";

        System.out.println(AnsiColors.accent(top));
        System.out.println(AnsiColors.accent("║") + center(title, width) + AnsiColors.accent("║"));
        System.out.println(AnsiColors.accent("║") + center(tagline, width) + AnsiColors.accent("║"));
        System.out.println(AnsiColors.accent("║") + center(edition, width) + AnsiColors.accent("║"));
        System.out.println(AnsiColors.accent(bottom));
    }

    /**
     * Prints the staged boot sequence used at startup.
     */
    public static void printBootSequence() {
        System.out.println();
        String[] steps = {
            "Loading User Module",
            "Loading Ingredient Dataset",
            "Loading Analysis Engine",
            "Loading Report Engine",
            "Preparing Terminal UI"
        };
        for (int i = 0; i < steps.length; i++) {
            System.out.print(AnsiColors.info("  ▸ " + steps[i] + "..."));
            pause(120);
            System.out.print("\r");
            System.out.println(AnsiColors.success("  ✔ " + steps[i] + "  "));
        }
        System.out.println(AnsiColors.bold(AnsiColors.CYAN) + "  System Ready" + AnsiColors.RESET);
        System.out.println();
    }

    private static String center(String text, int width) {
        int padding = width - text.length();
        int left = padding / 2;
        int right = padding - left;
        return " ".repeat(Math.max(0, left)) + text + " ".repeat(Math.max(0, right));
    }

    private static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
