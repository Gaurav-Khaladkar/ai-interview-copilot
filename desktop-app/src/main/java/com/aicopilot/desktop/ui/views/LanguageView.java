package com.aicopilot.desktop.ui.views;

import com.aicopilot.ai.service.AiServiceRouter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LanguageView {

    public static Node createView(AiServiceRouter aiRouter) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Multilingual Support & Localization");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Practice interviews in English, Hindi (हिंदी), and Marathi (मराठी) with instant technical translation.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Language Preferences Card
        VBox prefCard = new VBox(14);
        prefCard.getStyleClass().add("card");
        Label prefTitle = new Label("Default Language Preference");
        prefTitle.getStyleClass().add("card-title");

        HBox langSelectRow = new HBox(12);
        langSelectRow.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> langPicker = new ComboBox<>();
        langPicker.getItems().addAll("English", "Hindi (हिंदी)", "Marathi (मराठी)");
        langPicker.setValue("English");

        CheckBox autoDetectCheck = new CheckBox("Enable automatic language detection from candidate speech");
        autoDetectCheck.setSelected(true);
        autoDetectCheck.setStyle("-fx-text-fill: #f8fafc;");

        langSelectRow.getChildren().addAll(new Label("Select Target Language:"), langPicker);
        prefCard.getChildren().addAll(prefTitle, langSelectRow, autoDetectCheck);

        // Live Translation Playground Card
        VBox transCard = new VBox(14);
        transCard.getStyleClass().add("card");
        Label transTitle = new Label("Real-Time Technical Translation Preview");
        transTitle.getStyleClass().add("card-title");

        Label inputLbl = new Label("Enter English Interview Question or Answer:");
        TextArea inputArea = new TextArea("Explain the difference between optimistic locking and pessimistic locking in relational databases.");
        inputArea.setPrefRowCount(3);
        inputArea.setWrapText(true);

        HBox btnRow = new HBox(12);
        Button transHindiBtn = new Button("Translate to Hindi (हिंदी)");
        transHindiBtn.getStyleClass().add("btn-primary");

        Button transMarathiBtn = new Button("Translate to Marathi (मराठी)");
        transMarathiBtn.getStyleClass().add("btn-secondary");

        btnRow.getChildren().addAll(transHindiBtn, transMarathiBtn);

        Label outputLbl = new Label("Localized Output:");
        outputLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #34d399;");
        TextArea outputArea = new TextArea();
        outputArea.setPrefRowCount(3);
        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        transHindiBtn.setOnAction(e -> {
            String res = aiRouter.translate(inputArea.getText(), "Hindi");
            outputArea.setText(res);
        });

        transMarathiBtn.setOnAction(e -> {
            String res = aiRouter.translate(inputArea.getText(), "Marathi");
            outputArea.setText(res);
        });

        transCard.getChildren().addAll(transTitle, inputLbl, inputArea, btnRow, outputLbl, outputArea);

        // Offline Model Language Capability Banner
        VBox banner = new VBox(6);
        banner.setStyle("-fx-background-color: #1e293b; -fx-padding: 14px; -fx-background-radius: 8px; -fx-border-color: #f59e0b; -fx-border-width: 1px;");
        Label bannerTitle = new Label("ℹ Offline Model Multilingual Notice");
        bannerTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #f59e0b;");
        Label bannerDesc = new Label("When running in 100% Offline Mode without local multilingual LLM weights installed, the system utilizes the pre-loaded offline technical vocabulary engine for Hindi and Marathi.");
        bannerDesc.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        bannerDesc.setWrapText(true);
        banner.getChildren().addAll(bannerTitle, bannerDesc);

        container.getChildren().addAll(header, prefCard, transCard, banner);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }
}
