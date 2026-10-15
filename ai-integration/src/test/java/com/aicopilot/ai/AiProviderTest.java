package com.aicopilot.ai;

import com.aicopilot.ai.model.*;
import com.aicopilot.ai.provider.LocalAiProvider;
import com.aicopilot.ai.provider.MockAiProvider;
import com.aicopilot.ai.provider.OnlineAiProvider;
import com.aicopilot.ai.service.AiServiceRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AiProviderTest {

    private MockAiProvider mockAiProvider;
    private OnlineAiProvider onlineAiProvider;
    private LocalAiProvider localAiProvider;
    private AiServiceRouter router;

    @BeforeEach
    void setUp() {
        mockAiProvider = new MockAiProvider();
        onlineAiProvider = new OnlineAiProvider(mockAiProvider);
        localAiProvider = new LocalAiProvider(mockAiProvider);
        router = new AiServiceRouter(onlineAiProvider, localAiProvider, mockAiProvider);
    }

    @Test
    @DisplayName("Mock AI generates technical Java answer with line-by-line explanation")
    void testMockAiJavaExplanation() {
        AiRequest request = new AiRequest(
            "Explain HashMap collision handling",
            "Java",
            "Senior",
            "DETAILED",
            "English",
            true
        );

        AiResponse response = mockAiProvider.generateAnswer(request);
        assertNotNull(response);
        assertNotNull(response.getAnswer());
        assertTrue(response.getAnswer().contains("HashMap"));
        assertNotNull(response.getCodeSnippet());
        assertFalse(response.getLineByLineExplanation().isEmpty());
        assertFalse(response.getFollowUpQuestions().isEmpty());
    }

    @Test
    @DisplayName("Practice answer evaluation scores accuracy, completeness, and communication")
    void testAnswerEvaluation() {
        PracticeEvaluationRequest req = new PracticeEvaluationRequest(
            "Explain how the Saga pattern manages distributed transactions.",
            "The Saga pattern manages transactions by executing local transactions sequentially and applying compensating transactions if any step fails.",
            "Senior Java Backend Engineer",
            "Microservices",
            "Senior",
            "English"
        );

        PracticeEvaluationResponse res = mockAiProvider.evaluateAnswer(req);
        assertNotNull(res);
        assertTrue(res.getScoreAccuracy() >= 50 && res.getScoreAccuracy() <= 100);
        assertTrue(res.getScoreCompleteness() >= 50 && res.getScoreCompleteness() <= 100);
        assertTrue(res.getScoreCommunication() >= 50 && res.getScoreCommunication() <= 100);
        assertNotNull(res.getFeedback());
        assertNotNull(res.getIdealAnswerSample());
    }

    @Test
    @DisplayName("Offline router isolates network and routes to mock/local provider")
    void testOfflineNetworkIsolation() {
        router.setOperatingMode(AiServiceRouter.OperatingMode.OFFLINE);
        assertEquals(AiServiceRouter.OperatingMode.OFFLINE, router.getOperatingMode());

        AiRequest request = new AiRequest("What is Spring Boot auto-configuration?", "Spring Boot", "Mid-Level", "SHORT", "English", false);
        AiResponse res = router.generateAnswer(request);
        assertNotNull(res);
        assertTrue(res.getModeUsed().contains("OFFLINE") || res.getModeUsed().contains("LOCAL"));
    }

    @Test
    @DisplayName("Multilingual translation translates technical concepts into Hindi and Marathi")
    void testMultilingualTranslations() {
        String original = "Java Spring Boot architecture and memory optimization";

        String hindi = mockAiProvider.translate(original, "Hindi");
        assertNotNull(hindi);
        assertTrue(hindi.contains("जावा") || hindi.contains("हिंदी"));

        String marathi = mockAiProvider.translate(original, "Marathi");
        assertNotNull(marathi);
        assertTrue(marathi.contains("जावा") || marathi.contains("मराठी"));
    }

    @Test
    @DisplayName("Offline question generation pulls relevant domain questions")
    void testQuestionGeneration() {
        List<String> questions = mockAiProvider.generateQuestions(Arrays.asList("Java", "SQL"), "Java", "Senior", 3);
        assertNotNull(questions);
        assertEquals(3, questions.size());
    }
}
