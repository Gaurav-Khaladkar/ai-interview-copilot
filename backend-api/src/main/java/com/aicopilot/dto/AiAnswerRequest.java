package com.aicopilot.dto;

import jakarta.validation.constraints.NotBlank;

public class AiAnswerRequest {

    @NotBlank(message = "Question text cannot be blank")
    private String question;

    private String domain = "Java";
    private String difficulty = "Mid-Level";
    private String format = "DETAILED"; // "SHORT" or "DETAILED"
    private String targetLanguage = "English";
    private boolean includeCodeExample = true;

    public AiAnswerRequest() {}

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
}
