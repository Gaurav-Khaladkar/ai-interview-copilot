package com.aicopilot.dto;

import jakarta.validation.constraints.NotBlank;

public class TranslationRequest {

    @NotBlank(message = "Text cannot be blank")
    private String text;

    @NotBlank(message = "Target language cannot be blank")
    private String targetLanguage = "Hindi";

    public TranslationRequest() {}

    public TranslationRequest(String text, String targetLanguage) {
        this.text = text;
        this.targetLanguage = targetLanguage;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }
}
