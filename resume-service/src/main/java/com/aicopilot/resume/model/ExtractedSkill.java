package com.aicopilot.resume.model;

public class ExtractedSkill {
    private String name;
    private String category;
    private String proficiency; // "Foundational", "Intermediate", "Advanced"
    private int mentionCount;

    public ExtractedSkill() {}

    public ExtractedSkill(String name, String category, String proficiency, int mentionCount) {
        this.name = name;
        this.category = category;
        this.proficiency = proficiency;
        this.mentionCount = mentionCount;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getProficiency() { return proficiency; }
    public void setProficiency(String proficiency) { this.proficiency = proficiency; }

    public int getMentionCount() { return mentionCount; }
    public void setMentionCount(int mentionCount) { this.mentionCount = mentionCount; }
}
