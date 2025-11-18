package com.scentalux.exception;

// Custom exception for order validation errors
public class OrderValidationException extends RuntimeException {
    public OrderValidationException(String message) {
        super(message);
    }
}