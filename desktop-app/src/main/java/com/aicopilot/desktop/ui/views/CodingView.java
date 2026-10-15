package com.aicopilot.desktop.ui.views;

import com.aicopilot.dto.CodeExecutionRequestDto;
import com.aicopilot.dto.CodeExecutionResponseDto;
import com.aicopilot.service.CodeSandboxService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;

public class CodingView {

    public static Node createView(CodeSandboxService sandboxService) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Sandboxed Coding Practice Workspace");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Practice coding problems in Java, Python, JavaScript, and SQL inside an isolated execution sandbox.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Problem Selector Row
        HBox problemRow = new HBox(12);
        problemRow.setAlignment(Pos.CENTER_LEFT);
        problemRow.getStyleClass().add("card");
        problemRow.setPadding(new Insets(12, 16, 12, 16));

        ComboBox<String> problemPicker = new ComboBox<>();
        List<Map<String, String>> problems = sandboxService.getSampleProblems();
        for (Map<String, String> p : problems) {
            problemPicker.getItems().add(p.get("title") + " (" + p.get("language") + ")");
        }
        problemPicker.setValue(problemPicker.getItems().get(0));

        ComboBox<String> langPicker = new ComboBox<>();
        langPicker.getItems().addAll("Java", "Python", "JavaScript", "SQL");
        langPicker.setValue("Java");

        problemRow.getChildren().addAll(
            new Label("Select Problem:"), problemPicker,
            new Label("Language:"), langPicker
        );

        // Problem Description Card
        VBox descCard = new VBox(8);
        descCard.getStyleClass().add("card");
        Label descTitle = new Label(problems.get(0).get("title"));
        descTitle.getStyleClass().add("card-title");
        Label descBody = new Label(problems.get(0).get("description"));
        descBody.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13px;");
        descBody.setWrapText(true);
        descCard.getChildren().addAll(descTitle, descBody);

        // Code Editor Card
        VBox editorCard = new VBox(10);
        editorCard.getStyleClass().add("card");

        HBox editorHeader = new HBox(12);
        editorHeader.setAlignment(Pos.CENTER_LEFT);
        Label editorLabel = new Label("Source Code Editor");
        editorLabel.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button resetBtn = new Button("Reset Code");
        resetBtn.getStyleClass().add("btn-secondary");

        Button runBtn = new Button("▶ Run Code in Isolated Sandbox");
        runBtn.getStyleClass().add("btn-primary");

        editorHeader.getChildren().addAll(editorLabel, spacer, resetBtn, runBtn);

        TextArea codeArea = new TextArea(problems.get(0).get("starterCode"));
        codeArea.getStyleClass().add("code-editor-area");
        codeArea.setPrefRowCount(12);

        editorCard.getChildren().addAll(editorHeader, codeArea);

        // Execution Results Card
        VBox resultsCard = new VBox(12);
        resultsCard.getStyleClass().add("card");
        resultsCard.setVisible(false);

        Label resultsTitle = new Label("Execution & Complexity Results");
        resultsTitle.getStyleClass().add("card-title");

        HBox metricsRow = new HBox(20);
        Label statusLbl = new Label("Status: --");
        Label testsLbl = new Label("Tests: --");
        Label timeLbl = new Label("Time: --");
        Label compLbl = new Label("Complexity: --");
        statusLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #34d399;");
        testsLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #38bdf8;");
        timeLbl.setStyle("-fx-text-fill: #f59e0b;");
        compLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #a78bfa;");
        metricsRow.getChildren().addAll(statusLbl, testsLbl, timeLbl, compLbl);

        Label outputHeader = new Label("Sandbox Standard Output / Feedback:");
        outputHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        Label outputBody = new Label();
        outputBody.setStyle("-fx-text-fill: #f8fafc; -fx-font-family: monospace;");
        outputBody.setWrapText(true);

        Label critiqueHeader = new Label("AI Code Review & Optimization Advice:");
        critiqueHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #38bdf8; -fx-font-size: 11px;");
        Label critiqueBody = new Label();
        critiqueBody.setStyle("-fx-text-fill: #e2e8f0;");
        critiqueBody.setWrapText(true);

        resultsCard.getChildren().addAll(resultsTitle, metricsRow, outputHeader, outputBody, critiqueHeader, critiqueBody);

        // Problem Picker Switch Handler
        problemPicker.setOnAction(e -> {
            int idx = problemPicker.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && idx < problems.size()) {
                Map<String, String> p = problems.get(idx);
                descTitle.setText(p.get("title"));
                descBody.setText(p.get("description"));
                codeArea.setText(p.get("starterCode"));
                langPicker.setValue(p.get("language"));
                resultsCard.setVisible(false);
            }
        });

        resetBtn.setOnAction(e -> {
            int idx = problemPicker.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && idx < problems.size()) {
                codeArea.setText(problems.get(idx).get("starterCode"));
            }
        });

        // Run Button Handler
        runBtn.setOnAction(e -> {
            CodeExecutionRequestDto req = new CodeExecutionRequestDto();
            req.setLanguage(langPicker.getValue());
            req.setSourceCode(codeArea.getText());
            req.setProblemId(descTitle.getText());

            CodeExecutionResponseDto res = sandboxService.executeAndAnalyze(req);

            statusLbl.setText("Status: " + res.getStatus());
            if ("SECURITY_VIOLATION".equals(res.getStatus())) {
                statusLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #ef4444;");
            } else {
                statusLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #34d399;");
            }
            testsLbl.setText("Tests: " + res.getTestsPassed() + "/" + res.getTotalTests() + " Passed");
            timeLbl.setText("Execution: " + res.getExecutionTimeMs() + "ms (" + res.getMemoryUsedMb() + " MB)");
            compLbl.setText("Time: " + res.getTimeComplexity() + " | Space: " + res.getSpaceComplexity());
            outputBody.setText(res.getOutput());
            critiqueBody.setText(res.getCritique());

            resultsCard.setVisible(true);
        });

        container.getChildren().addAll(header, problemRow, descCard, editorCard, resultsCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }
}
