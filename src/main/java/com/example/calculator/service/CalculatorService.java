package com.example.calculator.service;

import com.example.calculator.model.CalculationHistory;
import com.example.calculator.model.ApiResponse;
import com.example.calculator.repository.HistoryRepository;
import com.example.calculator.util.ExpressionEvaluator;
import com.example.calculator.util.InvalidExpressionException;
import org.springframework.stereotype.Service;

/**
 * Calculation business logic:
 * 1. evaluate the expression on the back end (never on the front end)
 * 2. persist every successful calculation
 * 3. return a unified response
 */
@Service
public class CalculatorService {

    private final HistoryRepository historyRepository;

    public CalculatorService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public ApiResponse calculate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new InvalidExpressionException("Expression is empty");
        }
        String result = ExpressionEvaluator.evaluate(expression);
        historyRepository.save(new CalculationHistory(expression, result));
        return ApiResponse.success(expression, result);
    }
}
