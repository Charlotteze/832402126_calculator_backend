package com.example.calculator.util;

/**
 * Thrown when an expression cannot be evaluated,
 * e.g. invalid syntax, division by zero, unmatched parentheses.
 */
public class InvalidExpressionException extends RuntimeException {

    public InvalidExpressionException(String message) {
        super(message);
    }
}
