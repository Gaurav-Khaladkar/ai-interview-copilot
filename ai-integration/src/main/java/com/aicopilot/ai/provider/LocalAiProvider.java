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

@Component("localAiProvider")
public class LocalAiProvider implements AiProvider {

    private static final Logger log = LoggerFactory.getLogger(LocalAiProvider.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final MockAiProvider fallbackProvider;

    @Value("${ai.local.endpoint:http://localhost:11434/api/generate}")
    private String localEndpoint;

    @Value("${ai.local.model:llama3:8b}")
    private String localModel;

    public LocalAiProvider(MockAiProvider fallbackProvider) {
        this.fallbackProvider = fallbackProvider;
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
    }

    @Override
    public AiResponse generateAnswer(AiRequest request) {
        if (!isHealthy()) {
            log.info("Local LLM at {} is offline or not installed. Using local offline knowledge base.", localEndpoint);
            AiResponse res = fallbackProvider.generateAnswer(request);
            res.setModeUsed("OFFLINE_KNOWLEDGE_BASE (Local LLM Daemon Not Running)");
            return res;
        }

        try {
            String prompt = String.format(
                "You are an interview preparation assistant. Answer this %s question (%s): %s. Language: %s",
                request.getDomain(), request.getDifficulty(), request.getQuestion(), request.getTargetLanguage()
            );

            Map<String, Object> payload = Map.of(
                "model", localModel,
                "prompt", prompt,
                "stream", false
            );

            String requestBody = objectMapper.writeValueAsString(payload);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(localEndpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(httpResponse.body());
                String responseText = root.path("response").asText();

                AiResponse response = new AiResponse();
                response.setAnswer(responseText);
                response.setModeUsed(getProviderName());
                response.setLanguage(request.getTargetLanguage());
                response.setConceptSummary("Local Inference: " + localModel);
                return response;
            } else {
                AiResponse res = fallbackProvider.generateAnswer(request);
                res.setModeUsed("OFFLINE_FALLBACK (Local HTTP " + httpResponse.statusCode() + ")");
                return res;
            }
        } catch (Exception e) {
            log.warn("Local LLM request failed: {}. Falling back to built-in knowledge base.", e.getMessage());
            AiResponse res = fallbackProvider.generateAnswer(request);
            res.setModeUsed("OFFLINE_KNOWLEDGE_BASE");
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
        try {
            // Ping Ollama root or tags
            String basePing = localEndpoint.replace("/api/generate", "/api/tags");
            HttpRequest pingRequest = HttpRequest.newBuilder()
                .uri(URI.create(basePing))
                .timeout(Duration.ofSeconds(2))
                .GET()
                .build();
            HttpResponse<Void> resp = httpClient.send(pingRequest, HttpResponse.BodyHandlers.discarding());
            return resp.statusCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getProviderName() {
        return "LOCAL_LLM (" + localModel + ")";
    }

    public void setLocalEndpoint(String localEndpoint) { this.localEndpoint = localEndpoint; }
    public void setLocalModel(String localModel) { this.localModel = localModel; }
}
