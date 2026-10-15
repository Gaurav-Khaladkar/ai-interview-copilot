package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ModelManagerView {

    public static Node createView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Offline AI Model & Speech Engine Management");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Inspect installed local LLMs, check hardware resource requirements (RAM, VRAM, CPU), and configure offline inference.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Hardware Compatibility Diagnostics Card
        VBox hwCard = new VBox(12);
        hwCard.getStyleClass().add("card");
        Label hwTitle = new Label("Detected Windows Hardware Environment");
        hwTitle.getStyleClass().add("card-title");

        long maxMemoryMb = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        int cpuCores = Runtime.getRuntime().availableProcessors();

        HBox hwGrid = new HBox(20);
        hwGrid.getChildren().addAll(
            createHwStat("Available CPU Cores", cpuCores + " Threads", "#38bdf8"),
            createHwStat("JVM Heap Allocation", maxMemoryMb + " MB", "#34d399"),
            createHwStat("OS Architecture", System.getProperty("os.arch") + " (" + System.getProperty("os.name") + ")", "#a78bfa")
        );
        hwCard.getChildren().addAll(hwTitle, hwGrid);

        // Model Catalog & Status Table
        VBox catalogCard = new VBox(12);
        catalogCard.getStyleClass().add("card");
        Label catTitle = new Label("Supported Offline Model Catalog");
        catTitle.getStyleClass().add("card-title");

        VBox modelList = new VBox(8);
        modelList.getChildren().addAll(
            createModelRow("llama3:8b (Q4_K_M)", "Local LLM", "4.9 GB", "Min 8GB RAM, 6GB VRAM", "INSTALLED", "#10b981"),
            createModelRow("phi3:mini (3.8B)", "Compact Local LLM", "2.3 GB", "Min 4GB RAM, CPU-friendly", "READY", "#38bdf8"),
            createModelRow("mistral:7b-instruct", "General Reasoning", "4.1 GB", "Min 8GB RAM, 6GB VRAM", "READY", "#38bdf8"),
            createModelRow("whisper-base", "Offline Speech-to-Text", "142 MB", "Min 1GB RAM, Fast CPU", "ACTIVE", "#10b981"),
            createModelRow("offline-knowledge-base", "Built-in Technical Engine", "Embedded", "Zero extra RAM", "ACTIVE", "#10b981")
        );
        catalogCard.getChildren().addAll(catTitle, modelList);

        // Setup Guide Card
        VBox guideCard = new VBox(10);
        guideCard.getStyleClass().add("card");
        Label guideTitle = new Label("How to Download & Run Models for 100% Offline Use");
        guideTitle.getStyleClass().add("card-title");

        VBox steps = new VBox(6);
        steps.getChildren().addAll(
            new Label("Step 1: Install Ollama on Windows from https://ollama.com"),
            new Label("Step 2: Open PowerShell and run: ollama pull llama3:8b"),
            new Label("Step 3: Run: ollama run llama3:8b to verify it is responsive on http://localhost:11434"),
            new Label("Step 4: AI Interview Copilot will automatically discover the local model endpoint without internet access.")
        );
        for (Node n : steps.getChildren()) {
            ((Label) n).setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 12px;");
        }
        guideCard.getChildren().addAll(guideTitle, steps);

        container.getChildren().addAll(header, hwCard, catalogCard, guideCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }

    private static Node createHwStat(String title, String val, String color) {
        VBox b = new VBox(4);
        b.setStyle("-fx-background-color: #0f172a; -fx-padding: 10px 16px; -fx-background-radius: 8px; -fx-border-color: #334155; -fx-border-radius: 8px;");
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        Label v = new Label(val);
        v.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        b.getChildren().addAll(t, v);
        return b;
    }

    private static Node createModelRow(String name, String type, String size, String reqs, String status, String statusColor) {
        HBox row = new HBox(16);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: #0f172a; -fx-padding: 10px 14px; -fx-background-radius: 8px; -fx-border-color: #334155; -fx-border-radius: 8px;");

        VBox info = new VBox(2);
        Label nameLbl = new Label(name);
        nameLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label reqLbl = new Label(type + " • Size: " + size + " • " + reqs);
        reqLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        info.getChildren().addAll(nameLbl, reqLbl);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label statusLbl = new Label(status);
        statusLbl.setStyle("-fx-background-color: #1e3a8a; -fx-text-fill: " + statusColor + "; -fx-font-weight: bold; -fx-padding: 4px 10px; -fx-background-radius: 12px; -fx-font-size: 11px;");

        row.getChildren().addAll(info, statusLbl);
        return row;
    }
}
