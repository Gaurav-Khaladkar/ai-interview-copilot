package com.aicopilot.dto;

import jakarta.validation.constraints.NotBlank;

public class PracticeSessionRequest {

    @NotBlank(message = "Job role is required")
    private String jobRole;

    @NotBlank(message = "Topic is required")
    private String topic;

    private String difficulty = "Mid-Level";
    private String language = "English";
    private int durationMinutes = 30;
    private String resumeId;

    public PracticeSessionRequest() {}

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getResumeId() { return resumeId; }
    public void setResumeId(String resumeId) { this.resumeId = resumeId; }
}
