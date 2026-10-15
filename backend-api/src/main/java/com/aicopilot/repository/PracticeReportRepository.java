package com.aicopilot.repository;

import com.aicopilot.model.PracticeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeReportRepository extends JpaRepository<PracticeReport, String> {
    Optional<PracticeReport> findBySessionId(String sessionId);
}
