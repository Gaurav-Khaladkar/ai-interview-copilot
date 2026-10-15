package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "application_preferences")
public class ApplicationPreference {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 20)
    private String theme = "DARK";

    @Column(name = "window_opacity")
    private double windowOpacity = 1.0;

    @Column(name = "always_on_top")
    private boolean alwaysOnTop = false;

    @Column(name = "window_width")
    private int windowWidth = 1280;

    @Column(name = "window_height")
    private int windowHeight = 820;

    @Column(name = "window_x")
    private int windowX = 100;

    @Column(name = "window_y")
    private int windowY = 100;

    @Column(name = "active_mode", length = 20)
    private String activeMode = "OFFLINE"; // "ONLINE" or "OFFLINE"

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    public ApplicationPreference() {
        this.id = UUID.randomUUID().toString();
    }

    public ApplicationPreference(User user) {
        this();
        this.user = user;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public double getWindowOpacity() { return windowOpacity; }
    public void setWindowOpacity(double windowOpacity) {
        // Enforce 30% to 100% boundary
        this.windowOpacity = Math.max(0.30, Math.min(1.0, windowOpacity));
    }

    public boolean isAlwaysOnTop() { return alwaysOnTop; }
    public void setAlwaysOnTop(boolean alwaysOnTop) { this.alwaysOnTop = alwaysOnTop; }

    public int getWindowWidth() { return windowWidth; }
    public void setWindowWidth(int windowWidth) { this.windowWidth = windowWidth; }

    public int getWindowHeight() { return windowHeight; }
    public void setWindowHeight(int windowHeight) { this.windowHeight = windowHeight; }

    public int getWindowX() { return windowX; }
    public void setWindowX(int windowX) { this.windowX = windowX; }

    public int getWindowY() { return windowY; }
    public void setWindowY(int windowY) { this.windowY = windowY; }

    public String getActiveMode() { return activeMode; }
    public void setActiveMode(String activeMode) { this.activeMode = activeMode; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
