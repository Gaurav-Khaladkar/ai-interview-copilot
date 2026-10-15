package com.aicopilot.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PracticeReportDto {
    private String reportId;
    private String sessionId;
    private String jobRole;
    private String topic;
    private int overallScore;
    private int technicalScore;
    private int communicationScore;
    private List<String> strengths = new ArrayList<>();
    private List<String> weaknesses = new ArrayList<>();
    private List<String> recommendedTopics = new ArrayList<>();
    private Instant completedAt;

    public PracticeReportDto() {}

    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }

    public int getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(int technicalScore) { this.technicalScore = technicalScore; }

    public int getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(int communicationScore) { this.communicationScore = communicationScore; }

    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }

    public List<String> getWeaknesses() { return weaknesses; }
    public void setWeaknesses(List<String> weaknesses) { this.weaknesses = weaknesses; }

    public List<String> getRecommendedTopics() { return recommendedTopics; }
    public void setRecommendedTopics(List<String> recommendedTopics) { this.recommendedTopics = recommendedTopics; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
