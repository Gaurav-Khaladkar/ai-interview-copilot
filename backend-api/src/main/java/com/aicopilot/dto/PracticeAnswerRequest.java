package com.aicopilot.dto;

import jakarta.validation.constraints.NotBlank;

public class PracticeAnswerRequest {

    @NotBlank(message = "Question ID is required")
    private String questionId;

    @NotBlank(message = "User answer cannot be blank")
    private String userAnswer;

    private String inputMode = "TEXT"; // "TEXT" or "VOICE"
    private String transcriptionText;

    public PracticeAnswerRequest() {}

    public String getQuestionId() { return questionId; }
    public void setQuestionId(String questionId) { this.questionId = questionId; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }

    public String getInputMode() { return inputMode; }
    public void setInputMode(String inputMode) { this.inputMode = inputMode; }

    public String getTranscriptionText() { return transcriptionText; }
    public void setTranscriptionText(String transcriptionText) { this.transcriptionText = transcriptionText; }
}
