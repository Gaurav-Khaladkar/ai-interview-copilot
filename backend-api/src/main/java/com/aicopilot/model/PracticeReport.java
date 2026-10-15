package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "practice_reports")
public class PracticeReport {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private PracticeSession session;

    @Column(name = "overall_score")
    private int overallScore;

    @Column(name = "technical_score")
    private int technicalScore;

    @Column(name = "communication_score")
    private int communicationScore;

    @Column(name = "strengths_json", columnDefinition = "TEXT")
    private String strengthsJson;

    @Column(name = "weaknesses_json", columnDefinition = "TEXT")
    private String weaknessesJson;

    @Column(name = "recommended_topics_json", columnDefinition = "TEXT")
    private String recommendedTopicsJson;

    @Column(name = "generated_at")
    private Instant generatedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    public PracticeReport() {
        this.id = UUID.randomUUID().toString();
    }

    public PracticeReport(PracticeSession session, int overallScore, int technicalScore, int communicationScore) {
        this();
        this.session = session;
        this.overallScore = overallScore;
        this.technicalScore = technicalScore;
        this.communicationScore = communicationScore;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public PracticeSession getSession() { return session; }
    public void setSession(PracticeSession session) { this.session = session; }

    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }

    public int getTechnicalScore() { return technicalScore; }
    public void setTechnicalScore(int technicalScore) { this.technicalScore = technicalScore; }

    public int getCommunicationScore() { return communicationScore; }
    public void setCommunicationScore(int communicationScore) { this.communicationScore = communicationScore; }

    public String getStrengthsJson() { return strengthsJson; }
    public void setStrengthsJson(String strengthsJson) { this.strengthsJson = strengthsJson; }

    public String getWeaknessesJson() { return weaknessesJson; }
    public void setWeaknessesJson(String weaknessesJson) { this.weaknessesJson = weaknessesJson; }

    public String getRecommendedTopicsJson() { return recommendedTopicsJson; }
    public void setRecommendedTopicsJson(String recommendedTopicsJson) { this.recommendedTopicsJson = recommendedTopicsJson; }

    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
