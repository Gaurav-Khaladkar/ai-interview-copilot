package com.aicopilot.ai.provider;

import com.aicopilot.ai.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Component("onlineAiProvider")
public class OnlineAiProvider implements AiProvider {

    private static final Logger log = LoggerFactory.getLogger(OnlineAiProvider.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final MockAiProvider fallbackProvider;

    @Value("${ai.online.api-key:${OPENAI_API_KEY:}}")
    private String apiKey;

    @Value("${ai.online.endpoint:https://api.openai.com/v1/chat/completions}")
    private String endpointUrl;

    @Value("${ai.online.model:gpt-4o-mini}")
    private String modelName;

    public OnlineAiProvider(MockAiProvider fallbackProvider) {
        this.fallbackProvider = fallbackProvider;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    @Override
    public AiResponse generateAnswer(AiRequest request) {
        if (!isConfigured()) {
            log.info("Online AI provider not configured with API key. Falling back to offline knowledge base.");
            AiResponse res = fallbackProvider.generateAnswer(request);
            res.setModeUsed("OFFLINE_FALLBACK (No API Key)");
            return res;
        }

        try {
            String prompt = String.format(
                "You are an expert technical interviewer. Answer this %s interview question for %s level in %s format: %s. " +
                "Language: %s. Include code snippet and brief line-by-line explanation if applicable.",
                request.getDomain(), request.getDifficulty(), request.getFormat(), request.getQuestion(), request.getTargetLanguage()
            );

            Map<String, Object> message = Map.of("role", "user", "content", prompt);
            Map<String, Object> payload = Map.of(
                "model", modelName,
                "messages", List.of(message),
                "temperature", 0.7
            );

            String requestJson = objectMapper.writeValueAsString(payload);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .timeout(Duration.ofSeconds(15))
                .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(httpResponse.body());
                String content = root.path("choices").get(0).path("message").path("content").asText();

                AiResponse response = new AiResponse();
                response.setAnswer(content);
                response.setModeUsed(getProviderName());
                response.setLanguage(request.getTargetLanguage());
                response.setConceptSummary("Generated via " + modelName);
                return response;
            } else {
                log.warn("Online AI request failed with HTTP {}: {}. Falling back to offline engine.",
                    httpResponse.statusCode(), httpResponse.body());
                AiResponse res = fallbackProvider.generateAnswer(request);
                res.setModeUsed("OFFLINE_FALLBACK (HTTP " + httpResponse.statusCode() + ")");
                return res;
            }
        } catch (Exception e) {
            log.error("Exception communicating with online AI service: {}. Falling back.", e.getMessage());
            AiResponse res = fallbackProvider.generateAnswer(request);
            res.setModeUsed("OFFLINE_FALLBACK (" + e.getClass().getSimpleName() + ")");
            return res;
        }
    }

    @Override
    public List<String> generateQuestions(List<String> skills, String domain, String difficulty, int count) {
        return fallbackProvider.generateQuestions(skills, domain, difficulty, count);
    }

    @Override
    public PracticeEvaluationResponse evaluateAnswer(PracticeEvaluationRequest request) {
        return fallbackProvider.evaluateAnswer(request);
    }

    @Override
    public CodeAnalysisResponse analyzeCode(CodeAnalysisRequest request) {
        return fallbackProvider.analyzeCode(request);
    }

    @Override
    public String translate(String text, String targetLanguage) {
        return fallbackProvider.translate(text, targetLanguage);
    }

    @Override
    public boolean isHealthy() {
        return isConfigured();
    }

    @Override
    public String getProviderName() {
        return "ONLINE_AI (" + modelName + ")";
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.trim().isEmpty() && !apiKey.startsWith("demo_");
    }

    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    public void setEndpointUrl(String endpointUrl) { this.endpointUrl = endpointUrl; }
    public void setModelName(String modelName) { this.modelName = modelName; }
}
