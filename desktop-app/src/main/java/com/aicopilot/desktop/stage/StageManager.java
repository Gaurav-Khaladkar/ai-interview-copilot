package com.aicopilot.desktop.stage;

import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.Preferences;

public class StageManager {

    private static final Logger log = LoggerFactory.getLogger(StageManager.class);

    private static final String PREF_OPACITY = "window_opacity";
    private static final String PREF_ALWAYS_ON_TOP = "always_on_top";
    private static final String PREF_WIDTH = "window_width";
    private static final String PREF_HEIGHT = "window_height";
    private static final String PREF_X = "window_x";
    private static final String PREF_Y = "window_y";

    public enum LayoutMode {
        COMPACT(900, 650),
        STANDARD(1280, 820),
        EXPANDED(1550, 950);

        private final double width;
        private final double height;

        LayoutMode(double width, double height) {
            this.width = width;
            this.height = height;
        }

        public double getWidth() { return width; }
        public double getHeight() { return height; }
    }

    private final Stage stage;
    private final Preferences preferences = Preferences.userNodeForPackage(StageManager.class);
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isMaximized = false;
    private double prevX, prevY, prevW, prevH;

    public StageManager(Stage stage) {
        this.stage = stage;
    }

    public void restoreSavedWindowBounds() {
        double width = preferences.getDouble(PREF_WIDTH, 1280);
        double height = preferences.getDouble(PREF_HEIGHT, 820);
        double x = preferences.getDouble(PREF_X, 100);
        double y = preferences.getDouble(PREF_Y, 100);
        double opacity = preferences.getDouble(PREF_OPACITY, 1.0);
        boolean alwaysOnTop = preferences.getBoolean(PREF_ALWAYS_ON_TOP, false);

        stage.setWidth(width);
        stage.setHeight(height);

        // Ensure window coordinates are visible on screen
        if (isCoordinatesOnScreen(x, y)) {
            stage.setX(x);
            stage.setY(y);
        } else {
            stage.centerOnScreen();
        }

        setOpacity(opacity);
        setAlwaysOnTop(alwaysOnTop);

        // Save bounds when window closes
        stage.setOnCloseRequest(e -> saveWindowBounds());
    }

    public void saveWindowBounds() {
        if (!isMaximized) {
            preferences.putDouble(PREF_WIDTH, stage.getWidth());
            preferences.putDouble(PREF_HEIGHT, stage.getHeight());
            preferences.putDouble(PREF_X, stage.getX());
            preferences.putDouble(PREF_Y, stage.getY());
        }
        preferences.putDouble(PREF_OPACITY, stage.getOpacity());
        preferences.putBoolean(PREF_ALWAYS_ON_TOP, stage.isAlwaysOnTop());
        try {
            preferences.flush();
        } catch (Exception ignored) {}
    }

    public void setOpacity(double value) {
        // Enforce 30% to 100% boundary as required by specifications
        double bounded = Math.max(0.30, Math.min(1.0, value));
        try {
            stage.setOpacity(bounded);
            preferences.putDouble(PREF_OPACITY, bounded);
        } catch (Exception e) {
            log.warn("Could not set stage opacity: {}", e.getMessage());
        }
    }

    public double getOpacity() {
        return stage.getOpacity();
    }

    public void setAlwaysOnTop(boolean value) {
        try {
            stage.setAlwaysOnTop(value);
            preferences.putBoolean(PREF_ALWAYS_ON_TOP, value);
        } catch (Exception e) {
            log.warn("Could not set always on top: {}", e.getMessage());
        }
    }

    public boolean isAlwaysOnTop() {
        return stage.isAlwaysOnTop();
    }

    public void setLayoutMode(LayoutMode mode) {
        stage.setWidth(mode.getWidth());
        stage.setHeight(mode.getHeight());
        stage.centerOnScreen();
        saveWindowBounds();
    }

    public void setupDragHandlers(javafx.scene.Node titleBar) {
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            if (!isMaximized) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        titleBar.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                toggleMaximize();
            }
        });
    }

    public void minimize() {
        stage.setIconified(true);
    }

    public void toggleMaximize() {
        if (!isMaximized) {
            prevX = stage.getX();
            prevY = stage.getY();
            prevW = stage.getWidth();
            prevH = stage.getHeight();

            Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D bounds = screen.getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            isMaximized = true;
        } else {
            stage.setX(prevX);
            stage.setY(prevY);
            stage.setWidth(prevW);
            stage.setHeight(prevH);
            isMaximized = false;
        }
    }

    public void close() {
        saveWindowBounds();
        stage.close();
        System.exit(0);
    }

    private boolean isCoordinatesOnScreen(double x, double y) {
        for (Screen screen : Screen.getScreens()) {
            if (screen.getVisualBounds().contains(x, y)) {
                return true;
            }
        }
        return false;
    }
}
