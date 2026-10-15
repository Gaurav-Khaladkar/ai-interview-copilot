package com.aicopilot.repository;

import com.aicopilot.model.PracticeQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PracticeQuestionRepository extends JpaRepository<PracticeQuestion, String> {
    List<PracticeQuestion> findBySessionIdOrderByQuestionOrderAsc(String sessionId);
    Optional<PracticeQuestion> findBySessionIdAndQuestionOrder(String sessionId, int questionOrder);
}
