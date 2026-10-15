package com.aicopilot.dto;

public class PracticeSessionResponseDto {
    private String sessionId;
    private String jobRole;
    private String topic;
    private String difficulty;
    private String language;
    private String status;
    private int totalQuestions;
    private QuestionDto firstQuestion;

    public PracticeSessionResponseDto() {}

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getJobRole() { return jobRole; }
    public void setJobRole(String jobRole) { this.jobRole = jobRole; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public QuestionDto getFirstQuestion() { return firstQuestion; }
    public void setFirstQuestion(QuestionDto firstQuestion) { this.firstQuestion = firstQuestion; }

    public static class QuestionDto {
        private String questionId;
        private int order;
        private String text;
        private String type;

        public QuestionDto() {}

        public QuestionDto(String questionId, int order, String text, String type) {
            this.questionId = questionId;
            this.order = order;
            this.text = text;
            this.type = type;
        }

        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { this.questionId = questionId; }

        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }

        public String getText() { return text; }
        public void setText(String text) { this.text = text; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}
