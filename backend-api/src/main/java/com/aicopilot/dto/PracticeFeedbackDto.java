package com.aicopilot.dto;

import java.util.ArrayList;
import java.util.List;

public class PracticeFeedbackDto {
    private int scoreAccuracy;
    private int scoreCompleteness;
    private int scoreCommunication;
    private int overallScore;
    private String feedback;
    private String idealAnswerSample;
    private List<String> suggestions = new ArrayList<>();
    private PracticeSessionResponseDto.QuestionDto nextQuestion;
    private boolean sessionComplete;

    public PracticeFeedbackDto() {}

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

    public PracticeSessionResponseDto.QuestionDto getNextQuestion() { return nextQuestion; }
    public void setNextQuestion(PracticeSessionResponseDto.QuestionDto nextQuestion) { this.nextQuestion = nextQuestion; }

    public boolean isSessionComplete() { return sessionComplete; }
    public void setSessionComplete(boolean sessionComplete) { this.sessionComplete = sessionComplete; }
}
