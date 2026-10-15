package com.aicopilot.ai.service;

import com.aicopilot.ai.model.*;
import com.aicopilot.ai.provider.AiProvider;
import com.aicopilot.ai.provider.LocalAiProvider;
import com.aicopilot.ai.provider.MockAiProvider;
import com.aicopilot.ai.provider.OnlineAiProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class AiServiceRouter {

    private static final Logger log = LoggerFactory.getLogger(AiServiceRouter.class);

    public enum OperatingMode {
        ONLINE,
        OFFLINE
    }

    private final OnlineAiProvider onlineProvider;
    private final LocalAiProvider localProvider;
    private final MockAiProvider mockProvider;

    private final AtomicReference<OperatingMode> currentMode = new AtomicReference<>(OperatingMode.OFFLINE);

    public AiServiceRouter(OnlineAiProvider onlineProvider, LocalAiProvider localProvider, MockAiProvider mockProvider) {
        this.onlineProvider = onlineProvider;
        this.localProvider = localProvider;
        this.mockProvider = mockProvider;
    }

    public OperatingMode getOperatingMode() {
        return currentMode.get();
    }

    public void setOperatingMode(OperatingMode mode) {
        log.info("Switching AI Operating Mode from {} to {}", currentMode.get(), mode);
        this.currentMode.set(mode);
    }

    public AiProvider getActiveProvider() {
        OperatingMode mode = currentMode.get();
        if (mode == OperatingMode.ONLINE) {
            if (onlineProvider != null && onlineProvider.isConfigured()) {
                return onlineProvider;
            } else {
                log.info("Online mode active but no API key configured. Routing to local/mock provider.");
            }
        }

        // Guaranteed Offline path: No outbound network requests to internet
        if (localProvider != null && localProvider.isHealthy()) {
            return localProvider;
        }

        return mockProvider;
    }

    public AiResponse generateAnswer(AiRequest request) {
        return getActiveProvider().generateAnswer(request);
    }

    public List<String> generateQuestions(List<String> skills, String domain, String difficulty, int count) {
        return getActiveProvider().generateQuestions(skills, domain, difficulty, count);
    }

    public PracticeEvaluationResponse evaluateAnswer(PracticeEvaluationRequest request) {
        return getActiveProvider().evaluateAnswer(request);
    }

    public CodeAnalysisResponse analyzeCode(CodeAnalysisRequest request) {
        return getActiveProvider().analyzeCode(request);
    }

    public String translate(String text, String targetLanguage) {
        return getActiveProvider().translate(text, targetLanguage);
    }

    public String getActiveProviderName() {
        return getActiveProvider().getProviderName();
    }
}
