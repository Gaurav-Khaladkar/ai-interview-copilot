package com.aicopilot.controller;

import com.aicopilot.dto.ApiResponse;
import com.aicopilot.dto.CodeExecutionRequestDto;
import com.aicopilot.dto.CodeExecutionResponseDto;
import com.aicopilot.service.CodeSandboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coding")
@Tag(name = "Coding Practice", description = "Sandboxed code execution, test verification, and Big-O complexity analysis")
public class CodingController {

    private final CodeSandboxService sandboxService;

    public CodingController(CodeSandboxService sandboxService) {
        this.sandboxService = sandboxService;
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute candidate code inside isolated sandbox and evaluate correctness")
    public ResponseEntity<ApiResponse<CodeExecutionResponseDto>> executeCode(@Valid @RequestBody CodeExecutionRequestDto request) {
        CodeExecutionResponseDto response = sandboxService.executeAndAnalyze(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/problems")
    @Operation(summary = "Get curated practice programming problems with test cases")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getProblems() {
        List<Map<String, String>> problems = sandboxService.getSampleProblems();
        return ResponseEntity.ok(ApiResponse.ok(problems));
    }
}
