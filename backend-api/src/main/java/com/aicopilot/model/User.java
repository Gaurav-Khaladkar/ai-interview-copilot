package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    @Column(nullable = false)
    private String role = "ROLE_CANDIDATE";

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserSettings settings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ApplicationPreference preferences;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ModelPreference modelPreferences;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String email, String passwordHash, String fullName) {
        this();
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }

    public UserSettings getSettings() { return settings; }
    public void setSettings(UserSettings settings) { this.settings = settings; }

    public ApplicationPreference getPreferences() { return preferences; }
    public void setPreferences(ApplicationPreference preferences) { this.preferences = preferences; }

    public ModelPreference getModelPreferences() { return modelPreferences; }
    public void setModelPreferences(ModelPreference modelPreferences) { this.modelPreferences = modelPreferences; }
}
