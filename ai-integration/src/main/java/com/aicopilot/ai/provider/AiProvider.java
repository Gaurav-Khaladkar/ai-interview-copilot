package com.aicopilot.ai.provider;

import com.aicopilot.ai.model.*;
import java.util.List;

public interface AiProvider {
    AiResponse generateAnswer(AiRequest request);
    List<String> generateQuestions(List<String> skills, String domain, String difficulty, int count);
    PracticeEvaluationResponse evaluateAnswer(PracticeEvaluationRequest request);
    CodeAnalysisResponse analyzeCode(CodeAnalysisRequest request);
    String translate(String text, String targetLanguage);
    boolean isHealthy();
    String getProviderName();
}
