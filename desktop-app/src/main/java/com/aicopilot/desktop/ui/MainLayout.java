package com.aicopilot.desktop.ui;

import com.aicopilot.desktop.stage.StageManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MainLayout {

    private final StageManager stageManager;
    private final BorderPane rootPane = new BorderPane();
    private final StackPane contentArea = new StackPane();
    private final VBox sidebar = new VBox(6);
    private Label modeBadge;
    private boolean isDarkMode = true;
    private Runnable onThemeToggle;

    public MainLayout(StageManager stageManager) {
        this.stageManager = stageManager;
        initLayout();
    }

    public BorderPane getRoot() {
        return rootPane;
    }

    public StackPane getContentArea() {
        return contentArea;
    }

    private void initLayout() {
        rootPane.setTop(buildTopBar());
        rootPane.setLeft(buildSidebar());
        rootPane.setCenter(contentArea);
        contentArea.setPadding(new Insets(24));
    }

    private Node buildTopBar() {
        HBox titlebar = new HBox(12);
        titlebar.getStyleClass().add("window-titlebar");
        titlebar.setAlignment(Pos.CENTER_LEFT);

        Label brand = new Label("AI Interview Copilot");
        brand.getStyleClass().add("titlebar-brand");

        Label versionBadge = new Label("v1.0 Windows");
        versionBadge.getStyleClass().add("badge-pill");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Online / Offline Status Badge & Toggle
        modeBadge = new Label("OFFLINE MODE");
        modeBadge.getStyleClass().add("badge-mode-offline");
        modeBadge.setTooltip(new Tooltip("Click to switch between Online and Offline operation"));
        modeBadge.setCursor(javafx.scene.Cursor.HAND);
        modeBadge.setOnMouseClicked(e -> toggleOperatingMode());

        // Transparency Slider (30% to 100%)
        Label opacityLabel = new Label("Opacity:");
        opacityLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 11px;");
        Slider opacitySlider = new Slider(0.30, 1.0, stageManager.getOpacity());
        opacitySlider.setPrefWidth(90);
        opacitySlider.setTooltip(new Tooltip("Adjust window transparency (30% - 100%)"));
        opacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            stageManager.setOpacity(newVal.doubleValue());
        });

        // Always On Top Button
        ToggleButton pinBtn = new ToggleButton("Pin Top");
        pinBtn.setSelected(stageManager.isAlwaysOnTop());
        pinBtn.getStyleClass().add("window-control-btn");
        pinBtn.setTooltip(new Tooltip("Toggle always-on-top window behavior"));
        pinBtn.setOnAction(e -> stageManager.setAlwaysOnTop(pinBtn.isSelected()));

        // Theme Toggle Button
        Button themeBtn = new Button("Theme");
        themeBtn.getStyleClass().add("window-control-btn");
        themeBtn.setTooltip(new Tooltip("Toggle Dark / Light Theme"));
        themeBtn.setOnAction(e -> {
            isDarkMode = !isDarkMode;
            if (onThemeToggle != null) onThemeToggle.run();
        });

        // Window Controls (Min, Max, Close)
        Button minBtn = new Button("—");
        minBtn.getStyleClass().add("window-control-btn");
        minBtn.setOnAction(e -> stageManager.minimize());

        Button maxBtn = new Button("□");
        maxBtn.getStyleClass().add("window-control-btn");
        maxBtn.setOnAction(e -> stageManager.toggleMaximize());

        Button closeBtn = new Button("✕");
        closeBtn.getStyleClass().addAll("window-control-btn", "window-control-btn-close");
        closeBtn.setOnAction(e -> stageManager.close());

        HBox windowControls = new HBox(4, minBtn, maxBtn, closeBtn);

        titlebar.getChildren().addAll(brand, versionBadge, spacer, modeBadge, opacityLabel, opacitySlider, pinBtn, themeBtn, windowControls);
        stageManager.setupDragHandlers(titlebar);

        return titlebar;
    }

    private Node buildSidebar() {
        sidebar.getStyleClass().add("sidebar");

        Label menuHeader = new Label("WORKSPACE");
        menuHeader.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px; -fx-font-weight: bold; -fx-padding: 0 0 8px 10px;");

        sidebar.getChildren().add(menuHeader);
        return sidebar;
    }

    public void addNavButton(String title, Runnable action, boolean isActive) {
        Button btn = new Button(title);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("nav-button");
        if (isActive) {
            btn.getStyleClass().add("nav-button-active");
        }
        btn.setOnAction(e -> {
            for (Node n : sidebar.getChildren()) {
                if (n instanceof Button b) {
                    b.getStyleClass().remove("nav-button-active");
                }
            }
            btn.getStyleClass().add("nav-button-active");
            action.run();
        });
        sidebar.getChildren().add(btn);
    }

    public void toggleOperatingMode() {
        if (modeBadge.getText().contains("OFFLINE")) {
            modeBadge.setText("ONLINE MODE");
            modeBadge.getStyleClass().remove("badge-mode-offline");
            modeBadge.getStyleClass().add("badge-mode-online");
        } else {
            modeBadge.setText("OFFLINE MODE");
            modeBadge.getStyleClass().remove("badge-mode-online");
            modeBadge.getStyleClass().add("badge-mode-offline");
        }
    }

    public boolean isDarkMode() {
        return isDarkMode;
    }

    public void setOnThemeToggle(Runnable onThemeToggle) {
        this.onThemeToggle = onThemeToggle;
    }
}
