package com.nutriverify.engine;

import com.nutriverify.config.AppConfig;
import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.IngredientRisk;
import com.nutriverify.model.NutritionConsistencyFinding;
import com.nutriverify.model.RiskLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Aggregates claim validation, nutrition checks, and ingredient risks into a final score.
 */
public class AuthenticityEngine {
    private final ClaimValidator claimValidator;
    private final NutritionConsistencyChecker nutritionConsistencyChecker;
    private final IngredientAnalyzer ingredientAnalyzer;

    public AuthenticityEngine(ClaimValidator claimValidator,
                             NutritionConsistencyChecker nutritionConsistencyChecker,
                             IngredientAnalyzer ingredientAnalyzer) {
        this.claimValidator = claimValidator;
        this.nutritionConsistencyChecker = nutritionConsistencyChecker;
        this.ingredientAnalyzer = ingredientAnalyzer;
    }

    public AnalysisResult analyze(FoodLabel label) {
        List<ClaimResult> claimResults = claimValidator.validate(label);
        List<NutritionConsistencyFinding> nutritionFindings = nutritionConsistencyChecker.check(label);
        List<IngredientRisk> ingredientRisks = ingredientAnalyzer.analyze(label);

        int claimScore = scoreClaims(claimResults);
        int nutritionScore = scoreNutrition(nutritionFindings);
        int ingredientScore = scoreIngredients(ingredientRisks);

        int authenticityScore = (int) Math.round(claimScore * AppConfig.CLAIM_WEIGHT
                + nutritionScore * AppConfig.NUTRITION_WEIGHT
                + ingredientScore * AppConfig.INGREDIENT_WEIGHT);

        List<String> recommendations = new ArrayList<>();
        for (ClaimResult result : claimResults) {
            if (result.getVerdict() != com.nutriverify.model.ClaimVerdict.VERIFIED) {
                recommendations.add("Claim '" + result.getClaim().getDisplayText() + "' appears questionable: " + result.getReason());
            }
        }
        for (NutritionConsistencyFinding finding : nutritionFindings) {
            if (finding.isProblematic()) {
                recommendations.add("Nutrition concern: " + finding.getMessage());
            }
        }
        if (!ingredientRisks.isEmpty()) {
            recommendations.add("Ingredient profile includes concerning categories such as artificial or GMO-derived ingredients.");
        }

        RiskLevel riskLevel = mapScoreToRiskLevel(authenticityScore);
        return new AnalysisResult(label, claimResults, nutritionFindings, ingredientRisks, recommendations, authenticityScore, riskLevel);
    }

    private int scoreClaims(List<ClaimResult> claimResults) {
        if (claimResults.isEmpty()) {
            return 100;
        }
        int verifiedCount = 0;
        for (ClaimResult result : claimResults) {
            if (result.getVerdict() == com.nutriverify.model.ClaimVerdict.VERIFIED) {
                verifiedCount++;
            }
        }
        return (int) Math.round((double) verifiedCount / claimResults.size() * 100.0);
    }

    private int scoreNutrition(List<NutritionConsistencyFinding> findings) {
        long problematic = findings.stream().filter(NutritionConsistencyFinding::isProblematic).count();
        int score = 100 - (int) Math.round(problematic * 25.0);
        return Math.max(0, score);
    }

    private int scoreIngredients(List<IngredientRisk> risks) {
        int score = 100;
        for (IngredientRisk risk : risks) {
            score -= 15;
        }
        return Math.max(0, score);
    }

    private RiskLevel mapScoreToRiskLevel(int score) {
        if (score >= AppConfig.TRUSTED_MIN_SCORE) return RiskLevel.TRUSTED;
        if (score >= AppConfig.LOW_RISK_MIN_SCORE) return RiskLevel.LOW_RISK;
        if (score >= AppConfig.MODERATE_RISK_MIN_SCORE) return RiskLevel.MODERATE_RISK;
        if (score >= AppConfig.HIGH_RISK_MIN_SCORE) return RiskLevel.HIGH_RISK;
        return RiskLevel.CRITICAL_RISK;
    }
}
