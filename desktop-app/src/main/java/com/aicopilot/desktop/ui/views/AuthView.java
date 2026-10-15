package com.aicopilot.desktop.ui.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class AuthView {

    public static Node createView(Consumer<String> onLoginSuccess) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(30));

        VBox authCard = new VBox(18);
        authCard.getStyleClass().add("card");
        authCard.setMaxWidth(460);
        authCard.setAlignment(Pos.TOP_LEFT);

        Label headerTitle = new Label("Candidate Authentication");
        headerTitle.getStyleClass().add("card-title");
        headerTitle.setStyle("-fx-font-size: 20px;");

        Label headerSubtitle = new Label("Sign in to sync your interview progress or create a new profile.");
        headerSubtitle.setStyle("-fx-text-fill: #94a3b8;");

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");

        // Tab 1: Login
        Tab loginTab = new Tab("Sign In");
        loginTab.setClosable(false);
        VBox loginBox = new VBox(12);
        loginBox.setPadding(new Insets(16, 0, 0, 0));

        Label emailLbl = new Label("Email Address");
        TextField emailField = new TextField("demo@aicopilot.com");
        emailField.setPromptText("candidate@example.com");

        Label passLbl = new Label("Password");
        PasswordField passField = new PasswordField();
        passField.setText("Password123!");

        Label loginStatus = new Label();
        loginStatus.setStyle("-fx-text-fill: #34d399; -fx-font-size: 12px;");

        Button submitLoginBtn = new Button("Sign In");
        submitLoginBtn.getStyleClass().add("btn-primary");
        submitLoginBtn.setMaxWidth(Double.MAX_VALUE);
        submitLoginBtn.setOnAction(e -> {
            if (emailField.getText().isBlank() || passField.getText().isBlank()) {
                loginStatus.setStyle("-fx-text-fill: #ef4444;");
                loginStatus.setText("Please enter both email and password.");
                return;
            }
            loginStatus.setStyle("-fx-text-fill: #34d399;");
            loginStatus.setText("Authenticated successfully!");
            onLoginSuccess.accept(emailField.getText().trim());
        });

        loginBox.getChildren().addAll(emailLbl, emailField, passLbl, passField, loginStatus, submitLoginBtn);
        loginTab.setContent(loginBox);

        // Tab 2: Register
        Tab regTab = new Tab("Create Account");
        regTab.setClosable(false);
        VBox regBox = new VBox(12);
        regBox.setPadding(new Insets(16, 0, 0, 0));

        TextField regName = new TextField();
        regName.setPromptText("Full Name");
        TextField regEmail = new TextField();
        regEmail.setPromptText("Email Address");
        PasswordField regPass = new PasswordField();
        regPass.setPromptText("Password (min 8 characters)");
        TextField regDomain = new TextField("Java / Spring Boot");
        regDomain.setPromptText("Target Technical Domain");

        Label regStatus = new Label();
        regStatus.setStyle("-fx-text-fill: #34d399; -fx-font-size: 12px;");

        Button submitRegBtn = new Button("Register Account");
        submitRegBtn.getStyleClass().add("btn-success");
        submitRegBtn.setMaxWidth(Double.MAX_VALUE);
        submitRegBtn.setOnAction(e -> {
            if (regEmail.getText().isBlank() || regPass.getText().length() < 8) {
                regStatus.setStyle("-fx-text-fill: #ef4444;");
                regStatus.setText("Email is required and password must be at least 8 characters.");
                return;
            }
            regStatus.setStyle("-fx-text-fill: #34d399;");
            regStatus.setText("Account created! Logging in...");
            onLoginSuccess.accept(regEmail.getText().trim());
        });

        regBox.getChildren().addAll(
            new Label("Full Name"), regName,
            new Label("Email Address"), regEmail,
            new Label("Password"), regPass,
            new Label("Target Domain"), regDomain,
            regStatus, submitRegBtn
        );
        regTab.setContent(regBox);

        tabPane.getTabs().addAll(loginTab, regTab);
        authCard.getChildren().addAll(headerTitle, headerSubtitle, tabPane);
        container.getChildren().add(authCard);

        return container;
    }
}
