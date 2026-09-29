package com.example.calculator.service;

import com.example.calculator.model.CalculationHistory;
import com.example.calculator.repository.HistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Calculation history operations: query, delete one, clear all.
 */
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<CalculationHistory> getAllHistory() {
        return historyRepository.findAllByOrderByCreatedAtDesc();
    }

    public boolean deleteById(Long id) {
        if (!historyRepository.existsById(id)) {
            return false;
        }
        historyRepository.deleteById(id);
        return true;
    }

    public void clearAll() {
        historyRepository.deleteAll();
    }
}
