package com.nutriverify.app;

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
 * Main application loop for NutriVerify.
 */
public class NutriVerifyApp {
    private final ConsoleRenderer renderer = new ConsoleRenderer();
    private final Scanner scanner = new Scanner(System.in);
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

    public void run() {
        renderer.printBanner();
        renderer.printLoadingAnimation();
        renderer.printProgressBar(100);
        System.out.println(AnsiColors.info("Welcome! Type a menu option and press Enter."));

        while (true) {
            renderer.printMenu();
            System.out.print("Select an option: ");
            String input = scanner.nextLine().trim();
            if (InputValidator.isBlank(input)) {
                System.out.println(AnsiColors.warning("Please choose a menu option."));
                continue;
            }

            switch (input) {
                case "1" -> handleAnalyzeLabel();
                case "2" -> handleCompareProducts();
                case "3" -> handleIngredientSearch();
                case "4" -> handleHistory();
                case "5" -> handleExport();
                case "6" -> handleSettings();
                case "7" -> handleAbout();
                case "8" -> {
                    System.out.println(AnsiColors.success("Goodbye!"));
                    return;
                }
                default -> System.out.println(AnsiColors.warning("Invalid selection. Please choose 1-8."));
            }
        }
    }

    private void handleAnalyzeLabel() {
        try {
            FoodLabel label = promptForLabel();
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
            FoodLabel first = promptForLabel();
            FoodLabel second = promptForLabel();
            AnalysisResult firstResult = analysisService.analyze(first);
            AnalysisResult secondResult = analysisService.analyze(second);
            System.out.println(AnsiColors.info("Comparison Result"));
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
            System.out.print("Enter ingredient name or partial term: ");
            String query = scanner.nextLine().trim();
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
            System.out.print("Choose extension (.txt, .csv, .json): ");
            String extension = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
            if (!extension.startsWith(".")) {
                extension = "." + extension;
            }
            Path path = reportService.exportReport(currentReport, extension);
            System.out.println(AnsiColors.success("Report exported to " + path));
        } catch (ReportExportException ex) {
            System.out.println(AnsiColors.danger(ex.getMessage()));
        }
    }

    private void handleSettings() {
        System.out.println(AnsiColors.info("Configuration / Settings"));
        System.out.println("Current thresholds: low-fat <= " + com.nutriverify.config.AppConfig.LOW_FAT_THRESHOLD + "g, high-protein >= " + com.nutriverify.config.AppConfig.HIGH_PROTEIN_THRESHOLD + "g");
    }

    private void handleAbout() {
        System.out.println(AnsiColors.info(AppConfig.APP_NAME));
        System.out.println("Terminal-based food-label authenticity analyzer with claim validation, nutrition checks, and report history.");
    }

    private FoodLabel promptForLabel() throws InvalidLabelDataException {
        System.out.print("Product name: ");
        String productName = scanner.nextLine().trim();
        System.out.print("Brand: ");
        String brand = scanner.nextLine().trim();
        System.out.print("Serving size: ");
        String servingSize = scanner.nextLine().trim();

        System.out.print("Ingredients (comma-separated): ");
        String ingredientInput = scanner.nextLine().trim();
        List<String> ingredientNames = InputValidator.splitIngredients(ingredientInput);
        if (ingredientNames.isEmpty()) {
            ingredientNames = List.of("unknown ingredient");
        }

        System.out.print("Claims (comma-separated, e.g. No Added Sugar,Low Fat): ");
        String claimsInput = scanner.nextLine().trim();
        List<Claim> claims = parseClaims(claimsInput);
        System.out.print("Calories: ");
        String caloriesInput = scanner.nextLine().trim();
        System.out.print("Fat (g): ");
        String fatInput = scanner.nextLine().trim();
        System.out.print("Sugar (g): ");
        String sugarInput = scanner.nextLine().trim();
        System.out.print("Sodium (mg): ");
        String sodiumInput = scanner.nextLine().trim();
        System.out.print("Protein (g): ");
        String proteinInput = scanner.nextLine().trim();
        System.out.print("Carbs (g): ");
        String carbsInput = scanner.nextLine().trim();
        System.out.print("Fiber (g): ");
        String fiberInput = scanner.nextLine().trim();

        if (InputValidator.isBlank(productName) || InputValidator.isBlank(brand)) {
            throw new InvalidLabelDataException("Product name and brand are required.");
        }

        double calories = parseDouble(caloriesInput, "calories");
        double fat = parseDouble(fatInput, "fat");
        double sugar = parseDouble(sugarInput, "sugar");
        double sodium = parseDouble(sodiumInput, "sodium");
        double protein = parseDouble(proteinInput, "protein");
        double carbs = parseDouble(carbsInput, "carbs");
        double fiber = parseDouble(fiberInput, "fiber");

        List<Ingredient> ingredients = new ArrayList<>();
        for (String ingredientName : ingredientNames) {
            ingredients.add(mapIngredient(ingredientName));
        }

        return new FoodLabel(productName, brand, servingSize, ingredients, claims, calories, fat, sugar, sodium, protein, carbs, fiber);
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

    private double parseDouble(String value, String label) throws InvalidLabelDataException {
        if (InputValidator.isBlank(value) || !InputValidator.isDouble(value)) {
            throw new InvalidLabelDataException("Please enter a valid numeric value for " + label + ".");
        }
        return Double.parseDouble(value);
    }
}
