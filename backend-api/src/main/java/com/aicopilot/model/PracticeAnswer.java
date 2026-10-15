package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "practice_answers")
public class PracticeAnswer {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, unique = true)
    private PracticeQuestion question;

    @Column(name = "user_answer", columnDefinition = "TEXT")
    private String userAnswer;

    @Column(name = "input_mode", length = 20)
    private String inputMode = "TEXT"; // "TEXT" or "VOICE"

    @Column(name = "transcription_text", columnDefinition = "TEXT")
    private String transcriptionText;

    @Column(name = "score_accuracy")
    private int scoreAccuracy;

    @Column(name = "score_completeness")
    private int scoreCompleteness;

    @Column(name = "score_communication")
    private int scoreCommunication;

    @Column(name = "ai_feedback", columnDefinition = "TEXT")
    private String aiFeedback;

    @Column(name = "ideal_answer_sample", columnDefinition = "TEXT")
    private String idealAnswerSample;

    @Column(name = "submitted_at")
    private Instant submittedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    public PracticeAnswer() {
        this.id = UUID.randomUUID().toString();
    }

    public PracticeAnswer(PracticeQuestion question, String userAnswer, String inputMode) {
        this();
        this.question = question;
        this.userAnswer = userAnswer;
        this.inputMode = inputMode;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public PracticeQuestion getQuestion() { return question; }
    public void setQuestion(PracticeQuestion question) { this.question = question; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }

    public String getInputMode() { return inputMode; }
    public void setInputMode(String inputMode) { this.inputMode = inputMode; }

    public String getTranscriptionText() { return transcriptionText; }
    public void setTranscriptionText(String transcriptionText) { this.transcriptionText = transcriptionText; }

    public int getScoreAccuracy() { return scoreAccuracy; }
    public void setScoreAccuracy(int scoreAccuracy) { this.scoreAccuracy = scoreAccuracy; }

    public int getScoreCompleteness() { return scoreCompleteness; }
    public void setScoreCompleteness(int scoreCompleteness) { this.scoreCompleteness = scoreCompleteness; }

    public int getScoreCommunication() { return scoreCommunication; }
    public void setScoreCommunication(int scoreCommunication) { this.scoreCommunication = scoreCommunication; }

    public String getAiFeedback() { return aiFeedback; }
    public void setAiFeedback(String aiFeedback) { this.aiFeedback = aiFeedback; }

    public String getIdealAnswerSample() { return idealAnswerSample; }
    public void setIdealAnswerSample(String idealAnswerSample) { this.idealAnswerSample = idealAnswerSample; }

    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
