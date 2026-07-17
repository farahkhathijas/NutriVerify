package com.nutriverify.exception;

/**
 * Raised when a report cannot be exported to disk.
 */
public class ReportExportException extends NutriVerifyException {
    public ReportExportException(String message) {
        super(message);
    }
}
