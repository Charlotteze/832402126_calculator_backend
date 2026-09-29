package com.example.calculator.controller;

import com.example.calculator.model.ApiResponse;
import com.example.calculator.model.CalculationHistory;
import com.example.calculator.service.CalculatorService;
import com.example.calculator.service.HistoryService;
import com.example.calculator.util.InvalidExpressionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * REST API entry points.
 *
 * <pre>
 *   POST   /api/calculate          calculate and store history
 *   GET    /api/history            list history (newest first)
 *   DELETE /api/history/{id}       delete one record
 *   DELETE /api/history            clear all records (extended feature)
 * </pre>
 */
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final CalculatorService calculatorService;
    private final HistoryService historyService;

    public CalculatorController(CalculatorService calculatorService, HistoryService historyService) {
        this.calculatorService = calculatorService;
        this.historyService = historyService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse> calculate(@RequestBody Map<String, String> body) {
        String expression = body.get("expression");
        try {
            return ResponseEntity.ok(calculatorService.calculate(expression));
        } catch (InvalidExpressionException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<CalculationHistory>> getHistory() {
        return ResponseEntity.ok(historyService.getAllHistory());
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<ApiResponse> deleteHistory(@PathVariable Long id) {
        boolean deleted = historyService.deleteById(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("History record not found: " + id));
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> clearHistory() {
        historyService.clearAll();
        return ResponseEntity.noContent().build();
    }
}
