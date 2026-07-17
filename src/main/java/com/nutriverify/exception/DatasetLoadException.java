package com.nutriverify.exception;

/**
 * Raised when bundled datasets cannot be loaded.
 */
public class DatasetLoadException extends NutriVerifyException {
    public DatasetLoadException(String message) {
        super(message);
    }
}
