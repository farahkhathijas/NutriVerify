package com.nutriverify.engine.rules;

import com.nutriverify.engine.ClaimRule;
import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.ClaimType;
import com.nutriverify.model.ClaimVerdict;
import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.Ingredient;
import com.nutriverify.model.IngredientCategory;

import java.util.Locale;

/**
 * Validates the No Added Sugar claim.
 */
public class NoAddedSugarRule implements ClaimRule {
    private static final String[] SUGAR_INGREDIENTS = {"sugar", "sucrose", "dextrose", "corn syrup", "high-fructose corn syrup"};

    @Override
    public boolean supports(Claim claim) {
        return claim.getType() == ClaimType.NO_ADDED_SUGAR;
    }

    @Override
    public ClaimResult evaluate(FoodLabel label, Claim claim) {
        for (Ingredient ingredient : label.getIngredients()) {
            String normalized = ingredient.getName().toLowerCase(Locale.ROOT);
            for (String sugarIngredient : SUGAR_INGREDIENTS) {
                if (normalized.contains(sugarIngredient)) {
                    return new ClaimResult(claim, ClaimVerdict.FALSE,
                            "Contains sugar-related ingredient '" + ingredient.getName() + "'.");
                }
            }
            if (ingredient.getCategory() == IngredientCategory.SWEETENER) {
                return new ClaimResult(claim, ClaimVerdict.FALSE,
                        "Contains sweetener ingredient '" + ingredient.getName() + "'.");
            }
        }
        return new ClaimResult(claim, ClaimVerdict.VERIFIED, "No sugar-related ingredients were detected.");
    }
}
