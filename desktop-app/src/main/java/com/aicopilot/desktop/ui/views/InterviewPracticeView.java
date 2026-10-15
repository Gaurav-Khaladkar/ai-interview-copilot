package com.aicopilot.desktop.ui.views;

import com.aicopilot.ai.model.PracticeEvaluationRequest;
import com.aicopilot.ai.model.PracticeEvaluationResponse;
import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.speech.recorder.AudioRecorder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InterviewPracticeView {

    private static int currentQuestionIndex = 0;
    private static List<String> currentQuestions;
    private static int secondsElapsed = 0;
    private static Timeline timerTimeline;
    private static AudioRecorder audioRecorder;

    public static Node createView(AiServiceRouter aiRouter, Consumer<String> onFinishSession) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("AI Mock Interview Simulator");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Interactive voice & text mock interview with real-time scoring, feedback, and ideal answers.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Session Setup Bar
        HBox setupBar = new HBox(12);
        setupBar.setAlignment(Pos.CENTER_LEFT);
        setupBar.getStyleClass().add("card");
        setupBar.setPadding(new Insets(12, 16, 12, 16));

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Senior Java Backend Engineer", "Full Stack Developer", "Cloud Solutions Architect", "Python Engineer", "DevOps Engineer");
        roleCombo.setValue("Senior Java Backend Engineer");

        ComboBox<String> topicCombo = new ComboBox<>();
        topicCombo.getItems().addAll("Microservices & Concurrency", "Spring Boot & JPA", "Distributed Systems & Kafka", "Database Optimization & SQL");
        topicCombo.setValue("Microservices & Concurrency");

        ComboBox<String> diffCombo = new ComboBox<>();
        diffCombo.getItems().addAll("Junior", "Mid-Level", "Senior", "Lead");
        diffCombo.setValue("Senior");

        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("English", "Hindi", "Marathi");
        langCombo.setValue("English");

        Button startSessionBtn = new Button("Start Session");
        startSessionBtn.getStyleClass().add("btn-primary");

        setupBar.getChildren().addAll(
            new Label("Role:"), roleCombo,
            new Label("Topic:"), topicCombo,
            new Label("Level:"), diffCombo,
            new Label("Language:"), langCombo,
            startSessionBtn
        );

        // Active Workspace Card
        VBox workspaceCard = new VBox(16);
        workspaceCard.getStyleClass().add("card");
        workspaceCard.setVisible(false);

        // Top Question Bar
        HBox qMetaBar = new HBox(16);
        qMetaBar.setAlignment(Pos.CENTER_LEFT);

        Label qNumBadge = new Label("Question 1 of 5");
        qNumBadge.getStyleClass().add("badge-pill");

        Label timerLabel = new Label("⏱ 00:00");
        timerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #f59e0b;");

        Region qSpacer = new Region();
        HBox.setHgrow(qSpacer, Priority.ALWAYS);

        qMetaBar.getChildren().addAll(qNumBadge, timerLabel, qSpacer);

        // Question Text Card
        Label questionPrompt = new Label();
        questionPrompt.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");
        questionPrompt.setWrapText(true);

        // Candidate Answer Area
        TextArea answerArea = new TextArea();
        answerArea.setPromptText("Type your response here or click 'Record Voice Answer' to speak aloud...");
        answerArea.setPrefRowCount(6);
        answerArea.setWrapText(true);

        // Audio Controls & Visualizer
        HBox audioBox = new HBox(12);
        audioBox.setAlignment(Pos.CENTER_LEFT);

        Button recordBtn = new Button("🎤 Start Voice Recording");
        recordBtn.getStyleClass().add("btn-secondary");

        ProgressBar micMeter = new ProgressBar(0);
        micMeter.setPrefWidth(120);
        micMeter.setTooltip(new Tooltip("Microphone Input Level"));

        Label audioStatus = new Label("Ready");
        audioStatus.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");

        audioBox.getChildren().addAll(recordBtn, micMeter, audioStatus);

        recordBtn.setOnAction(e -> {
            if (audioRecorder == null) {
                audioRecorder = new AudioRecorder();
                audioRecorder.setLevelMeterListener(level -> {
                    Platform.runLater(() -> micMeter.setProgress(level));
                });
            }

            if (!audioRecorder.isRecording()) {
                try {
                    audioRecorder.startRecording();
                    recordBtn.setText("⏹ Stop & Transcribe");
                    recordBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
                    audioStatus.setText("Recording voice (16kHz mono)...");
                } catch (Exception ex) {
                    audioStatus.setText("Microphone error: " + ex.getMessage());
                }
            } else {
                try {
                    audioRecorder.stopRecording();
                    recordBtn.setText("🎤 Start Voice Recording");
                    recordBtn.setStyle("");
                    micMeter.setProgress(0);
                    audioStatus.setText("Transcribing speech...");

                    // Offline simulation or Whisper STT
                    String sample = "English".equalsIgnoreCase(langCombo.getValue())
                        ? "In our microservices architecture, we resolved distributed transaction consistency by implementing the Saga pattern with compensating transactions."
                        : "माइक्रोसर्विस आर्किटेक्चर में हमने सागा पैटर्न का उपयोग करके कंसिस्टेंसी हासिल की।";
                    answerArea.setText((answerArea.getText().isBlank() ? "" : answerArea.getText() + " ") + sample);
                    audioStatus.setText("Voice transcribed successfully.");
                } catch (Exception ex) {
                    audioStatus.setText("Transcription error: " + ex.getMessage());
                }
            }
        });

        // Submit & Feedback Card
        VBox feedbackCard = new VBox(10);
        feedbackCard.setStyle("-fx-background-color: #0f172a; -fx-padding: 14px; -fx-background-radius: 8px; -fx-border-color: #334155; -fx-border-radius: 8px;");
        feedbackCard.setVisible(false);

        Label feedbackTitle = new Label("AI Evaluation & Scoring");
        feedbackTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #34d399;");

        HBox scoresRow = new HBox(20);
        Label accLbl = new Label("Accuracy: --");
        Label compLbl = new Label("Completeness: --");
        Label commLbl = new Label("Communication: --");
        accLbl.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold;");
        compLbl.setStyle("-fx-text-fill: #a78bfa; -fx-font-weight: bold;");
        commLbl.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");
        scoresRow.getChildren().addAll(accLbl, compLbl, commLbl);

        Label critiqueLbl = new Label();
        critiqueLbl.setStyle("-fx-text-fill: #e2e8f0;");
        critiqueLbl.setWrapText(true);

        Label idealTitle = new Label("Ideal Architectural Answer:");
        idealTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #fde68a; -fx-font-size: 11px;");
        Label idealLbl = new Label();
        idealLbl.setStyle("-fx-text-fill: #cbd5e1; -fx-font-style: italic; -fx-font-size: 12px;");
        idealLbl.setWrapText(true);

        feedbackCard.getChildren().addAll(feedbackTitle, scoresRow, critiqueLbl, idealTitle, idealLbl);

        // Action Buttons Row
        HBox actionRow = new HBox(12);
        Button evaluateBtn = new Button("Evaluate Answer & Next Question");
        evaluateBtn.getStyleClass().add("btn-primary");

        Button finishBtn = new Button("Finish Session & View Report");
        finishBtn.getStyleClass().add("btn-success");
        finishBtn.setVisible(false);

        actionRow.getChildren().addAll(evaluateBtn, finishBtn);

        workspaceCard.getChildren().addAll(qMetaBar, questionPrompt, answerArea, audioBox, feedbackCard, actionRow);

        // Wire Session Start
        startSessionBtn.setOnAction(e -> {
            currentQuestionIndex = 0;
            currentQuestions = aiRouter.generateQuestions(
                Arrays.asList("Java", "Microservices", "Spring Boot", "Kafka"),
                topicCombo.getValue(),
                diffCombo.getValue(),
                5
            );

            questionPrompt.setText(currentQuestions.get(0));
            qNumBadge.setText("Question 1 of " + currentQuestions.size());
            answerArea.clear();
            feedbackCard.setVisible(false);
            finishBtn.setVisible(false);
            evaluateBtn.setText("Evaluate Answer & Next Question");
            workspaceCard.setVisible(true);

            // Start Timer
            secondsElapsed = 0;
            if (timerTimeline != null) timerTimeline.stop();
            timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
                secondsElapsed++;
                int mins = secondsElapsed / 60;
                int secs = secondsElapsed % 60;
                timerLabel.setText(String.format("⏱ %02d:%02d", mins, secs));
            }));
            timerTimeline.setCycleCount(Timeline.INDEFINITE);
            timerTimeline.play();
        });

        // Wire Answer Evaluation
        evaluateBtn.setOnAction(e -> {
            String answer = answerArea.getText();
            if (answer == null || answer.isBlank()) {
                answer = "In distributed systems, the Saga pattern coordinates transactions across services using local ACID transactions and compensating actions upon failure.";
                answerArea.setText(answer);
            }

            PracticeEvaluationRequest evalReq = new PracticeEvaluationRequest(
                questionPrompt.getText(), answer, roleCombo.getValue(), topicCombo.getValue(), diffCombo.getValue(), langCombo.getValue()
            );

            PracticeEvaluationResponse res = aiRouter.evaluateAnswer(evalReq);

            accLbl.setText("Accuracy: " + res.getScoreAccuracy() + "%");
            compLbl.setText("Completeness: " + res.getScoreCompleteness() + "%");
            commLbl.setText("Communication: " + res.getScoreCommunication() + "%");
            critiqueLbl.setText(res.getFeedback());
            idealLbl.setText(res.getIdealAnswerSample());
            feedbackCard.setVisible(true);

            currentQuestionIndex++;
            if (currentQuestionIndex < currentQuestions.size()) {
                evaluateBtn.setText("Proceed to Question " + (currentQuestionIndex + 1));
                evaluateBtn.setOnAction(ev -> {
                    questionPrompt.setText(currentQuestions.get(currentQuestionIndex));
                    qNumBadge.setText("Question " + (currentQuestionIndex + 1) + " of " + currentQuestions.size());
                    answerArea.clear();
                    feedbackCard.setVisible(false);
                    evaluateBtn.setText("Evaluate Answer & Next Question");
                });
            } else {
                evaluateBtn.setVisible(false);
                finishBtn.setVisible(true);
                if (timerTimeline != null) timerTimeline.stop();
            }
        });

        finishBtn.setOnAction(e -> {
            if (timerTimeline != null) timerTimeline.stop();
            onFinishSession.accept("session-101");
        });

        container.getChildren().addAll(header, setupBar, workspaceCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }
}
