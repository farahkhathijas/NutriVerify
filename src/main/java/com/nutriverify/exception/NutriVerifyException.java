package com.nutriverify.exception;

/**
 * Base exception for user-facing application failures.
 */
public class NutriVerifyException extends Exception {
    public NutriVerifyException(String message) {
        super(message);
    }

    public NutriVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}
