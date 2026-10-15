package com.aicopilot.speech.model;

public class TranscriptionResult {
    private String text;
    private double confidence;
    private String language;
    private double durationSeconds;
    private boolean isOffline;
    private String provider;

    public TranscriptionResult() {}

    public TranscriptionResult(String text, double confidence, String language, double durationSeconds, boolean isOffline, String provider) {
        this.text = text;
        this.confidence = confidence;
        this.language = language;
        this.durationSeconds = durationSeconds;
        this.isOffline = isOffline;
        this.provider = provider;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public double getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(double durationSeconds) { this.durationSeconds = durationSeconds; }

    public boolean isOffline() { return isOffline; }
    public void setOffline(boolean offline) { isOffline = offline; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}
