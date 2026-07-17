package com.nutriverify.engine;

import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.FoodLabel;
import com.nutriverify.model.ClaimVerdict;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates all claims using a strategy registry.
 */
public class ClaimValidator {
    private final List<ClaimRule> rules;

    public ClaimValidator(List<ClaimRule> rules) {
        this.rules = new ArrayList<>(rules);
    }

    public List<ClaimResult> validate(FoodLabel label) {
        List<ClaimResult> results = new ArrayList<>();
        for (Claim claim : label.getClaims()) {
            ClaimResult result = evaluateOne(label, claim);
            results.add(result);
        }
        return results;
    }

    private ClaimResult evaluateOne(FoodLabel label, Claim claim) {
        for (ClaimRule rule : rules) {
            if (rule.supports(claim)) {
                return rule.evaluate(label, claim);
            }
        }
        return new ClaimResult(claim, ClaimVerdict.SUSPICIOUS, "No dedicated validation rule available for this claim.");
    }
}
