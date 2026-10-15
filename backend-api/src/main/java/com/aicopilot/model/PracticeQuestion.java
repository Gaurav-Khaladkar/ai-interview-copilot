package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "practice_questions")
public class PracticeQuestion {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private PracticeSession session;

    @Column(name = "question_order", nullable = false)
    private int questionOrder;

    @Column(name = "question_type")
    private String questionType = "TECHNICAL";

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "expected_keywords", columnDefinition = "TEXT")
    private String expectedKeywords;

    @Column(length = 50)
    private String difficulty;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PracticeAnswer answer;

    public PracticeQuestion() {
        this.id = UUID.randomUUID().toString();
    }

    public PracticeQuestion(PracticeSession session, int questionOrder, String questionText, String questionType) {
        this();
        this.session = session;
        this.questionOrder = questionOrder;
        this.questionText = questionText;
        this.questionType = questionType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public PracticeSession getSession() { return session; }
    public void setSession(PracticeSession session) { this.session = session; }

    public int getQuestionOrder() { return questionOrder; }
    public void setQuestionOrder(int questionOrder) { this.questionOrder = questionOrder; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getExpectedKeywords() { return expectedKeywords; }
    public void setExpectedKeywords(String expectedKeywords) { this.expectedKeywords = expectedKeywords; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }

    public PracticeAnswer getAnswer() { return answer; }
    public void setAnswer(PracticeAnswer answer) { this.answer = answer; }
}
