package com.dz.myfinance.repositories;

import com.dz.myfinance.models.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {

    Optional<FinancialGoal> findById(Long id);
    List<FinancialGoal> findByUserId(Long userId);
    List<FinancialGoal> findByType(String type);
}