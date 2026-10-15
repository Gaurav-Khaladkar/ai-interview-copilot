package com.aicopilot.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class CodeExecutionRequestDto {

    @NotBlank(message = "Language is required")
    private String language; // "JAVA", "PYTHON", "JAVASCRIPT", "SQL"

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    private String problemId;
    private long timeoutMs = 5000;

    public CodeExecutionRequestDto() {}

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public String getProblemId() { return problemId; }
    public void setProblemId(String problemId) { this.problemId = problemId; }

    public long getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(long timeoutMs) { this.timeoutMs = timeoutMs; }
}
