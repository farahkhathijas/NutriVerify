package com.nutriverify.service;

import com.nutriverify.config.AppConfig;
import com.nutriverify.exception.ReportExportException;
import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.Report;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates reports and exports them to disk.
 */
public class ReportService {
    public Report createReport(AnalysisResult analysisResult) {
        return new Report(
                analysisResult.getLabel().getProductName(),
                analysisResult.getLabel().getBrand(),
                LocalDateTime.now(),
                analysisResult);
    }

    public Path exportReport(Report report, String extension) throws ReportExportException {
        Path directory = Paths.get(AppConfig.EXPORTS_DIRECTORY);
        try {
            Files.createDirectories(directory);
        } catch (IOException ex) {
            throw new ReportExportException("Unable to create export directory: " + ex.getMessage());
        }

        String filename = sanitize(report.getProductName()) + "-" + report.getGeneratedAt().toLocalDate() + extension;
        Path target = directory.resolve(filename);
        try {
            Files.writeString(target, renderText(report), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new ReportExportException("Unable to write export file: " + ex.getMessage());
        }
        return target;
    }

    private String renderText(Report report) {
        StringBuilder builder = new StringBuilder();
        builder.append("NutriVerify Report\n");
        builder.append("Product: ").append(report.getProductName()).append("\n");
        builder.append("Brand: ").append(report.getBrand()).append("\n");
        builder.append("Score: ").append(report.getAnalysisResult().getAuthenticityScore()).append("\n");
        builder.append("Risk: ").append(report.getAnalysisResult().getRiskLevel()).append("\n");
        for (String recommendation : report.getAnalysisResult().getRecommendations()) {
            builder.append("- ").append(recommendation).append("\n");
        }
        return builder.toString();
    }

    private String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "-").toLowerCase();
    }
}
