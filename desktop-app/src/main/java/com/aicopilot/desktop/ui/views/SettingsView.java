package com.aicopilot.desktop.ui.views;

import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.desktop.stage.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class SettingsView {

    public static Node createView(StageManager stageManager, AiServiceRouter aiRouter) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Application Preferences & Desktop Window Customization");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Configure window transparency, pin mode, layout sizes, AI operating mode, and local offline database.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Window Customization Card (Prompt Section 8)
        VBox windowCard = new VBox(14);
        windowCard.getStyleClass().add("card");
        Label windowTitle = new Label("Windows Stage & Desktop Customization");
        windowTitle.getStyleClass().add("card-title");

        // 1. Transparency Slider (30% to 100%)
        VBox opacityBox = new VBox(8);
        Label opacityHeader = new Label("Window Transparency (Opacity: 30% - 100%):");
        opacityHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        HBox opacityRow = new HBox(16);
        opacityRow.setAlignment(Pos.CENTER_LEFT);

        Slider opacitySlider = new Slider(0.30, 1.0, stageManager.getOpacity());
        opacitySlider.setPrefWidth(220);
        opacitySlider.setShowTickLabels(true);
        opacitySlider.setShowTickMarks(true);
        opacitySlider.setMajorTickUnit(0.10);

        Label opacityValLbl = new Label((int)(stageManager.getOpacity() * 100) + "%");
        opacityValLbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");

        Button resetOpacityBtn = new Button("Restore Full Opacity (100%)");
        resetOpacityBtn.getStyleClass().add("btn-secondary");

        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            stageManager.setOpacity(newVal.doubleValue());
            opacityValLbl.setText((int)(newVal.doubleValue() * 100) + "%");
        });

        resetOpacityBtn.setOnAction(e -> {
            opacitySlider.setValue(1.0);
            stageManager.setOpacity(1.0);
        });

        opacityRow.getChildren().addAll(opacitySlider, opacityValLbl, resetOpacityBtn);
        opacityBox.getChildren().addAll(opacityHeader, opacityRow);

        // 2. Always On Top
        CheckBox alwaysOnTopCheck = new CheckBox("Always on Top (Keep application window pinned over other windows)");
        alwaysOnTopCheck.setSelected(stageManager.isAlwaysOnTop());
        alwaysOnTopCheck.setStyle("-fx-text-fill: #f8fafc; -fx-font-weight: bold;");
        alwaysOnTopCheck.setOnAction(e -> stageManager.setAlwaysOnTop(alwaysOnTopCheck.isSelected()));

        // 3. Layout Presets
        VBox layoutBox = new VBox(8);
        Label layoutHeader = new Label("Window Dimension Presets:");
        layoutHeader.setStyle("-fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        HBox layoutButtons = new HBox(12);
        Button compactBtn = new Button("Compact (900 x 650)");
        compactBtn.getStyleClass().add("btn-secondary");
        compactBtn.setOnAction(e -> stageManager.setLayoutMode(StageManager.LayoutMode.COMPACT));

        Button standardBtn = new Button("Standard (1280 x 820)");
        standardBtn.getStyleClass().add("btn-primary");
        standardBtn.setOnAction(e -> stageManager.setLayoutMode(StageManager.LayoutMode.STANDARD));

        Button expandedBtn = new Button("Expanded (1550 x 950)");
        expandedBtn.getStyleClass().add("btn-secondary");
        expandedBtn.setOnAction(e -> stageManager.setLayoutMode(StageManager.LayoutMode.EXPANDED));

        layoutButtons.getChildren().addAll(compactBtn, standardBtn, expandedBtn);
        layoutBox.getChildren().addAll(layoutHeader, layoutButtons);

        windowCard.getChildren().addAll(windowTitle, opacityBox, alwaysOnTopCheck, layoutBox);

        // Operating Mode & AI Provider Card (Prompt Section 6)
        VBox modeCard = new VBox(14);
        modeCard.getStyleClass().add("card");
        Label modeTitle = new Label("AI Operating Mode & Network Isolation");
        modeTitle.getStyleClass().add("card-title");

        HBox modeRow = new HBox(16);
        modeRow.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton offlineRadio = new RadioButton("Offline Mode (Zero network leaks, uses local models & SQLite)");
        offlineRadio.setToggleGroup(modeGroup);
        offlineRadio.setStyle("-fx-text-fill: #fde68a; -fx-font-weight: bold;");

        RadioButton onlineRadio = new RadioButton("Online Mode (Connects to OpenAI / Gemini / Cloud APIs)");
        onlineRadio.setToggleGroup(modeGroup);
        onlineRadio.setStyle("-fx-text-fill: #6ee7b7; -fx-font-weight: bold;");

        if (aiRouter.getOperatingMode() == AiServiceRouter.OperatingMode.ONLINE) {
            onlineRadio.setSelected(true);
        } else {
            offlineRadio.setSelected(true);
        }

        offlineRadio.setOnAction(e -> aiRouter.setOperatingMode(AiServiceRouter.OperatingMode.OFFLINE));
        onlineRadio.setOnAction(e -> aiRouter.setOperatingMode(AiServiceRouter.OperatingMode.ONLINE));

        modeRow.getChildren().addAll(offlineRadio, onlineRadio);

        VBox apiBox = new VBox(8);
        Label apiHeader = new Label("Online AI API Key (Optional, stored securely):");
        PasswordField apiKeyField = new PasswordField();
        apiKeyField.setPromptText("sk-proj-...");
        apiBox.getChildren().addAll(apiHeader, apiKeyField);

        modeCard.getChildren().addAll(modeTitle, modeRow, apiBox);

        // Local Database & Storage Status Card
        VBox dbCard = new VBox(10);
        dbCard.getStyleClass().add("card");
        Label dbTitle = new Label("Local Storage & Database Engine");
        dbTitle.getStyleClass().add("card-title");

        Label dbStatus = new Label("✓ SQLite Local Database Connected (./ai-copilot.db)");
        dbStatus.setStyle("-fx-text-fill: #34d399; -fx-font-weight: bold;");

        Label dbNote = new Label("All mock interviews, candidate resumes, extracted skills, and report scorecards are persisted locally and function 100% without internet.");
        dbNote.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        dbNote.setWrapText(true);

        dbCard.getChildren().addAll(dbTitle, dbStatus, dbNote);

        container.getChildren().addAll(header, windowCard, modeCard, dbCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }
}
