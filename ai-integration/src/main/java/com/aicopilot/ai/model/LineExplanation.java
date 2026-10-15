package com.aicopilot.ai.model;

public class LineExplanation {
    private int lineNumber;
    private String codeLine;
    private String explanation;

    public LineExplanation() {}

    public LineExplanation(int lineNumber, String codeLine, String explanation) {
        this.lineNumber = lineNumber;
        this.codeLine = codeLine;
        this.explanation = explanation;
    }

    public int getLineNumber() { return lineNumber; }
    public void setLineNumber(int lineNumber) { this.lineNumber = lineNumber; }

    public String getCodeLine() { return codeLine; }
    public void setCodeLine(String codeLine) { this.codeLine = codeLine; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}
