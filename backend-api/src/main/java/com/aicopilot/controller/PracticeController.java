package com.aicopilot.controller;

import com.aicopilot.dto.*;
import com.aicopilot.security.SecurityUtils;
import com.aicopilot.service.PracticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/practice")
@Tag(name = "Practice Sessions", description = "Mock interview session lifecycle, evaluation scoring, and report analytics")
public class PracticeController {

    private final PracticeService practiceService;
    private final SecurityUtils securityUtils;

    public PracticeController(PracticeService practiceService, SecurityUtils securityUtils) {
        this.practiceService = practiceService;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/sessions")
    @Operation(summary = "Start new mock interview practice session")
    public ResponseEntity<ApiResponse<PracticeSessionResponseDto>> createSession(@Valid @RequestBody PracticeSessionRequest request) {
        String userId = securityUtils.getCurrentUserId();
        PracticeSessionResponseDto response = practiceService.createSession(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Session created.", response));
    }

    @PostMapping("/sessions/{id}/answers")
    @Operation(summary = "Submit candidate response and receive immediate AI evaluation score and next question")
    public ResponseEntity<ApiResponse<PracticeFeedbackDto>> submitAnswer(
            @PathVariable String id,
            @Valid @RequestBody PracticeAnswerRequest request) {
        String userId = securityUtils.getCurrentUserId();
        PracticeFeedbackDto feedback = practiceService.submitAnswer(userId, id, request);
        return ResponseEntity.ok(ApiResponse.ok("Answer evaluated.", feedback));
    }

    @GetMapping("/reports/{sessionId}")
    @Operation(summary = "Get comprehensive performance scorecard and report analytics for a session")
    public ResponseEntity<ApiResponse<PracticeReportDto>> getReport(@PathVariable String sessionId) {
        String userId = securityUtils.getCurrentUserId();
        PracticeReportDto report = practiceService.getReport(userId, sessionId);
        return ResponseEntity.ok(ApiResponse.ok(report));
    }

    @GetMapping("/history")
    @Operation(summary = "List candidate interview history and past sessions")
    public ResponseEntity<ApiResponse<List<PracticeSessionResponseDto>>> getHistory() {
        String userId = securityUtils.getCurrentUserId();
        List<PracticeSessionResponseDto> history = practiceService.getUserHistory(userId);
        return ResponseEntity.ok(ApiResponse.ok(history));
    }
}
