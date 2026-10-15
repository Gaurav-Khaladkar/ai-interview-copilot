package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "practice_sessions")
public class PracticeSession {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "job_role", nullable = false)
    private String jobRole;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private String language = "English";

    @Column(name = "duration_minutes")
    private int durationMinutes = 30;

    @Column(nullable = false)
    private String status = "IN_PROGRESS"; // "IN_PROGRESS", "COMPLETED", "ABANDONED"

    @Column(name = "started_at")
    private Instant startedAt = Instant.now();

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PracticeQuestion> questions = new ArrayList<>();

    @OneToOne(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PracticeReport report;

    public PracticeSession() {
        this.id = UUID.randomUUID().toString();
    }

    public PracticeSession(User user, String jobRole, String topic, String difficulty, String language, int durationMinutes) {
        this();
        this.user = user;
        this.jobRole = jobRole;
        this.topic = topic;
        this.difficulty = difficulty;
        this.language = language;
        this.durationMinutes = durationMinutes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getStartedAt() { return startedAt; }
    public void setStartedAt(Instant startedAt) { this.startedAt = startedAt; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }

    public List<PracticeQuestion> getQuestions() { return questions; }
    public void setQuestions(List<PracticeQuestion> questions) { this.questions = questions; }

    public PracticeReport getReport() { return report; }
    public void setReport(PracticeReport report) { this.report = report; }

    public void addQuestion(PracticeQuestion question) {
        questions.add(question);
        question.setSession(this);
    }
}
