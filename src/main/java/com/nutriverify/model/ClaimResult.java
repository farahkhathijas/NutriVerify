package com.nutriverify.model;

/**
 * Result payload from validating a marketing claim.
 */
public class ClaimResult {
    private final Claim claim;
    private final ClaimVerdict verdict;
    private final String reason;

    public ClaimResult(Claim claim, ClaimVerdict verdict, String reason) {
        this.claim = claim;
        this.verdict = verdict;
        this.reason = reason;
    }

    public Claim getClaim() {
        return claim;
    }

    public ClaimVerdict getVerdict() {
        return verdict;
    }

    public String getReason() {
        return reason;
    }
}
