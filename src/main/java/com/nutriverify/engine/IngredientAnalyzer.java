package com.nutriverify.engine;

import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.Ingredient;
import com.nutriverify.model.IngredientCategory;
import com.nutriverify.model.IngredientRisk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Produces an ingredient-based risk profile for a label.
 */
public class IngredientAnalyzer {
    public List<IngredientRisk> analyze(FoodLabel label) {
        Map<IngredientCategory, Long> counts = label.getIngredients().stream()
                .map(Ingredient::getCategory)
                .collect(Collectors.groupingBy(category -> category, Collectors.counting()));

        List<IngredientRisk> risks = new ArrayList<>();
        for (Map.Entry<IngredientCategory, Long> entry : counts.entrySet()) {
            if (entry.getKey() != IngredientCategory.NATURAL) {
                risks.add(new IngredientRisk(entry.getKey(), entry.getValue()));
            }
        }
        return risks;
    }
}
