package com.aicopilot.dto;

public class SettingsDto {
    private String targetDomain;
    private int yearsExperience;
    private String preferredLanguage;
    private String defaultDifficulty;
    private boolean autoAudioPlayback;

    // Application Preferences
    private String theme;
    private double windowOpacity;
    private boolean alwaysOnTop;
    private int windowWidth;
    private int windowHeight;
    private int windowX;
    private int windowY;
    private String activeMode; // "ONLINE" or "OFFLINE"

    // Model Preferences
    private String onlineProvider;
    private String onlineModelName;
    private String localProvider;
    private String localModelName;
    private String localEndpointUrl;
    private String whisperModel;

    public SettingsDto() {}

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

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public double getWindowOpacity() { return windowOpacity; }
    public void setWindowOpacity(double windowOpacity) { this.windowOpacity = windowOpacity; }

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

    public String getOnlineProvider() { return onlineProvider; }
    public void setOnlineProvider(String onlineProvider) { this.onlineProvider = onlineProvider; }

    public String getOnlineModelName() { return onlineModelName; }
    public void setOnlineModelName(String onlineModelName) { this.onlineModelName = onlineModelName; }

    public String getLocalProvider() { return localProvider; }
    public void setLocalProvider(String localProvider) { this.localProvider = localProvider; }

    public String getLocalModelName() { return localModelName; }
    public void setLocalModelName(String localModelName) { this.localModelName = localModelName; }

    public String getLocalEndpointUrl() { return localEndpointUrl; }
    public void setLocalEndpointUrl(String localEndpointUrl) { this.localEndpointUrl = localEndpointUrl; }

    public String getWhisperModel() { return whisperModel; }
    public void setWhisperModel(String whisperModel) { this.whisperModel = whisperModel; }
}
