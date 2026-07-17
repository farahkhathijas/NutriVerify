package com.nutriverify.model;

import java.util.List;

/**
 * Final analysis output for a single label.
 */
public class AnalysisResult {
    private final FoodLabel label;
    private final List<ClaimResult> claimResults;
    private final List<NutritionConsistencyFinding> nutritionFindings;
    private final List<IngredientRisk> ingredientRisks;
    private final List<String> recommendations;
    private final int authenticityScore;
    private final RiskLevel riskLevel;

    public AnalysisResult(FoodLabel label, List<ClaimResult> claimResults,
                          List<NutritionConsistencyFinding> nutritionFindings,
                          List<IngredientRisk> ingredientRisks,
                          List<String> recommendations, int authenticityScore, RiskLevel riskLevel) {
        this.label = label;
        this.claimResults = claimResults;
        this.nutritionFindings = nutritionFindings;
        this.ingredientRisks = ingredientRisks;
        this.recommendations = recommendations;
        this.authenticityScore = authenticityScore;
        this.riskLevel = riskLevel;
    }

    public FoodLabel getLabel() { return label; }
    public List<ClaimResult> getClaimResults() { return claimResults; }
    public List<NutritionConsistencyFinding> getNutritionFindings() { return nutritionFindings; }
    public List<IngredientRisk> getIngredientRisks() { return ingredientRisks; }
    public List<String> getRecommendations() { return recommendations; }
    public int getAuthenticityScore() { return authenticityScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
}
