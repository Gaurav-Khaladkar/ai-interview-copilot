package com.aicopilot.ai.model;

public class PracticeEvaluationRequest {
    private String questionText;
    private String candidateAnswer;
    private String jobRole;
    private String topic;
    private String difficulty;
    private String language;

    public PracticeEvaluationRequest() {}

    public PracticeEvaluationRequest(String questionText, String candidateAnswer, String jobRole, String topic, String difficulty, String language) {
        this.questionText = questionText;
        this.candidateAnswer = candidateAnswer;
        this.jobRole = jobRole;
        this.topic = topic;
        this.difficulty = difficulty;
        this.language = language;
    }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getCandidateAnswer() { return candidateAnswer; }
    public void setCandidateAnswer(String candidateAnswer) { this.candidateAnswer = candidateAnswer; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
