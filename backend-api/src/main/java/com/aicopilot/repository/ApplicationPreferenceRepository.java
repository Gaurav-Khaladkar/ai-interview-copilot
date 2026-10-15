package com.aicopilot.repository;

import com.aicopilot.model.ApplicationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationPreferenceRepository extends JpaRepository<ApplicationPreference, String> {
    Optional<ApplicationPreference> findByUserId(String userId);
}
