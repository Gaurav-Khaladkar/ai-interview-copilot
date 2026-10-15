package com.aicopilot.ai.model;

import java.util.ArrayList;
import java.util.List;

public class CodeAnalysisRequest {
    private String language; // JAVA, PYTHON, JAVASCRIPT, SQL
    private String sourceCode;
    private String problemTitle;
    private String problemDescription;
    private List<String> testInputs = new ArrayList<>();
    private List<String> expectedOutputs = new ArrayList<>();

    public CodeAnalysisRequest() {}

    public CodeAnalysisRequest(String language, String sourceCode, String problemTitle) {
        this.language = language;
        this.sourceCode = sourceCode;
        this.problemTitle = problemTitle;
    }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getSourceCode() { return sourceCode; }
    public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }

    public String getProblemTitle() { return problemTitle; }
    public void setProblemTitle(String problemTitle) { this.problemTitle = problemTitle; }

    public String getProblemDescription() { return problemDescription; }
    public void setProblemDescription(String problemDescription) { this.problemDescription = problemDescription; }

    public List<String> getTestInputs() { return testInputs; }
    public void setTestInputs(List<String> testInputs) { this.testInputs = testInputs; }

    public List<String> getExpectedOutputs() { return expectedOutputs; }
    public void setExpectedOutputs(List<String> expectedOutputs) { this.expectedOutputs = expectedOutputs; }
}
