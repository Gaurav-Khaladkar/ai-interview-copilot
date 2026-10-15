package com.aicopilot.repository;

import com.aicopilot.model.PracticeSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PracticeSessionRepository extends JpaRepository<PracticeSession, String> {
    List<PracticeSession> findByUserIdOrderByStartedAtDesc(String userId);
    Optional<PracticeSession> findByIdAndUserId(String id, String userId);
}
