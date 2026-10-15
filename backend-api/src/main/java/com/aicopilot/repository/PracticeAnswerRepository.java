package com.aicopilot.repository;

import com.aicopilot.model.PracticeAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PracticeAnswerRepository extends JpaRepository<PracticeAnswer, String> {
    Optional<PracticeAnswer> findByQuestionId(String questionId);
}
