package com.nutriverify.engine.rules;

import com.nutriverify.config.AppConfig;
import com.nutriverify.engine.ClaimRule;
import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.ClaimType;
import com.nutriverify.model.ClaimVerdict;
import com.nutriverify.model.FoodLabel;

/**
 * Validates the Low Fat claim.
 */
public class LowFatRule implements ClaimRule {
    @Override
    public boolean supports(Claim claim) {
        return claim.getType() == ClaimType.LOW_FAT;
    }

    @Override
    public ClaimResult evaluate(FoodLabel label, Claim claim) {
        if (label.getFat() <= AppConfig.LOW_FAT_THRESHOLD) {
            return new ClaimResult(claim, ClaimVerdict.VERIFIED,
                    "Fat content is within the low-fat threshold of " + AppConfig.LOW_FAT_THRESHOLD + "g.");
        }
        return new ClaimResult(claim, ClaimVerdict.FALSE,
                "Fat content exceeds the low-fat threshold of " + AppConfig.LOW_FAT_THRESHOLD + "g.");
    }
}
