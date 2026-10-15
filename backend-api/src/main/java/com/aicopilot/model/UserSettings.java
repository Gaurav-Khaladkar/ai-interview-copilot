package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "target_domain")
    private String targetDomain = "Java Full Stack";

    @Column(name = "years_experience")
    private int yearsExperience = 2;

    @Column(name = "preferred_language")
    private String preferredLanguage = "English";

    @Column(name = "default_difficulty")
    private String defaultDifficulty = "Mid-Level";

    @Column(name = "auto_audio_playback")
    private boolean autoAudioPlayback = false;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    public UserSettings() {
        this.id = UUID.randomUUID().toString();
    }

    public UserSettings(User user) {
        this();
        this.user = user;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTargetDomain() { return targetDomain; }
    public void setTargetDomain(String targetDomain) { this.targetDomain = targetDomain; }

    public int getYearsExperience() { return yearsExperience; }
    public void setYearsExperience(int yearsExperience) { this.yearsExperience = yearsExperience; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public String getDefaultDifficulty() { return defaultDifficulty; }
    public void setDefaultDifficulty(String defaultDifficulty) { this.defaultDifficulty = defaultDifficulty; }

    public boolean isAutoAudioPlayback() { return autoAudioPlayback; }
    public void setAutoAudioPlayback(boolean autoAudioPlayback) { this.autoAudioPlayback = autoAudioPlayback; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
