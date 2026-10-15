package com.aicopilot.dto;

public class TranslationResponse {
    private String translatedText;
    private String originalText;
    private String targetLanguage;

    public TranslationResponse() {}

    public TranslationResponse(String translatedText, String originalText, String targetLanguage) {
        this.translatedText = translatedText;
        this.originalText = originalText;
        this.targetLanguage = targetLanguage;
    }

    public String getTranslatedText() { return translatedText; }
    public void setTranslatedText(String translatedText) { this.translatedText = translatedText; }

    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }

    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }
}
