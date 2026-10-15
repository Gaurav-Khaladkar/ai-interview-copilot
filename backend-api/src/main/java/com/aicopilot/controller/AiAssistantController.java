package com.aicopilot.controller;

import com.aicopilot.ai.model.AiRequest;
import com.aicopilot.ai.model.AiResponse;
import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.ApiResponse;
import com.aicopilot.dto.AiAnswerRequest;
import com.aicopilot.dto.TranslationRequest;
import com.aicopilot.dto.TranslationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/assistant")
@Tag(name = "AI Interview Assistant", description = "AI answer generation, technical explanations, and translations")
public class AiAssistantController {

    private final AiServiceRouter aiRouter;

    public AiAssistantController(AiServiceRouter aiRouter) {
        this.aiRouter = aiRouter;
    }

    @PostMapping("/answers")
    @Operation(summary = "Generate interview answer with technical breakdown")
    public ResponseEntity<ApiResponse<AiResponse>> generateAnswer(@Valid @RequestBody AiAnswerRequest request) {
        AiRequest aiReq = new AiRequest(
            request.getQuestion(),
            request.getDomain(),
            request.getDifficulty(),
            request.getFormat(),
            request.getTargetLanguage(),
            request.isIncludeCodeExample()
        );

        AiResponse response = aiRouter.generateAnswer(aiReq);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/questions")
    @Operation(summary = "Generate interview questions for a specific technical domain")
    public ResponseEntity<ApiResponse<List<String>>> generateQuestions(
            @RequestParam(required = false, defaultValue = "Java") String domain,
            @RequestParam(required = false, defaultValue = "Mid-Level") String difficulty,
            @RequestParam(required = false, defaultValue = "5") int count) {
        List<String> questions = aiRouter.generateQuestions(Collections.singletonList(domain), domain, difficulty, count);
        return ResponseEntity.ok(ApiResponse.ok(questions));
    }

    @PostMapping("/explanations")
    @Operation(summary = "Generate in-depth architectural explanation for a technical concept")
    public ResponseEntity<ApiResponse<AiResponse>> generateExplanation(@Valid @RequestBody AiAnswerRequest request) {
        request.setFormat("DETAILED");
        AiRequest aiReq = new AiRequest(
            request.getQuestion(),
            request.getDomain(),
            request.getDifficulty(),
            "DETAILED",
            request.getTargetLanguage(),
            true
        );

        AiResponse response = aiRouter.generateAnswer(aiReq);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/translate")
    @Operation(summary = "Translate question or technical explanation into Hindi, Marathi or configured locale")
    public ResponseEntity<ApiResponse<TranslationResponse>> translate(@Valid @RequestBody TranslationRequest request) {
        String translated = aiRouter.translate(request.getText(), request.getTargetLanguage());
        TranslationResponse response = new TranslationResponse(translated, request.getText(), request.getTargetLanguage());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
