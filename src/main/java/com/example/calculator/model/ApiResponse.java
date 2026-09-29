package com.example.calculator.model;

/**
 * Unified API response envelope.
 * Every endpoint returns the same structure so the front end
 * can parse responses in one way.
 */
public class ApiResponse {

    private boolean success;
    private String message;
    private String expression;
    private String result;

    private ApiResponse(boolean success, String message, String expression, String result) {
        this.success = success;
        this.message = message;
        this.expression = expression;
        this.result = result;
    }

    public static ApiResponse success(String expression, String result) {
        return new ApiResponse(true, null, expression, result);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message, null, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }
}
