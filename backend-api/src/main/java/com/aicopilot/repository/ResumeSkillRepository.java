package com.aicopilot.repository;

import com.aicopilot.model.ResumeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeSkillRepository extends JpaRepository<ResumeSkill, String> {
    List<ResumeSkill> findByResumeId(String resumeId);
}
