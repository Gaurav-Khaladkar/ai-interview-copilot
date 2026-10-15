package com.aicopilot.ai.model;

import java.util.ArrayList;
import java.util.List;

public class CodeAnalysisResponse {
    private String status; // "PASSED", "FAILED", "SYNTAX_ERROR", "TIMEOUT"
    private int testsPassed;
    private int totalTests;
    private long executionTimeMs;
    private double memoryUsedMb;
    private String timeComplexity;
    private String spaceComplexity;
    private String output;
    private String critique;
    private List<String> suggestions = new ArrayList<>();
    private String refactoredCode;

    public CodeAnalysisResponse() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTestsPassed() { return testsPassed; }
    public void setTestsPassed(int testsPassed) { this.testsPassed = testsPassed; }

    public int getTotalTests() { return totalTests; }
    public void setTotalTests(int totalTests) { this.totalTests = totalTests; }

    public long getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public double getMemoryUsedMb() { return memoryUsedMb; }
    public void setMemoryUsedMb(double memoryUsedMb) { this.memoryUsedMb = memoryUsedMb; }

    public String getTimeComplexity() { return timeComplexity; }
    public void setTimeComplexity(String timeComplexity) { this.timeComplexity = timeComplexity; }

    public String getSpaceComplexity() { return spaceComplexity; }
    public void setSpaceComplexity(String spaceComplexity) { this.spaceComplexity = spaceComplexity; }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getCritique() { return critique; }
    public void setCritique(String critique) { this.critique = critique; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public String getRefactoredCode() { return refactoredCode; }
    public void setRefactoredCode(String refactoredCode) { this.refactoredCode = refactoredCode; }
}
