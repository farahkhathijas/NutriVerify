package com.nutriverify.engine.rules;

import com.nutriverify.config.AppConfig;
import com.nutriverify.engine.ClaimRule;
import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.ClaimType;
import com.nutriverify.model.ClaimVerdict;
import com.nutriverify.model.FoodLabel;

/**
 * Validates the High Protein claim.
 */
public class HighProteinRule implements ClaimRule {
    @Override
    public boolean supports(Claim claim) {
        return claim.getType() == ClaimType.HIGH_PROTEIN;
    }

    @Override
    public ClaimResult evaluate(FoodLabel label, Claim claim) {
        if (label.getProtein() >= AppConfig.HIGH_PROTEIN_THRESHOLD) {
            return new ClaimResult(claim, ClaimVerdict.VERIFIED,
                    "Protein content meets the high-protein threshold of " + AppConfig.HIGH_PROTEIN_THRESHOLD + "g.");
        }
        return new ClaimResult(claim, ClaimVerdict.FALSE,
                "Protein content falls below the high-protein threshold of " + AppConfig.HIGH_PROTEIN_THRESHOLD + "g.");
    }
}
