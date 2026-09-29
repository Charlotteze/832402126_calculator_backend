package com.example.calculator.repository;

import com.example.calculator.model.CalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository.
 * CRUD operations are provided automatically; only custom queries
 * need to be declared here.
 */
@Repository
public interface HistoryRepository extends JpaRepository<CalculationHistory, Long> {

    /** History sorted newest first. */
    List<CalculationHistory> findAllByOrderByCreatedAtDesc();
}
