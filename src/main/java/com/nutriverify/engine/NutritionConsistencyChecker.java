package com.nutriverify.engine;

import com.nutriverify.config.AppConfig;
import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.NutritionConsistencyFinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks nutrition facts for internal consistency and plausible ranges.
 */
public class NutritionConsistencyChecker {
    public List<NutritionConsistencyFinding> check(FoodLabel label) {
        List<NutritionConsistencyFinding> findings = new ArrayList<>();

        double expectedCalories = label.getFat() * 9 + label.getCarbs() * 4 + label.getProtein() * 4;
        double delta = Math.abs(expectedCalories - label.getCalories()) / Math.max(label.getCalories(), 1.0);
        if (delta > AppConfig.CALORIE_TOLERANCE) {
            findings.add(new NutritionConsistencyFinding(
                    "Calories do not closely align with fat/carbs/protein values (expected about " + Math.round(expectedCalories) + "kcal).",
                    true));
        } else {
            findings.add(new NutritionConsistencyFinding("Calories are broadly aligned with the macronutrients.", false));
        }

        if (label.getSugar() > label.getCarbs()) {
            findings.add(new NutritionConsistencyFinding("Sugar exceeds total carbohydrates, which is internally inconsistent.", true));
        } else {
            findings.add(new NutritionConsistencyFinding("Sugar does not exceed the reported carbs.", false));
        }

        if (label.getSodium() > AppConfig.SODIUM_PER_SERVING_LIMIT) {
            findings.add(new NutritionConsistencyFinding("Sodium is high for a single serving and may warrant caution.", true));
        } else {
            findings.add(new NutritionConsistencyFinding("Sodium appears within a reasonable range.", false));
        }

        return findings;
    }
}
