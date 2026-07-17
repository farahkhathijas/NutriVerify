package com.nutriverify.ui;

import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.IngredientRisk;
import com.nutriverify.model.NutritionConsistencyFinding;
import com.nutriverify.model.RiskLevel;
import com.nutriverify.util.AnsiColors;
import com.nutriverify.util.ConsoleTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Renders structured console output for the analysis report.
 */
public class ConsoleRenderer {
    public void printBanner() {
        System.out.println(AnsiColors.accent("┌────────────────────────────────────────────┐"));
        System.out.println(AnsiColors.accent("│") + AnsiColors.BOLD + AnsiColors.CYAN + "   NutriVerify" + AnsiColors.RESET + AnsiColors.accent("                    │"));
        System.out.println(AnsiColors.accent("│") + AnsiColors.CYAN + " AI-Powered Food Label Authenticity Analyzer" + AnsiColors.RESET + AnsiColors.accent(" │"));
        System.out.println(AnsiColors.accent("└────────────────────────────────────────────┘"));
    }

    public void printLoadingAnimation() {
        String[] steps = {"Loading nutrition datasets...", "Initializing authenticity engine...", "Preparing console experience..."};
        for (String step : steps) {
            System.out.print(AnsiColors.info("● " + step));
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println();
        }
    }

    public void printProgressBar(int value) {
        int filled = Math.max(0, Math.min(20, value / 5));
        StringBuilder builder = new StringBuilder();
        builder.append("[" );
        for (int i = 0; i < filled; i++) {
            builder.append("█");
        }
        for (int i = filled; i < 20; i++) {
            builder.append("░");
        }
        builder.append("] ").append(value).append("%");
        System.out.println(AnsiColors.info(builder.toString()));
    }

    public void printMenu() {
        System.out.println(AnsiColors.info("\nMain Menu"));
        System.out.println("1. Analyze a Food Label");
        System.out.println("2. Compare Two Products");
        System.out.println("3. Search Ingredient Database");
        System.out.println("4. View Report History");
        System.out.println("5. Export a Report");
        System.out.println("6. Configuration / Settings");
        System.out.println("7. About / Help");
        System.out.println("8. Exit");
    }

    public void printReport(AnalysisResult result) {
        System.out.println(AnsiColors.info("\nAnalysis Report"));
        System.out.println(AnsiColors.accent("Product: ") + result.getLabel().getProductName());
        System.out.println(AnsiColors.accent("Brand: ") + result.getLabel().getBrand());
        System.out.println();
        printScoreCard(result);
        System.out.println();
        printClaimTable(result.getClaimResults());
        System.out.println();
        printIngredients(result.getIngredientRisks());
        System.out.println();
        printNutrition(result.getNutritionFindings());
        System.out.println();
        printRecommendations(result.getRecommendations());
    }

    private void printScoreCard(AnalysisResult result) {
        String riskText = formatRisk(result.getRiskLevel());
        System.out.println(AnsiColors.accent("┌─────────────────────────────┐"));
        System.out.println(AnsiColors.accent("│") + AnsiColors.BOLD + " Authenticity Score " + AnsiColors.RESET + AnsiColors.accent("│"));
        System.out.println(AnsiColors.accent("│") + " " + result.getAuthenticityScore() + "/100 " + riskText + AnsiColors.accent(" │"));
        printProgressBar(result.getAuthenticityScore());
        System.out.println(AnsiColors.accent("└─────────────────────────────┘"));
    }

    private void printClaimTable(List<ClaimResult> claims) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Claim", "Verdict", "Reason"});
        for (ClaimResult claim : claims) {
            rows.add(new String[]{claim.getClaim().getDisplayText(), verdictSymbol(claim.getVerdict()), claim.getReason()});
        }
        System.out.println(AnsiColors.info("Claim Verdicts"));
        System.out.println(ConsoleTable.render(rows));
    }

    private void printIngredients(List<IngredientRisk> risks) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Category", "Count"});
        for (IngredientRisk risk : risks) {
            rows.add(new String[]{risk.getCategory().name(), String.valueOf(risk.getCount())});
        }
        System.out.println(AnsiColors.info("Ingredient Risk Breakdown"));
        System.out.println(ConsoleTable.render(rows));
    }

    private void printNutrition(List<NutritionConsistencyFinding> findings) {
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Finding", "Status"});
        for (NutritionConsistencyFinding finding : findings) {
            rows.add(new String[]{finding.getMessage(), finding.isProblematic() ? "⚠" : "✔"});
        }
        System.out.println(AnsiColors.info("Nutrition Consistency"));
        System.out.println(ConsoleTable.render(rows));
    }

    private void printRecommendations(List<String> recommendations) {
        System.out.println(AnsiColors.info("Recommendations"));
        for (String recommendation : recommendations) {
            System.out.println("• " + recommendation);
        }
    }

    private String verdictSymbol(com.nutriverify.model.ClaimVerdict verdict) {
        return switch (verdict) {
            case VERIFIED -> AnsiColors.success("✔") + " Verified";
            case SUSPICIOUS -> AnsiColors.warning("⚠") + " Suspicious";
            case FALSE -> AnsiColors.danger("✘") + " False";
        };
    }

    private String formatRisk(RiskLevel riskLevel) {
        return switch (riskLevel) {
            case TRUSTED -> AnsiColors.success("TRUSTED");
            case LOW_RISK -> AnsiColors.info("LOW RISK");
            case MODERATE_RISK -> AnsiColors.warning("MODERATE RISK");
            case HIGH_RISK -> AnsiColors.danger("HIGH RISK");
            case CRITICAL_RISK -> AnsiColors.danger("CRITICAL RISK");
        };
    }
}
