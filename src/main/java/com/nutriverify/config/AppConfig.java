package com.nutriverify.config;

/**
 * Central configuration constants for scoring, thresholds, and filenames.
 */
public final class AppConfig {
    private AppConfig() {}

    public static final String APP_NAME = "NutriVerify";
    public static final String APP_VERSION = "2.0";
    public static final String APP_TAGLINE = "Beyond the Printed Claim.";
    public static final String DATASET_PATH = "src/main/resources/ingredients.csv";
    public static final String HISTORY_PATH = "data/history.csv";
    public static final String USERS_PATH = "data/users.csv";
    public static final String EXPORTS_DIRECTORY = "exports";

    public static final double CLAIM_WEIGHT = 0.50;
    public static final double NUTRITION_WEIGHT = 0.30;
    public static final double INGREDIENT_WEIGHT = 0.20;

    public static final double LOW_FAT_THRESHOLD = 3.0;
    public static final double HIGH_PROTEIN_THRESHOLD = 10.0;
    public static final double SUGAR_CARBS_RATIO_LIMIT = 0.50;
    public static final double SODIUM_PER_SERVING_LIMIT = 400.0;

    public static final int TRUSTED_MIN_SCORE = 85;
    public static final int LOW_RISK_MIN_SCORE = 65;
    public static final int MODERATE_RISK_MIN_SCORE = 40;
    public static final int HIGH_RISK_MIN_SCORE = 20;

    public static final double CALORIE_TOLERANCE = 0.15;
}