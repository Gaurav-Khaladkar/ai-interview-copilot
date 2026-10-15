package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

public class DashboardView {

    public static Node createView(Runnable onStartPractice, Runnable onUploadResume, Runnable onOpenCoding, Runnable onOpenAssistant) {
        VBox container = new VBox(24);
        container.setPadding(new Insets(10));

        // Welcome Header
        VBox header = new VBox(4);
        Label title = new Label("Candidate Workspace Dashboard");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Track your mock interview progress, resume strength, and technical competencies.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Metric KPI Cards Row
        HBox kpiRow = new HBox(16);
        kpiRow.setAlignment(Pos.CENTER_LEFT);

        kpiRow.getChildren().addAll(
            createKpiCard("Readiness Score", "88%", "+5% this week", "#38bdf8"),
            createKpiCard("Sessions Completed", "14", "Last taken today", "#34d399"),
            createKpiCard("Verified Skills", "26", "From parsed resume", "#a78bfa"),
            createKpiCard("Avg Communication", "91%", "Strong verbal clarity", "#f59e0b")
        );

        // Quick Actions Row
        VBox quickActionsCard = new VBox(12);
        quickActionsCard.getStyleClass().add("card");
        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.getStyleClass().add("card-title");

        HBox actionButtons = new HBox(12);
        Button btn1 = new Button("Start Mock Interview");
        btn1.getStyleClass().add("btn-primary");
        btn1.setOnAction(e -> onStartPractice.run());

        Button btn2 = new Button("Analyze Resume");
        btn2.getStyleClass().add("btn-secondary");
        btn2.setOnAction(e -> onUploadResume.run());

        Button btn3 = new Button("Coding Sandbox");
        btn3.getStyleClass().add("btn-secondary");
        btn3.setOnAction(e -> onOpenCoding.run());

        Button btn4 = new Button("Ask AI Copilot");
        btn4.getStyleClass().add("btn-secondary");
        btn4.setOnAction(e -> onOpenAssistant.run());

        actionButtons.getChildren().addAll(btn1, btn2, btn3, btn4);
        quickActionsCard.getChildren().addAll(actionsTitle, actionButtons);

        // Recent Sessions Card
        VBox sessionsCard = new VBox(12);
        sessionsCard.getStyleClass().add("card");
        Label sessionsTitle = new Label("Recent Practice Sessions");
        sessionsTitle.getStyleClass().add("card-title");

        VBox sessionList = new VBox(8);
        sessionList.getChildren().addAll(
            createSessionRow("Senior Java & Distributed Systems", "COMPLETED", "Score: 92/100", "25 mins ago"),
            createSessionRow("Spring Cloud & Microservices", "COMPLETED", "Score: 86/100", "Yesterday"),
            createSessionRow("SQL Indexing & ACID Transactions", "COMPLETED", "Score: 89/100", "3 days ago")
        );
        sessionsCard.getChildren().addAll(sessionsTitle, sessionList);

        container.getChildren().addAll(header, kpiRow, quickActionsCard, sessionsCard);
        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private static Node createKpiCard(String label, String value, String subtext, String color) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        card.setPrefWidth(240);

        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        Label val = new Label(value);
        val.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label sub = new Label(subtext);
        sub.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");

        card.getChildren().addAll(lbl, val, sub);
        return card;
    }

    private static Node createSessionRow(String title, String status, String score, String time) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #0f172a; -fx-padding: 10px 14px; -fx-background-radius: 8px; -fx-border-color: #334155; -fx-border-radius: 8px;");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        HBox.setHgrow(titleLbl, Priority.ALWAYS);

        Label scoreLbl = new Label(score);
        scoreLbl.setStyle("-fx-text-fill: #38bdf8; -fx-font-weight: bold;");

        Label statusLbl = new Label(status);
        statusLbl.getStyleClass().add("badge-success");

        Label timeLbl = new Label(time);
        timeLbl.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");

        row.getChildren().addAll(titleLbl, scoreLbl, statusLbl, timeLbl);
        return row;
    }
}
