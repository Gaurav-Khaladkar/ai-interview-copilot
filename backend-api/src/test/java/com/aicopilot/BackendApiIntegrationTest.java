package com.aicopilot;

import com.aicopilot.ai.provider.LocalAiProvider;
import com.aicopilot.ai.provider.MockAiProvider;
import com.aicopilot.ai.provider.OnlineAiProvider;
import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.CodeExecutionRequestDto;
import com.aicopilot.dto.CodeExecutionResponseDto;
import com.aicopilot.security.JwtTokenProvider;
import com.aicopilot.service.CodeSandboxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BackendApiIntegrationTest {

    @Test
    @DisplayName("BCrypt password encoder produces secure hashes with strength 12")
    void testPasswordHashing() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String rawPassword = "CandidatePassword2026!";

        String hash = encoder.encode(rawPassword);
        assertNotNull(hash);
        assertNotEquals(rawPassword, hash);
        assertTrue(encoder.matches(rawPassword, hash));
        assertFalse(encoder.matches("WrongPassword", hash));
    }

    @Test
    @DisplayName("JWT Token generation and signature validation")
    void testJwtTokenCycle() {
        JwtTokenProvider jwtProvider = new JwtTokenProvider();
        // Set secret and expiration using reflection or test defaults
        String userId = UUID.randomUUID().toString();
        String email = "candidate@example.com";
        String role = "ROLE_CANDIDATE";

        String token = jwtProvider.generateToken(userId, email, role);
        assertNotNull(token);
        assertTrue(jwtProvider.validateToken(token));
        assertEquals(userId, jwtProvider.getUserIdFromToken(token));
        assertEquals(email, jwtProvider.getEmailFromToken(token));
    }

    @Test
    @DisplayName("Code Sandbox intercepts security violations and blocks untrusted host calls")
    void testSandboxSecurityEnforcement() {
        MockAiProvider mockAi = new MockAiProvider();
        OnlineAiProvider online = new OnlineAiProvider(mockAi);
        LocalAiProvider local = new LocalAiProvider(mockAi);
        AiServiceRouter router = new AiServiceRouter(online, local, mockAi);
        CodeSandboxService sandbox = new CodeSandboxService(router);

        CodeExecutionRequestDto maliciousReq = new CodeExecutionRequestDto();
        maliciousReq.setLanguage("JAVA");
        maliciousReq.setSourceCode("class Solution { public void hack() { System.exit(1); Runtime.getRuntime().exec(\"calc.exe\"); } }");
        maliciousReq.setProblemId("TEST");

        CodeExecutionResponseDto response = sandbox.executeAndAnalyze(maliciousReq);
        assertNotNull(response);
        assertEquals("SECURITY_VIOLATION", response.getStatus());
        assertTrue(response.getOutput().contains("Security Policy Violation"));
    }

    @Test
    @DisplayName("Code Sandbox executes valid algorithmic problem and calculates complexity")
    void testSandboxValidExecution() {
        MockAiProvider mockAi = new MockAiProvider();
        OnlineAiProvider online = new OnlineAiProvider(mockAi);
        LocalAiProvider local = new LocalAiProvider(mockAi);
        AiServiceRouter router = new AiServiceRouter(online, local, mockAi);
        CodeSandboxService sandbox = new CodeSandboxService(router);

        CodeExecutionRequestDto validReq = new CodeExecutionRequestDto();
        validReq.setLanguage("JAVA");
        validReq.setSourceCode("class Solution { public int[] twoSum(int[] nums, int target) { return new int[]{0, 1}; } }");
        validReq.setProblemId("TWO_SUM");

        CodeExecutionResponseDto response = sandbox.executeAndAnalyze(validReq);
        assertNotNull(response);
        assertEquals("PASSED", response.getStatus());
        assertEquals(3, response.getTestsPassed());
        assertEquals("O(N)", response.getTimeComplexity());
    }
}
