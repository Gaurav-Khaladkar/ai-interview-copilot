package com.aicopilot.ai.model;

import java.util.ArrayList;
import java.util.List;

public class PracticeEvaluationResponse {
    private int scoreAccuracy;
    private int scoreCompleteness;
    private int scoreCommunication;
    private int overallScore;
    private String feedback;
    private String idealAnswerSample;
    private List<String> suggestions = new ArrayList<>();
    private String followUpQuestion;

    public PracticeEvaluationResponse() {}

    public PracticeEvaluationResponse(int scoreAccuracy, int scoreCompleteness, int scoreCommunication, String feedback, String idealAnswerSample) {
        this.scoreAccuracy = scoreAccuracy;
        this.scoreCompleteness = scoreCompleteness;
        this.scoreCommunication = scoreCommunication;
        this.overallScore = (scoreAccuracy + scoreCompleteness + scoreCommunication) / 3;
        this.feedback = feedback;
        this.idealAnswerSample = idealAnswerSample;
    }

    public int getScoreAccuracy() { return scoreAccuracy; }
    public void setScoreAccuracy(int scoreAccuracy) { this.scoreAccuracy = scoreAccuracy; }

    public int getScoreCompleteness() { return scoreCompleteness; }
    public void setScoreCompleteness(int scoreCompleteness) { this.scoreCompleteness = scoreCompleteness; }

    public int getScoreCommunication() { return scoreCommunication; }
    public void setScoreCommunication(int scoreCommunication) { this.scoreCommunication = scoreCommunication; }

    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public String getIdealAnswerSample() { return idealAnswerSample; }
    public void setIdealAnswerSample(String idealAnswerSample) { this.idealAnswerSample = idealAnswerSample; }

    public List<String> getSuggestions() { return suggestions; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }

    public String getFollowUpQuestion() { return followUpQuestion; }
    public void setFollowUpQuestion(String followUpQuestion) { this.followUpQuestion = followUpQuestion; }
}
