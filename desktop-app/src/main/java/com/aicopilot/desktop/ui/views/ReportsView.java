package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ReportsView {

    public static Node createView(String sessionId) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Candidate Performance Evaluation Report");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Comprehensive analysis of accuracy, architectural depth, verbal communication, and topic strengths.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Overall Scorecard Row
        HBox scoreRow = new HBox(16);
        scoreRow.getChildren().addAll(
            createScoreCard("Overall Readiness", "90%", "#34d399"),
            createScoreCard("Technical Accuracy", "92%", "#38bdf8"),
            createScoreCard("Answer Structure", "88%", "#a78bfa"),
            createScoreCard("Communication Clarity", "90%", "#f59e0b")
        );

        // Strengths & Weaknesses Split
        HBox analysisRow = new HBox(16);

        VBox strengthsCard = new VBox(10);
        strengthsCard.getStyleClass().add("card");
        HBox.setHgrow(strengthsCard, Priority.ALWAYS);
        Label strTitle = new Label("Verified Strengths & Strong Answers");
        strTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #34d399; -fx-font-size: 15px;");
        VBox strList = new VBox(6);
        strList.getChildren().addAll(
            new Label("✓ Precise explanation of HashMap internal hash distribution and Red-Black tree conversion."),
            new Label("✓ Thorough knowledge of distributed transactions, contrasting 2PC vs Saga orchestration."),
            new Label("✓ Clear articulation of database indexing strategies and query execution plan analysis."),
            new Label("✓ Confident verbal pacing with appropriate architectural trade-off reasoning.")
        );
        for (Node n : strList.getChildren()) {
            ((Label) n).setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 12px;");
            ((Label) n).setWrapText(true);
        }
        strengthsCard.getChildren().addAll(strTitle, strList);

        VBox weaknessesCard = new VBox(10);
        weaknessesCard.getStyleClass().add("card");
        HBox.setHgrow(weaknessesCard, Priority.ALWAYS);
        Label weakTitle = new Label("Areas for Growth & Potential Pitfalls");
        weakTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #f59e0b; -fx-font-size: 15px;");
        VBox weakList = new VBox(6);
        weakList.getChildren().addAll(
            new Label("! Could elaborate more on edge case recovery during Kafka consumer group rebalancing."),
            new Label("! Consider mentioning observability metrics (SLIs, SLOs, distributed tracing) when asked about resilience."),
            new Label("! Add explicit Big-O time and auxiliary space complexity statements before coding.")
        );
        for (Node n : weakList.getChildren()) {
            ((Label) n).setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 12px;");
            ((Label) n).setWrapText(true);
        }
        weaknessesCard.getChildren().addAll(weakTitle, weakList);

        analysisRow.getChildren().addAll(strengthsCard, weaknessesCard);

        // Recommended Topics & Action Plan
        VBox recCard = new VBox(10);
        recCard.getStyleClass().add("card");
        Label recTitle = new Label("Recommended Learning Roadmap for Next Round");
        recTitle.getStyleClass().add("card-title");

        VBox recList = new VBox(6);
        recList.getChildren().addAll(
            new Label("1. Review Kafka Idempotent Producers & Transactional Exactly-Once Semantics (EOS)."),
            new Label("2. Deepen knowledge of Java 21 Virtual Threads (Project Loom) carrier thread pinning."),
            new Label("3. Practice designing rate limiters using Token Bucket and Leaky Bucket algorithms.")
        );
        for (Node n : recList.getChildren()) {
            ((Label) n).setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 12px;");
            ((Label) n).setWrapText(true);
        }

        HBox exportRow = new HBox(12);
        Button exportBtn = new Button("📥 Export PDF Report");
        exportBtn.getStyleClass().add("btn-primary");
        exportBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Exported");
            alert.setHeaderText("Performance Report Ready");
            alert.setContentText("Report saved to candidate downloads as PDF.");
            alert.showAndWait();
        });
        exportRow.getChildren().add(exportBtn);

        recCard.getChildren().addAll(recTitle, recList, exportRow);

        container.getChildren().addAll(header, scoreRow, analysisRow, recCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private static Node createScoreCard(String title, String score, String color) {
        VBox card = new VBox(4);
        card.getStyleClass().add("card");
        card.setPrefWidth(240);
        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        Label scoreLbl = new Label(score);
        scoreLbl.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        card.getChildren().addAll(titleLbl, scoreLbl);
        return card;
    }
}
