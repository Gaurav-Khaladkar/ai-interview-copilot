package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class HistoryView {

    public static Node createView(Consumer<String> onSelectSession) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Candidate Interview Practice History");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Review past mock interview sessions, historical score trends, and answer transcripts.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Filter Bar
        HBox filterBar = new HBox(12);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.getStyleClass().add("card");
        filterBar.setPadding(new Insets(10, 14, 10, 14));

        TextField searchField = new TextField();
        searchField.setPromptText("Search by topic or role...");
        searchField.setPrefWidth(260);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("All Statuses", "COMPLETED", "IN_PROGRESS");
        statusFilter.setValue("All Statuses");

        filterBar.getChildren().addAll(new Label("Filter:"), searchField, statusFilter);

        // Session Cards List
        VBox listCard = new VBox(10);
        listCard.getStyleClass().add("card");

        listCard.getChildren().addAll(
            createHistoryItem("Senior Java & Distributed Systems", "5 Questions", "Score: 92/100", "COMPLETED", "Today at 02:15 AM", () -> onSelectSession.accept("sess-01")),
            createHistoryItem("Spring Cloud & Microservices Resiliency", "5 Questions", "Score: 86/100", "COMPLETED", "Yesterday at 04:30 PM", () -> onSelectSession.accept("sess-02")),
            createHistoryItem("PostgreSQL Indexing & Optimization", "5 Questions", "Score: 89/100", "COMPLETED", "3 days ago", () -> onSelectSession.accept("sess-03")),
            createHistoryItem("Python Concurrency & GIL Internals", "4 Questions", "Score: 84/100", "COMPLETED", "Last week", () -> onSelectSession.accept("sess-04"))
        );

        container.getChildren().addAll(header, filterBar, listCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private static Node createHistoryItem(String topic, String qCount, String score, String status, String date, Runnable onViewReport) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #0f172a; -fx-padding: 14px 18px; -fx-background-radius: 8px; -fx-border-color: #334155; -fx-border-radius: 8px;");

        VBox meta = new VBox(4);
        Label topicLbl = new Label(topic);
        topicLbl.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label detailsLbl = new Label(qCount + " • " + date);
        detailsLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        meta.getChildren().addAll(topicLbl, detailsLbl);
        HBox.setHgrow(meta, Priority.ALWAYS);

        Label scoreLbl = new Label(score);
        scoreLbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");

        Label statusLbl = new Label(status);
        statusLbl.getStyleClass().add("badge-success");

        Button viewBtn = new Button("View Report");
        viewBtn.getStyleClass().add("btn-secondary");
        viewBtn.setOnAction(e -> onViewReport.run());

        row.getChildren().addAll(meta, scoreLbl, statusLbl, viewBtn);
        return row;
    }
}
