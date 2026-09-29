package com.example.calculator.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Database entity mapped to the calculation_history table.
 * Persists every successful calculation in the back-end database.
 */
@Entity
@Table(name = "calculation_history")
public class CalculationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String expression;

    @Column(nullable = false, length = 64)
    private String result;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Required by JPA. */
    protected CalculationHistory() {
    }

    public CalculationHistory(String expression, String result) {
        this.expression = expression;
        this.result = result;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
