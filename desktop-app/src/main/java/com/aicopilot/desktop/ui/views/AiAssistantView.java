package com.aicopilot.desktop.ui.views;

import com.aicopilot.ai.model.AiRequest;
import com.aicopilot.ai.model.AiResponse;
import com.aicopilot.ai.model.LineExplanation;
import com.aicopilot.ai.service.AiServiceRouter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AiAssistantView {

    public static Node createView(AiServiceRouter aiRouter) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("AI Technical Assistant & Concept Explainer");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Instant technical explanations, line-by-line code breakdowns, and follow-up interview questions.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Controls Card
        VBox queryCard = new VBox(12);
        queryCard.getStyleClass().add("card");

        HBox filterRow = new HBox(12);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> domainCombo = new ComboBox<>();
        domainCombo.getItems().addAll("Java", "Spring Boot", "Python", "SQL", "JavaScript", "Cloud & Microservices", "Android Development");
        domainCombo.setValue("Java");

        ComboBox<String> diffCombo = new ComboBox<>();
        diffCombo.getItems().addAll("Junior", "Mid-Level", "Senior", "Lead");
        diffCombo.setValue("Senior");

        ToggleGroup formatGroup = new ToggleGroup();
        RadioButton shortRadio = new RadioButton("Short & Crisp");
        shortRadio.setToggleGroup(formatGroup);
        RadioButton detailedRadio = new RadioButton("Detailed Architectural");
        detailedRadio.setToggleGroup(formatGroup);
        detailedRadio.setSelected(true);

        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("English", "Hindi", "Marathi");
        langCombo.setValue("English");

        filterRow.getChildren().addAll(
            new Label("Domain:"), domainCombo,
            new Label("Difficulty:"), diffCombo,
            new Label("Format:"), shortRadio, detailedRadio,
            new Label("Language:"), langCombo
        );

        TextField questionField = new TextField("Explain how HashMap works internally in Java, including collision handling in Java 8+.");
        questionField.setPromptText("Ask any technical interview question...");

        HBox submitRow = new HBox(12);
        Button askBtn = new Button("Generate Technical Answer");
        askBtn.getStyleClass().add("btn-primary");

        submitRow.getChildren().addAll(askBtn);
        queryCard.getChildren().addAll(filterRow, questionField, submitRow);

        // Results Presentation Card
        VBox resultsCard = new VBox(14);
        resultsCard.getStyleClass().add("card");
        resultsCard.setVisible(false);

        Label responseTitle = new Label("Answer & Analysis");
        responseTitle.getStyleClass().add("card-title");

        Label providerBadge = new Label();
        providerBadge.getStyleClass().add("badge-pill");

        Label answerText = new Label();
        answerText.setStyle("-fx-font-size: 14px; -fx-text-fill: #f8fafc; -fx-line-spacing: 4px;");
        answerText.setWrapText(true);

        // Concept Summary Card
        VBox summaryBox = new VBox(6);
        summaryBox.setStyle("-fx-background-color: #0f172a; -fx-padding: 10px 14px; -fx-background-radius: 8px; -fx-border-color: #334155; -fx-border-radius: 8px;");
        Label summaryTitle = new Label("Core Architectural Concepts:");
        summaryTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #38bdf8; -fx-font-size: 11px;");
        Label summaryText = new Label();
        summaryText.setStyle("-fx-text-fill: #94a3b8;");
        summaryBox.getChildren().addAll(summaryTitle, summaryText);

        // Code Snippet Card
        VBox codeBox = new VBox(6);
        Label codeTitle = new Label("Illustrative Code Implementation:");
        codeTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #34d399;");
        TextArea codeSnippetArea = new TextArea();
        codeSnippetArea.getStyleClass().add("code-editor-area");
        codeSnippetArea.setEditable(false);
        codeSnippetArea.setPrefRowCount(4);
        codeBox.getChildren().addAll(codeTitle, codeSnippetArea);

        // Line-by-Line Breakdown Box
        VBox lineByLineBox = new VBox(6);
        Label lineTitle = new Label("Line-by-Line Code Breakdown:");
        lineTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #a78bfa;");
        VBox lineItems = new VBox(4);
        lineByLineBox.getChildren().addAll(lineTitle, lineItems);

        // Follow-up Questions Box
        VBox followUpBox = new VBox(6);
        Label followTitle = new Label("Anticipated Interview Follow-Up Questions:");
        followTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #f59e0b;");
        VBox followUpItems = new VBox(4);
        followUpBox.getChildren().addAll(followTitle, followUpItems);

        resultsCard.getChildren().addAll(
            new HBox(12, responseTitle, providerBadge),
            answerText,
            summaryBox,
            codeBox,
            lineByLineBox,
            followUpBox
        );

        askBtn.setOnAction(e -> {
            String q = questionField.getText();
            if (q == null || q.isBlank()) return;

            AiRequest req = new AiRequest(
                q,
                domainCombo.getValue(),
                diffCombo.getValue(),
                detailedRadio.isSelected() ? "DETAILED" : "SHORT",
                langCombo.getValue(),
                true
            );

            AiResponse res = aiRouter.generateAnswer(req);

            providerBadge.setText(res.getModeUsed() != null ? res.getModeUsed() : "OFFLINE ENGINE");
            answerText.setText(res.getAnswer());
            summaryText.setText(res.getConceptSummary() != null ? res.getConceptSummary() : "Core Principles & Best Practices");

            if (res.getCodeSnippet() != null) {
                codeSnippetArea.setText(res.getCodeSnippet());
                codeBox.setVisible(true);
            } else {
                codeBox.setVisible(false);
            }

            lineItems.getChildren().clear();
            if (res.getLineByLineExplanation() != null && !res.getLineByLineExplanation().isEmpty()) {
                for (LineExplanation le : res.getLineByLineExplanation()) {
                    Label lineLbl = new Label("Line " + le.getLineNumber() + " [" + le.getCodeLine() + "]: " + le.getExplanation());
                    lineLbl.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 12px;");
                    lineLbl.setWrapText(true);
                    lineItems.getChildren().add(lineLbl);
                }
                lineByLineBox.setVisible(true);
            } else {
                lineByLineBox.setVisible(false);
            }

            followUpItems.getChildren().clear();
            if (res.getFollowUpQuestions() != null) {
                for (String fq : res.getFollowUpQuestions()) {
                    Label fqLbl = new Label("→ " + fq);
                    fqLbl.setStyle("-fx-text-fill: #fde68a; -fx-font-size: 12px;");
                    fqLbl.setWrapText(true);
                    followUpItems.getChildren().add(fqLbl);
                }
            }

            resultsCard.setVisible(true);
        });

        container.getChildren().addAll(header, queryCard, resultsCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }
}
