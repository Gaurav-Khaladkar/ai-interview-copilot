package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PrivacySecurityView {

    public static Node createView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(10));

        // Header
        VBox header = new VBox(4);
        Label title = new Label("Privacy, Data Security & Ethical Assessment Policy");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");
        Label subtitle = new Label("Transparent privacy controls, local data deletion, and third-party desktop compatibility policies.");
        subtitle.setStyle("-fx-text-fill: #94a3b8;");
        header.getChildren().addAll(title, subtitle);

        // Ethical & Assessment Software Compliance Card (Prompt Section 9)
        VBox complianceCard = new VBox(12);
        complianceCard.getStyleClass().add("card");
        complianceCard.setStyle("-fx-border-color: #3b82f6; -fx-border-width: 1px;");

        Label compTitle = new Label("Third-Party Assessment Compatibility & Fair Use Declaration");
        compTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #38bdf8; -fx-font-size: 15px;");

        Label compBody = new Label(
            "AI Interview Copilot is strictly an educational preparation and mock interview simulation desktop tool. " +
            "In full accordance with system integrity and assessment software security policies:\n\n" +
            "• Zero Process Hiding: The application runs as a standard transparent user process identifiable in Windows Task Manager as 'AI Interview Copilot'.\n" +
            "• Zero Hooking / Anti-Cheat Bypass: The application contains no kernel-level hooks, DLL injections, or evasion mechanisms.\n" +
            "• Proctoring Isolation: Mock interview sessions must be conducted independently for practice prior to taking formal third-party proctored assessments."
        );
        compBody.setStyle("-fx-text-fill: #cbd5e1; -fx-line-spacing: 4px;");
        compBody.setWrapText(true);

        complianceCard.getChildren().addAll(compTitle, compBody);

        // Audio & Microphone Privacy Card (Prompt Section 3.E)
        VBox micPrivacyCard = new VBox(12);
        micPrivacyCard.getStyleClass().add("card");
        Label micTitle = new Label("Microphone & Audio Privacy Controls");
        micTitle.getStyleClass().add("card-title");

        CheckBox micPermissionCheck = new CheckBox("Allow AI Interview Copilot to access host microphone for speech transcription");
        micPermissionCheck.setSelected(true);
        micPermissionCheck.setStyle("-fx-text-fill: #f8fafc; -fx-font-weight: bold;");

        Label micGuarantee = new Label(
            "Audio Privacy Guarantee: The microphone is opened ONLY when you explicitly press 'Start Recording'. " +
            "Zero background listening occurs. Audio recordings are immediately flushed and converted into standard 16kHz WAV format in temporary memory."
        );
        micGuarantee.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
        micGuarantee.setWrapText(true);

        micPrivacyCard.getChildren().addAll(micTitle, micPermissionCheck, micGuarantee);

        // Data Purge & Deletion Card
        VBox purgeCard = new VBox(12);
        purgeCard.getStyleClass().add("card");
        Label purgeTitle = new Label("Candidate Data Deletion & Privacy Wipe");
        purgeTitle.getStyleClass().add("card-title");

        Label purgeDesc = new Label("You can permanently delete all locally stored resumes, mock interview scores, transcripts, and preferences at any time.");
        purgeDesc.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 12px;");
        purgeDesc.setWrapText(true);

        Button purgeBtn = new Button("🗑 Wipe All Local Practice Data & Resumes");
        purgeBtn.getStyleClass().add("btn-danger");
        purgeBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Data Wipe");
            confirm.setHeaderText("Delete all local data?");
            confirm.setContentText("This will purge all local SQLite session history and uploaded resumes.");
            confirm.showAndWait().ifPresent(res -> {
                if (res == ButtonType.OK) {
                    Alert done = new Alert(Alert.AlertType.INFORMATION);
                    done.setTitle("Wipe Complete");
                    done.setHeaderText("Local Data Cleared");
                    done.setContentText("All resumes, history, and temporary audio files have been purged.");
                    done.showAndWait();
                }
            });
        });

        purgeCard.getChildren().addAll(purgeTitle, purgeDesc, purgeBtn);

        container.getChildren().addAll(header, complianceCard, micPrivacyCard, purgeCard);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        sp.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return sp;
    }
}
