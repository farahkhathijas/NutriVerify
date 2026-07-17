package com.nutriverify.engine;

import com.nutriverify.model.Claim;
import com.nutriverify.model.ClaimResult;
import com.nutriverify.model.FoodLabel;

/**
 * Strategy interface for validating a given marketing claim.
 */
public interface ClaimRule {
    boolean supports(Claim claim);

    ClaimResult evaluate(FoodLabel label, Claim claim);
}
