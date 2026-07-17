package com.nutriverify.service;

import com.nutriverify.config.AppConfig;
import com.nutriverify.model.AnalysisResult;
import com.nutriverify.model.Report;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists report summaries to a local CSV history file.
 */
public class HistoryService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void save(Report report) throws IOException {
        Path path = Paths.get(AppConfig.HISTORY_PATH);
        Files.createDirectories(path.getParent());
        String line = String.join(",", report.getProductName(), report.getBrand(),
                report.getGeneratedAt().format(FORMATTER),
                String.valueOf(report.getAnalysisResult().getAuthenticityScore()),
                report.getAnalysisResult().getRiskLevel().name()) + System.lineSeparator();
        Files.writeString(path, line, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public List<String> loadHistoryLines() throws IOException {
        Path path = Paths.get(AppConfig.HISTORY_PATH);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}
