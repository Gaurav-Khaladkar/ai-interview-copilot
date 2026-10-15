package com.aicopilot.repository;

import com.aicopilot.model.ModelPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelPreferenceRepository extends JpaRepository<ModelPreference, String> {
    Optional<ModelPreference> findByUserId(String userId);
}
