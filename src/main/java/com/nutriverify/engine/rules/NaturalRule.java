package com.nutriverify.engine.rules;

import com.nutriverify.engine.ClaimRule;
import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.ClaimType;
import com.nutriverify.model.ClaimVerdict;
import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.Ingredient;
import com.nutriverify.model.IngredientCategory;

/**
 * Validates the 100% Natural claim.
 */
public class NaturalRule implements ClaimRule {
    @Override
    public boolean supports(Claim claim) {
        return claim.getType() == ClaimType.NATURAL;
    }

    @Override
    public ClaimResult evaluate(FoodLabel label, Claim claim) {
        for (Ingredient ingredient : label.getIngredients()) {
            if (ingredient.getCategory() == IngredientCategory.ARTIFICIAL || ingredient.getCategory() == IngredientCategory.GMO_DERIVED) {
                return new ClaimResult(claim, ClaimVerdict.FALSE,
                        "Contains artificial or synthetic ingredient '" + ingredient.getName() + "'.");
            }
        }
        return new ClaimResult(claim, ClaimVerdict.VERIFIED, "No artificial or synthetic ingredients were detected.");
    }
}
