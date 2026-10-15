package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "raw_text", columnDefinition = "TEXT")
    private String rawText;

    @Column(name = "quality_score")
    private int qualityScore;

    @Column(name = "suggestions_json", columnDefinition = "TEXT")
    private String suggestionsJson;

    @Column(name = "uploaded_at")
    private Instant uploadedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ResumeSkill> skills = new ArrayList<>();

    public Resume() {
        this.id = UUID.randomUUID().toString();
    }

    public Resume(User user, String fileName, String fileType, long fileSize, String rawText) {
        this();
        this.user = user;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.rawText = rawText;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public long getFileSize() { return fileSize; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }

    public String getRawText() { return rawText; }
    public void setRawText(String rawText) { this.rawText = rawText; }

    public int getQualityScore() { return qualityScore; }
    public void setQualityScore(int qualityScore) { this.qualityScore = qualityScore; }

    public String getSuggestionsJson() { return suggestionsJson; }
    public void setSuggestionsJson(String suggestionsJson) { this.suggestionsJson = suggestionsJson; }

    public Instant getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(Instant uploadedAt) { this.uploadedAt = uploadedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }

    public List<ResumeSkill> getSkills() { return skills; }
    public void setSkills(List<ResumeSkill> skills) { this.skills = skills; }

    public void addSkill(ResumeSkill skill) {
        skills.add(skill);
        skill.setResume(this);
    }
}
