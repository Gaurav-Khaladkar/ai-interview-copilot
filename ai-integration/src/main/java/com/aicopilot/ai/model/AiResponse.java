package com.aicopilot.ai.model;

import java.util.ArrayList;
import java.util.List;

public class AiResponse {
    private String answer;
    private String codeSnippet;
    private List<LineExplanation> lineByLineExplanation = new ArrayList<>();
    private List<String> followUpQuestions = new ArrayList<>();
    private String conceptSummary;
    private List<String> learningSuggestions = new ArrayList<>();
    private String modeUsed; // "ONLINE_AI", "LOCAL_LLM", "OFFLINE_KNOWLEDGE_BASE"
    private String language;

    public AiResponse() {}

    public AiResponse(String answer, String modeUsed) {
        this.answer = answer;
        this.modeUsed = modeUsed;
    }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getCodeSnippet() { return codeSnippet; }
    public void setCodeSnippet(String codeSnippet) { this.codeSnippet = codeSnippet; }

    public List<LineExplanation> getLineByLineExplanation() { return lineByLineExplanation; }
    public void setLineByLineExplanation(List<LineExplanation> lineByLineExplanation) { this.lineByLineExplanation = lineByLineExplanation; }

    public List<String> getFollowUpQuestions() { return followUpQuestions; }
    public void setFollowUpQuestions(List<String> followUpQuestions) { this.followUpQuestions = followUpQuestions; }

    public String getConceptSummary() { return conceptSummary; }
    public void setConceptSummary(String conceptSummary) { this.conceptSummary = conceptSummary; }

    public List<String> getLearningSuggestions() { return learningSuggestions; }
    public void setLearningSuggestions(List<String> learningSuggestions) { this.learningSuggestions = learningSuggestions; }

    public String getModeUsed() { return modeUsed; }
    public void setModeUsed(String modeUsed) { this.modeUsed = modeUsed; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
