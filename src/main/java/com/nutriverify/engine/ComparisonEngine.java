package com.nutriverify.engine;

import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.IngredientRisk;
import com.nutriverify.model.RiskLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Compares two product analyses and identifies the more trustworthy option.
 */
public class ComparisonEngine {
    public String compare(AnalysisResult left, AnalysisResult right) {
        if (left.getAuthenticityScore() == right.getAuthenticityScore()) {
            return "Both products score equally, so the choice is a tie.";
        }
        if (left.getAuthenticityScore() > right.getAuthenticityScore()) {
            return left.getLabel().getProductName() + " is more trustworthy because it has the higher authenticity score.";
        }
        return right.getLabel().getProductName() + " is more trustworthy because it has the higher authenticity score.";
    }

    public List<String> differences(AnalysisResult left, AnalysisResult right) {
        List<String> differences = new ArrayList<>();
        differences.add("Score: " + left.getAuthenticityScore() + " vs " + right.getAuthenticityScore());
        differences.add("Risk: " + left.getRiskLevel() + " vs " + right.getRiskLevel());
        return differences;
    }
}
