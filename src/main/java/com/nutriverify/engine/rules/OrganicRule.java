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
 * Validates the Organic claim.
 */
public class OrganicRule implements ClaimRule {
    @Override
    public boolean supports(Claim claim) {
        return claim.getType() == ClaimType.ORGANIC;
    }

    @Override
    public ClaimResult evaluate(FoodLabel label, Claim claim) {
        for (Ingredient ingredient : label.getIngredients()) {
            if (ingredient.getCategory() == IngredientCategory.ARTIFICIAL || ingredient.getCategory() == IngredientCategory.GMO_DERIVED || ingredient.getCategory() == IngredientCategory.PRESERVATIVE) {
                return new ClaimResult(claim, ClaimVerdict.FALSE,
                        "Contains non-organic or synthetic ingredient '" + ingredient.getName() + "'.");
            }
        }
        return new ClaimResult(claim, ClaimVerdict.VERIFIED, "No obvious non-organic additives were detected.");
    }
}
