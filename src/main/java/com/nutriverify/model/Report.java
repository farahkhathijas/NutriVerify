package com.nutriverify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Report object rendered to the console and persisted to history.
 */
public class Report {
    private final String productName;
    private final String brand;
    private final LocalDateTime generatedAt;
    private final AnalysisResult analysisResult;
    private final List<String> historyLines;

    public Report(String productName, String brand, LocalDateTime generatedAt, AnalysisResult analysisResult) {
        this.productName = productName;
        this.brand = brand;
        this.generatedAt = generatedAt;
        this.analysisResult = analysisResult;
        this.historyLines = new ArrayList<>();
    }

    public String getProductName() { return productName; }
    public String getBrand() { return brand; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public AnalysisResult getAnalysisResult() { return analysisResult; }
    public List<String> getHistoryLines() { return historyLines; }
}
