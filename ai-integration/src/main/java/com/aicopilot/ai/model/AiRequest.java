package com.aicopilot.ai.model;

import java.util.List;

public class AiRequest {
    private String question;
    private String domain;
    private String difficulty;
    private String format; // "SHORT" or "DETAILED"
    private String targetLanguage; // "English", "Hindi", "Marathi"
    private boolean includeCodeExample;
    private String contextCode;

    public AiRequest() {}

    public AiRequest(String question, String domain, String difficulty, String format, String targetLanguage, boolean includeCodeExample) {
        this.question = question;
        this.domain = domain;
        this.difficulty = difficulty;
        this.format = format;
        this.targetLanguage = targetLanguage;
        this.includeCodeExample = includeCodeExample;
    }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    public String getTargetLanguage() { return targetLanguage; }
    public void setTargetLanguage(String targetLanguage) { this.targetLanguage = targetLanguage; }

    public boolean isIncludeCodeExample() { return includeCodeExample; }
    public void setIncludeCodeExample(boolean includeCodeExample) { this.includeCodeExample = includeCodeExample; }

    public String getContextCode() { return contextCode; }
    public void setContextCode(String contextCode) { this.contextCode = contextCode; }
}
