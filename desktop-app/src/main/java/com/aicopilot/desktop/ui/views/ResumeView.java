package com.aicopilot.desktop.ui.views;

import com.aicopilot.resume.model.ExtractedResumeData;
import com.aicopilot.resume.model.ExtractedSkill;
import com.aicopilot.resume.service.ResumeIntelligenceService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;

public class ResumeView {

    private static final ResumeIntelligenceService resumeService = new ResumeIntelligenceService();

    public static Node createView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Resume Intelligence & Skill Parser");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Upload your PDF or DOCX resume to extract technical competencies and generate targeted interview questions.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Upload Zone Card
        VBox uploadCard = new VBox(14);
        uploadCard.getStyleClass().add("card");
        uploadCard.setAlignment(Pos.CENTER);
        uploadCard.setStyle("-fx-padding: 30px; -fx-border-style: dashed; -fx-border-color: #3b82f6; -fx-border-width: 2px;");

        Label uploadIcon = new Label("📄");
        uploadIcon.setStyle("-fx-font-size: 36px;");

        Label uploadPrompt = new Label("Click below to select your PDF or Word (.docx) resume");
        uploadPrompt.setStyle("-fx-font-size: 14px; -fx-text-fill: #f8fafc; -fx-font-weight: bold;");

        Label limitNote = new Label("Supported Formats: PDF, DOCX (Max 10 MB)");
        limitNote.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");

        Button selectFileBtn = new Button("Choose Resume File");
        selectFileBtn.getStyleClass().add("btn-primary");

        uploadCard.getChildren().addAll(uploadIcon, uploadPrompt, limitNote, selectFileBtn);

        // Results Container (Dynamically populated)
        VBox resultsBox = new VBox(16);
        resultsBox.setVisible(false);

        selectFileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select Resume (PDF or DOCX)");
            chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Resume Files (*.pdf, *.docx)", "*.pdf", "*.docx"),
                new FileChooser.ExtensionFilter("PDF Documents (*.pdf)", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Documents (*.docx)", "*.docx")
            );

            File selectedFile = chooser.showOpenDialog(selectFileBtn.getScene().getWindow());
            if (selectedFile != null) {
                try {
                    byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                    ExtractedResumeData data = resumeService.parseAndAnalyze(selectedFile.getName(), bytes);
                    renderResults(resultsBox, data);
                    resultsBox.setVisible(true);
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Parsing Error");
                    alert.setHeaderText("Failed to parse resume");
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        // Pre-populate with sample parsed resume demo if requested
        Button loadDemoBtn = new Button("Load Sample Senior Java Resume");
        loadDemoBtn.getStyleClass().add("btn-secondary");
        loadDemoBtn.setOnAction(e -> {
            String sampleResumeText =
                "Jane Doe\n" +
                "jane.doe@example.com | +1 (555) 234-5678\n" +
                "Summary: 4 years of experience as Senior Java Backend Engineer developing microservices.\n" +
                "Core Skills: Java, Spring Boot, PostgreSQL, Docker, Kubernetes, Apache Kafka, Redis, AWS, REST API, Git, JUnit 5, Microservices.\n" +
                "Experience: Optimized distributed payment microservice latency by 42% using Redis caching and Kafka streaming.\n" +
                "Delivered zero-downtime Kubernetes deployments across multi-region AWS clusters.\n" +
                "Education: Bachelor of Technology in Computer Science.\n" +
                "Certifications: AWS Certified Solutions Architect, Oracle Certified Java Professional.";

            ExtractedResumeData data = resumeService.analyzeText("Jane_Doe_Resume.pdf", "application/pdf", 1024 * 180, sampleResumeText);
            renderResults(resultsBox, data);
            resultsBox.setVisible(true);
        });

        HBox topActions = new HBox(12, loadDemoBtn);
        container.getChildren().addAll(header, uploadCard, topActions, resultsBox);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private static void renderResults(VBox box, ExtractedResumeData data) {
        box.getChildren().clear();

        // 1. Overview Card with Quality Score
        VBox scoreCard = new VBox(10);
        scoreCard.getStyleClass().add("card");

        HBox topRow = new HBox(16);
        topRow.setAlignment(Pos.CENTER_LEFT);

        VBox meta = new VBox(4);
        Label candidateLbl = new Label("Candidate: " + data.getCandidateName());
        candidateLbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label contactLbl = new Label("Email: " + data.getCandidateEmail() + " | Phone: " + data.getCandidatePhone());
        contactLbl.setStyle("-fx-text-fill: #94a3b8;");
        meta.getChildren().addAll(candidateLbl, contactLbl);
        HBox.setHgrow(meta, Priority.ALWAYS);

        VBox scoreBox = new VBox(2);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreNum = new Label(data.getQualityScore() + "/100");
        scoreNum.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #34d399;");
        Label scoreTitle = new Label("Quality Score");
        scoreTitle.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        scoreBox.getChildren().addAll(scoreNum, scoreTitle);

        topRow.getChildren().addAll(meta, scoreBox);
        scoreCard.getChildren().add(topRow);

        // 2. Extracted Skills Chips
        VBox skillsCard = new VBox(10);
        skillsCard.getStyleClass().add("card");
        Label skillsTitle = new Label("Verified Technical Skills (" + data.getSkills().size() + ")");
        skillsTitle.getStyleClass().add("card-title");

        FlowPane chips = new FlowPane();
        chips.setHgap(8);
        chips.setVgap(8);

        for (ExtractedSkill s : data.getSkills()) {
            Label chip = new Label(s.getName() + " (" + s.getProficiency() + ")");
            chip.getStyleClass().add("badge-pill");
            chips.getChildren().add(chip);
        }
        skillsCard.getChildren().addAll(skillsTitle, chips);

        // 3. Actionable Suggestions
        VBox suggestionsCard = new VBox(10);
        suggestionsCard.getStyleClass().add("card");
        Label sugTitle = new Label("Resume Improvement Recommendations");
        sugTitle.getStyleClass().add("card-title");

        VBox sugList = new VBox(6);
        for (String sug : data.getSuggestions()) {
            Label item = new Label("• " + sug);
            item.setStyle("-fx-text-fill: #fde68a; -fx-font-size: 12px;");
            item.setWrapText(true);
            sugList.getChildren().add(item);
        }
        suggestionsCard.getChildren().addAll(sugTitle, sugList);

        // 4. Tailored Interview Questions
        VBox questionsCard = new VBox(10);
        questionsCard.getStyleClass().add("card");
        Label qTitle = new Label("Tailored Interview Questions for this Resume");
        qTitle.getStyleClass().add("card-title");

        VBox qList = new VBox(8);
        int idx = 1;
        for (String q : data.getGeneratedQuestions()) {
            VBox qItem = new VBox(4);
            qItem.setStyle("-fx-background-color: #0f172a; -fx-padding: 10px; -fx-background-radius: 8px;");
            Label qText = new Label("Q" + (idx++) + ": " + q);
            qText.setStyle("-fx-font-weight: bold; -fx-text-fill: #38bdf8;");
            qText.setWrapText(true);
            qItem.getChildren().add(qText);
            qList.getChildren().add(qItem);
        }
        questionsCard.getChildren().addAll(qTitle, qList);

        box.getChildren().addAll(scoreCard, skillsCard, suggestionsCard, questionsCard);
    }
}
