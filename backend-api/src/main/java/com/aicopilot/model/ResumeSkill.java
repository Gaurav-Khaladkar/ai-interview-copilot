package com.aicopilot.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "resume_skills")
public class ResumeSkill {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "skill_name", nullable = false)
    private String skillName;

    @Column(nullable = false)
    private String category;

    @Column(name = "proficiency_level")
    private String proficiencyLevel = "Intermediate";

    @Column(name = "years_experience")
    private int yearsExperience = 1;

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    public ResumeSkill() {
        this.id = UUID.randomUUID().toString();
    }

    public ResumeSkill(Resume resume, String skillName, String category, String proficiencyLevel) {
        this();
        this.resume = resume;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Resume getResume() { return resume; }
    public void setResume(Resume resume) { this.resume = resume; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getProficiencyLevel() { return proficiencyLevel; }
    public void setProficiencyLevel(String proficiencyLevel) { this.proficiencyLevel = proficiencyLevel; }

    public int getYearsExperience() { return yearsExperience; }
    public void setYearsExperience(int yearsExperience) { this.yearsExperience = yearsExperience; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
