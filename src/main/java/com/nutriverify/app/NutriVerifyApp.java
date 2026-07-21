package com.nutriverify.app;

import com.nutriverify.auth.AuthSession;
import com.nutriverify.cli.Console;
import com.nutriverify.config.AppConfig;
import com.nutriverify.engine.AuthenticityEngine;
import com.nutriverify.engine.ClaimValidator;
import com.nutriverify.engine.ComparisonEngine;
import com.nutriverify.engine.IngredientAnalyzer;
import com.nutriverify.engine.NutritionConsistencyChecker;
import com.nutriverify.engine.rules.HighProteinRule;
import com.nutriverify.engine.rules.LowFatRule;
import com.nutriverify.engine.rules.NaturalRule;
import com.nutriverify.engine.rules.NoAddedSugarRule;
import com.nutriverify.engine.rules.NonGmoRule;
import com.nutriverify.engine.rules.OrganicRule;
import com.nutriverify.exception.DatasetLoadException;
import com.nutriverify.exception.InvalidLabelDataException;
import com.nutriverify.exception.ReportExportException;
import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimType;
import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.Ingredient;
import com.nutriverify.model.IngredientCategory;
import com.nutriverify.model.Report;
import com.nutriverify.repository.IngredientDatasetRepository;
import com.nutriverify.service.AnalysisService;
import com.nutriverify.service.HistoryService;
import com.nutriverify.service.ReportService;
import com.nutriverify.ui.ConsoleRenderer;
import com.nutriverify.util.AnsiColors;
import com.nutriverify.util.InputValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Main authenticated application loop for NutriVerify. Receives the
 * active {@link AuthSession} so downstream modules can attribute work
 * to the logged-in user.
 */
public class NutriVerifyApp {
    private final Console console;
    private final Scanner scanner;
    private final AuthSession session;
    private final ConsoleRenderer renderer = new ConsoleRenderer();
    private final IngredientDatasetRepository repository = new IngredientDatasetRepository();
    private final AuthenticityEngine engine = new AuthenticityEngine(
            new ClaimValidator(List.of(
                    new NoAddedSugarRule(),
                    new LowFatRule(),
                    new HighProteinRule(),
                    new NaturalRule(),
                    new OrganicRule(),
                    new NonGmoRule())),
            new NutritionConsistencyChecker(),
            new IngredientAnalyzer());
    private final AnalysisService analysisService = new AnalysisService(engine);
    private final ReportService reportService = new ReportService();
    private final HistoryService historyService = new HistoryService();
    private final ComparisonEngine comparisonEngine = new ComparisonEngine();
    private Report currentReport;

    public NutriVerifyApp(Console console, Scanner scanner, AuthSession session) {
        this.console = console;
        this.scanner = scanner;
        this.session = session;
    }

    public void run() {
        String[] options = {
            "Analyze Food Product",
            "Ingredient Analysis",
            "Nutrition Checker",
            "Compare Products",
            "View History",
            "Export Reports",
            "User Profile",
            "Settings",
            "About NutriVerify",
            "Logout"
        };
        while (true) {
            Integer choice = console.readMenuChoice("Dashboard", options);
            if (choice == null) {
                return;
            }
            switch (choice) {
                case 1 -> handleAnalyzeLabel();
                case 2 -> handleIngredientSearch();
                case 3 -> handleNutritionChecker();
                case 4 -> handleCompareProducts();
                case 5 -> handleHistory();
                case 6 -> handleExport();
                case 7 -> handleProfile();
                case 8 -> handleSettings();
                case 9 -> handleAbout();
                case 10 -> {
                    System.out.println(AnsiColors.success("Logging out. Goodbye, "
                        + session.displayName() + "!"));
                    return;
                }
                default -> System.out.println(AnsiColors.warning("Invalid selection."));
            }
        }
    }

    private void handleAnalyzeLabel() {
        try {
            FoodLabel label = promptForLabel();
            if (label == null) return;
            AnalysisResult result = analysisService.analyze(label);
            currentReport = reportService.createReport(result);
            renderer.printReport(result);
            historyService.save(currentReport);
            System.out.println(AnsiColors.success("Analysis saved to history."));
        } catch (Exception ex) {
            System.out.println(AnsiColors.danger("Analysis failed: " + ex.getMessage()));
        }
    }

    private void handleCompareProducts() {
        try {
            System.out.println(AnsiColors.info("\n── First Product ──"));
            FoodLabel first = promptForLabel();
            if (first == null) return;
            System.out.println(AnsiColors.info("\n── Second Product ──"));
            FoodLabel second = promptForLabel();
            if (second == null) return;
            AnalysisResult firstResult = analysisService.analyze(first);
            AnalysisResult secondResult = analysisService.analyze(second);
            System.out.println(AnsiColors.info("\nComparison Result"));
            System.out.println(comparisonEngine.compare(firstResult, secondResult));
            for (String difference : comparisonEngine.differences(firstResult, secondResult)) {
                System.out.println("- " + difference);
            }
        } catch (Exception ex) {
            System.out.println(AnsiColors.danger("Comparison failed: " + ex.getMessage()));
        }
    }

    private void handleIngredientSearch() {
        try {
            String query = console.readLine("Enter ingredient name or partial term");
            if (query == null) return;
            List<Ingredient> matches = repository.search(query);
            if (matches.isEmpty()) {
                System.out.println(AnsiColors.warning("No matches found."));
                return;
            }
            for (Ingredient ingredient : matches) {
                System.out.println(ingredient.getName() + " -> " + ingredient.getCategory() + " :: " + ingredient.getNote());
            }
        } catch (DatasetLoadException ex) {
            System.out.println(AnsiColors.danger(ex.getMessage()));
        }
    }

    private void handleNutritionChecker() {
        System.out.println(AnsiColors.info("\n── Nutrition Checker ──"));
        System.out.println(AnsiColors.warning(
            "  This module will be implemented in Module 5 (Nutrition Validator)."));
        System.out.println(AnsiColors.info(
            "  For now, use \"Analyze Food Product\" to get a full nutrition consistency check."));
    }

    private void handleHistory() {
        try {
            List<String> lines = historyService.loadHistoryLines();
            if (lines.isEmpty()) {
                System.out.println(AnsiColors.warning("No report history yet."));
                return;
            }
            System.out.println(AnsiColors.info("Report History"));
            for (String line : lines) {
                System.out.println("• " + line);
            }
        } catch (IOException ex) {
            System.out.println(AnsiColors.danger("Unable to view history: " + ex.getMessage()));
        }
    }

    private void handleExport() {
        if (currentReport == null) {
            System.out.println(AnsiColors.warning("No report is currently loaded. Analyze a label first."));
            return;
        }
        try {
            String extension = console.readLine("Choose extension (.txt, .csv, .json)");
            if (extension == null) return;
            extension = extension.trim().toLowerCase(Locale.ROOT);
            if (!extension.startsWith(".")) {
                extension = "." + extension;
            }
            Path path = reportService.exportReport(currentReport, extension);
            System.out.println(AnsiColors.success("Report exported to " + path));
        } catch (ReportExportException ex) {
            System.out.println(AnsiColors.danger(ex.getMessage()));
        }
    }

    private void handleProfile() {
        System.out.println(AnsiColors.info("\n── User Profile ──"));
        System.out.println(AnsiColors.accent("Name   : ") + session.getUser().getName());
        System.out.println(AnsiColors.accent("Email  : ") + session.getUser().getEmail());
        System.out.println(AnsiColors.accent("Member : ") + session.getUser().getCreatedAt().toLocalDate());
        System.out.println(AnsiColors.accent("Login  : ") + session.getLoginAt());
    }

    private void handleSettings() {
        System.out.println(AnsiColors.info("\n── Configuration / Settings ──"));
        System.out.println("Current thresholds: low-fat <= " + AppConfig.LOW_FAT_THRESHOLD
            + "g, high-protein >= " + AppConfig.HIGH_PROTEIN_THRESHOLD + "g");
        System.out.println(AnsiColors.warning("  Runtime settings editing arrives in Module 12 (Settings)."));
    }

    private void handleAbout() {
        System.out.println(AnsiColors.info("\n── About NutriVerify ──"));
        System.out.println(AnsiColors.bold(AppConfig.APP_NAME + " v" + AppConfig.APP_VERSION));
        System.out.println(AnsiColors.accent(AppConfig.APP_TAGLINE));
        System.out.println();
        System.out.println("Terminal-based food-label authenticity analyzer with claim");
        System.out.println("validation, nutrition checks, and report history.");
        System.out.println();
        System.out.println(AnsiColors.accent("Logged in as: ") + session.displayName());
    }

    private FoodLabel promptForLabel() throws InvalidLabelDataException {
        String productName = console.readLine("Product name");
        if (productName == null) return null;
        String brand = console.readLine("Brand");
        if (brand == null) return null;
        String servingSize = console.readLine("Serving size");
        if (servingSize == null) return null;

        String ingredientInput = console.readLine("Ingredients (comma-separated)");
        if (ingredientInput == null) return null;
        List<String> ingredientNames = InputValidator.splitIngredients(ingredientInput);
        if (ingredientNames.isEmpty()) {
            ingredientNames = List.of("unknown ingredient");
        }

        String claimsInput = console.readLine("Claims (comma-separated, e.g. No Added Sugar,Low Fat)");
        if (claimsInput == null) return null;
        List<Claim> claims = parseClaims(claimsInput);

        Double calories = console.readDouble("Calories", 0, 100000);
        if (calories == null) return null;
        Double fat = console.readDouble("Fat (g)", 0, 1000);
        if (fat == null) return null;
        Double sugar = console.readDouble("Sugar (g)", 0, 1000);
        if (sugar == null) return null;
        Double sodium = console.readDouble("Sodium (mg)", 0, 100000);
        if (sodium == null) return null;
        Double protein = console.readDouble("Protein (g)", 0, 1000);
        if (protein == null) return null;
        Double carbs = console.readDouble("Carbs (g)", 0, 1000);
        if (carbs == null) return null;
        Double fiber = console.readDouble("Fiber (g)", 0, 1000);
        if (fiber == null) return null;

        if (InputValidator.isBlank(productName) || InputValidator.isBlank(brand)) {
            throw new InvalidLabelDataException("Product name and brand are required.");
        }

        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientName : ingredientNames) {
            ingredients.add(mapIngredient(ingredientName));
        }

        return new FoodLabel(productName, brand, servingSize, ingredients, claims,
            calories, fat, sugar, sodium, protein, carbs, fiber);
    }

    private List<Claim> parseClaims(String input) {
        List<Claim> claims = new ArrayList<>();
        if (InputValidator.isBlank(input)) {
            return claims;
        }
        for (String part : input.split(",")) {
            String trimmed = part.trim();
            ClaimType type = mapClaimType(trimmed);
            claims.add(new Claim(type, trimmed));
        }
        return claims;
    }

    private ClaimType mapClaimType(String text) {
        String normalized = text.toLowerCase(Locale.ROOT);
        if (normalized.contains("sugar")) return ClaimType.NO_ADDED_SUGAR;
        if (normalized.contains("fat")) return ClaimType.LOW_FAT;
        if (normalized.contains("protein")) return ClaimType.HIGH_PROTEIN;
        if (normalized.contains("natural")) return ClaimType.NATURAL;
        if (normalized.contains("organic")) return ClaimType.ORGANIC;
        return ClaimType.NON_GMO;
    }

    private Ingredient mapIngredient(String name) {
        String normalized = name.toLowerCase(Locale.ROOT);
        if (normalized.contains("artificial") || normalized.contains("flavor")) {
            return new Ingredient(name, IngredientCategory.ARTIFICIAL, "Artificial component");
        }
        if (normalized.contains("sugar") || normalized.contains("syrup") || normalized.contains("sweet")) {
            return new Ingredient(name, IngredientCategory.SWEETENER, "Sweetener");
        }
        if (normalized.contains("preserv")) {
            return new Ingredient(name, IngredientCategory.PRESERVATIVE, "Preservative");
        }
        if (normalized.contains("soy") || normalized.contains("corn")) {
            return new Ingredient(name, IngredientCategory.GMO_DERIVED, "Potential GMO-derived source");
        }
        if (normalized.contains("milk") || normalized.contains("peanut") || normalized.contains("wheat")) {
            return new Ingredient(name, IngredientCategory.ALLERGEN, "Common allergen");
        }
        return new Ingredient(name, IngredientCategory.NATURAL, "Natural ingredient");
    }
}
