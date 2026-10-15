package com.aicopilot.desktop;

import com.aicopilot.ai.provider.LocalAiProvider;
import com.aicopilot.ai.provider.MockAiProvider;
import com.aicopilot.ai.provider.OnlineAiProvider;
import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.desktop.stage.StageManager;
import com.aicopilot.desktop.ui.MainLayout;
import com.aicopilot.desktop.ui.views.*;
import com.aicopilot.service.CodeSandboxService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CopilotApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(CopilotApp.class);

    private StageManager stageManager;
    private MainLayout mainLayout;
    private Scene scene;
    private AiServiceRouter aiRouter;
    private CodeSandboxService sandboxService;

    @Override
    public void start(Stage primaryStage) {
        log.info("Launching AI Interview Copilot Desktop Dashboard...");

        // Initialize Embedded Services for Seamless Offline/Desktop execution
        MockAiProvider mockProvider = new MockAiProvider();
        OnlineAiProvider onlineProvider = new OnlineAiProvider(mockProvider);
        LocalAiProvider localProvider = new LocalAiProvider(mockProvider);
        this.aiRouter = new AiServiceRouter(onlineProvider, localProvider, mockProvider);
        this.sandboxService = new CodeSandboxService(aiRouter);

        // Custom Frameless Window
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("AI Interview Copilot");
        primaryStage.setMinWidth(850);
        primaryStage.setMinHeight(600);

        this.stageManager = new StageManager(primaryStage);
        this.mainLayout = new MainLayout(stageManager);

        setupNavigation();

        scene = new Scene(mainLayout.getRoot(), 1280, 820);
        applyTheme(true);

        mainLayout.setOnThemeToggle(() -> {
            applyTheme(mainLayout.isDarkMode());
        });

        primaryStage.setScene(scene);
        stageManager.restoreSavedWindowBounds();
        primaryStage.show();

        // Default screen: Dashboard
        showDashboard();
    }

    private void applyTheme(boolean isDark) {
        scene.getStylesheets().clear();
        String themeCss = isDark ? "/styles/dark-theme.css" : "/styles/light-theme.css";
        var res = getClass().getResource(themeCss);
        if (res != null) {
            scene.getStylesheets().add(res.toExternalForm());
        }
    }

    private void setupNavigation() {
        mainLayout.addNavButton("📊 Dashboard", this::showDashboard, true);
        mainLayout.addNavButton("🎯 Mock Interview", this::showPractice, false);
        mainLayout.addNavButton("📄 Resume Intelligence", this::showResume, false);
        mainLayout.addNavButton("💻 Coding Sandbox", this::showCoding, false);
        mainLayout.addNavButton("💡 AI Assistant Q&A", this::showAssistant, false);
        mainLayout.addNavButton("🎙 Speech & Voice", this::showVoice, false);
        mainLayout.addNavButton("🌐 Multilingual", this::showLanguage, false);
        mainLayout.addNavButton("📜 Interview History", this::showHistory, false);
        mainLayout.addNavButton("📈 Performance Reports", () -> showReports("sess-01"), false);
        mainLayout.addNavButton("⚙ Window & Settings", this::showSettings, false);
        mainLayout.addNavButton("🤖 AI Model Manager", this::showModelManager, false);
        mainLayout.addNavButton("🛡 Privacy & Policy", this::showPrivacySecurity, false);
        mainLayout.addNavButton("👋 Welcome / Info", this::showWelcome, false);
        mainLayout.addNavButton("🔐 Account / Login", this::showAuth, false);
    }

    private void setView(javafx.scene.Node viewNode) {
        mainLayout.getContentArea().getChildren().setAll(viewNode);
    }

    private void showDashboard() {
        setView(DashboardView.createView(this::showPractice, this::showResume, this::showCoding, this::showAssistant));
    }

    private void showWelcome() {
        setView(WelcomeView.createView(this::showPractice, this::showResume));
    }

    private void showAuth() {
        setView(AuthView.createView(email -> showDashboard()));
    }

    private void showPractice() {
        setView(InterviewPracticeView.createView(aiRouter, this::showReports));
    }

    private void showResume() {
        setView(ResumeView.createView());
    }

    private void showAssistant() {
        setView(AiAssistantView.createView(aiRouter));
    }

    private void showCoding() {
        setView(CodingView.createView(sandboxService));
    }

    private void showVoice() {
        setView(VoiceInputView.createView());
    }

    private void showLanguage() {
        setView(LanguageView.createView(aiRouter));
    }

    private void showHistory() {
        setView(HistoryView.createView(this::showReports));
    }

    private void showReports(String sessionId) {
        setView(ReportsView.createView(sessionId));
    }

    private void showSettings() {
        setView(SettingsView.createView(stageManager, aiRouter));
    }

    private void showModelManager() {
        setView(ModelManagerView.createView());
    }

    private void showPrivacySecurity() {
        setView(PrivacySecurityView.createView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
