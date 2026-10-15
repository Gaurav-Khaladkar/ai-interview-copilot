package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class WelcomeView {

    public static Node createView(Runnable onStartPractice, Runnable onUploadResume) {
        VBox container = new VBox(24);
        container.setAlignment(Pos.TOP_LEFT);

        // Hero Card
        VBox heroCard = new VBox(14);
        heroCard.getStyleClass().add("card");
        heroCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #1e3a8a, #0f172a); -fx-padding: 32px; -fx-background-radius: 16px;");

        Label badge = new Label("ENTERPRISE AI INTERVIEW COPILOT");
        badge.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 4px 12px; -fx-background-radius: 20px; -fx-font-size: 11px;");

        Label heroTitle = new Label("Master Technical Interviews with Confidence");
        heroTitle.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        Label heroSubtitle = new Label("AI-powered mock interviews, intelligent resume parsing, voice transcription, multilingual answer breakdown, and sandboxed coding practice — designed specifically for Windows 10 & 11.");
        heroSubtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #cbd5e1; -fx-max-width: 800px;");
        heroSubtitle.setWrapText(true);

        HBox ctaBox = new HBox(12);
        ctaBox.setPadding(new Insets(10, 0, 0, 0));

        Button startBtn = new Button("Start Mock Interview");
        startBtn.getStyleClass().add("btn-primary");
        startBtn.setOnAction(e -> onStartPractice.run());

        Button resumeBtn = new Button("Upload Resume");
        resumeBtn.getStyleClass().add("btn-secondary");
        resumeBtn.setOnAction(e -> onUploadResume.run());

        ctaBox.getChildren().addAll(startBtn, resumeBtn);
        heroCard.getChildren().addAll(badge, heroTitle, heroSubtitle, ctaBox);

        // Feature Highlights Grid
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(createFeatureCard("Intelligent Resume Parser", "Upload PDF or DOCX to extract technical skills, calculate resume readiness scores, and generate interview questions.", "#38bdf8"), 0, 0);
        grid.add(createFeatureCard("Voice & Whisper Speech", "Practice speaking aloud. Uses 16kHz audio capture with Whisper STT and Windows SAPI speech synthesis.", "#34d399"), 1, 0);
        grid.add(createFeatureCard("Dual Online / Offline Mode", "Operates seamlessly without internet using local models and offline knowledge banks. Zero network leaks.", "#f59e0b"), 0, 1);
        grid.add(createFeatureCard("Sandboxed Coding Practice", "Interactive code editor supporting Java, Python, JS, and SQL with isolated execution and Big-O analysis.", "#a78bfa"), 1, 1);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);

        container.getChildren().addAll(heroCard, grid);
        return new ScrollPane(container);
    }

    private static Node createFeatureCard(String title, String desc, String accentColor) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(18));

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: " + accentColor + ";");

        Label descLbl = new Label(desc);
        descLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #94a3b8;");
        descLbl.setWrapText(true);

        card.getChildren().addAll(titleLbl, descLbl);
        return card;
    }
}
