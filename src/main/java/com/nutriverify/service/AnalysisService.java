package com.nutriverify.service;

import com.nutriverify.engine.AuthenticityEngine;
import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.FoodLabel;

/**
 * Orchestrates the analysis workflow for a single label.
 */
public class AnalysisService {
    private final AuthenticityEngine authenticityEngine;

    public AnalysisService(AuthenticityEngine authenticityEngine) {
        this.authenticityEngine = authenticityEngine;
    }

    public AnalysisResult analyze(FoodLabel label) {
        return authenticityEngine.analyze(label);
    }
}
