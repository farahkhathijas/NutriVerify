package com.nutriverify.exception;

/**
 * Raised when user-provided label data is incomplete or malformed.
 */
public class InvalidLabelDataException extends NutriVerifyException {
    public InvalidLabelDataException(String message) {
        super(message);
    }
}
