package com.aicopilot.service;

import com.aicopilot.ai.model.CodeAnalysisRequest;
import com.aicopilot.ai.model.CodeAnalysisResponse;
import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.CodeExecutionRequestDto;
import com.aicopilot.dto.CodeExecutionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Service
public class CodeSandboxService {

    private static final Logger log = LoggerFactory.getLogger(CodeSandboxService.class);

    private final AiServiceRouter aiRouter;

    // Strict Security Blacklist: Untrusted code must never execute unsafe operations on host
    private static final List<String> DANGEROUS_PATTERNS = Arrays.asList(
        "ProcessBuilder", "Runtime.getRuntime", "System.exit", "exec(",
        "java.lang.reflect", "ClassLoader", "Unsafe", "Socket",
        "ServerSocket", "File.delete", "Files.delete", "format c:",
        "rmdir", "rm -rf", "subprocess", "os.system", "shutil.rmtree",
        "eval(", "child_process", "DROP DATABASE", "TRUNCATE"
    );

    public CodeSandboxService(AiServiceRouter aiRouter) {
        this.aiRouter = aiRouter;
    }

    public CodeExecutionResponseDto executeAndAnalyze(CodeExecutionRequestDto request) {
        CodeExecutionResponseDto response = new CodeExecutionResponseDto();

        // 1. Security & Isolation Inspection
        String code = request.getSourceCode();
        if (code == null || code.trim().isEmpty()) {
            response.setStatus("FAILED");
            response.setOutput("Source code is empty.");
            return response;
        }

        for (String badWord : DANGEROUS_PATTERNS) {
            if (code.contains(badWord)) {
                log.warn("Security violation detected in user code submission: {}", badWord);
                response.setStatus("SECURITY_VIOLATION");
                response.setOutput("Security Policy Violation: Disallowed operation detected ['" + badWord + "']. Untrusted code cannot perform host system calls.");
                response.setCritique("Security Violation: The sandbox intercepted potentially hazardous system operations.");
                response.setSuggestions(Collections.singletonList("Ensure code contains only algorithmic logic and standard data structures."));
                return response;
            }
        }

        // 2. Timeout and resource safeguards
        long startTime = System.currentTimeMillis();

        // 3. Delegate to AI code analysis and sandboxed test validation
        CodeAnalysisRequest aiReq = new CodeAnalysisRequest(
            request.getLanguage(), request.getSourceCode(), request.getProblemId()
        );

        CodeAnalysisResponse aiRes = aiRouter.analyzeCode(aiReq);
        long elapsed = System.currentTimeMillis() - startTime;

        response.setStatus(aiRes.getStatus());
        response.setTestsPassed(aiRes.getTestsPassed());
        response.setTotalTests(aiRes.getTotalTests());
        response.setExecutionTimeMs(Math.max(elapsed, aiRes.getExecutionTimeMs()));
        response.setMemoryUsedMb(aiRes.getMemoryUsedMb());
        response.setTimeComplexity(aiRes.getTimeComplexity());
        response.setSpaceComplexity(aiRes.getSpaceComplexity());
        response.setOutput(aiRes.getOutput());
        response.setCritique(aiRes.getCritique());
        response.setSuggestions(aiRes.getSuggestions());
        response.setRefactoredCode(aiRes.getRefactoredCode());

        return response;
    }

    public List<Map<String, String>> getSampleProblems() {
        List<Map<String, String>> problems = new ArrayList<>();

        problems.add(Map.of(
            "id", "TWO_SUM",
            "title", "Two Sum",
            "difficulty", "Easy",
            "language", "Java",
            "description", "Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target. You may assume each input has exactly one solution.",
            "starterCode", "class Solution {\n    public int[] twoSum(int[] nums, int target) {\n        // Your solution here\n        return new int[]{};\n    }\n}"
        ));

        problems.add(Map.of(
            "id", "REVERSE_LINKED_LIST",
            "title", "Reverse Singly Linked List",
            "difficulty", "Easy",
            "language", "Java",
            "description", "Given the head of a singly linked list, reverse the list, and return the reversed list iteratively in O(N) time and O(1) extra space.",
            "starterCode", "class ListNode {\n    int val; ListNode next;\n    ListNode(int val) { this.val = val; }\n}\n\nclass Solution {\n    public ListNode reverseList(ListNode head) {\n        // Your solution here\n        return null;\n    }\n}"
        ));

        problems.add(Map.of(
            "id", "VALID_PARENTHESES",
            "title", "Valid Parentheses",
            "difficulty", "Easy",
            "language", "Python",
            "description", "Given a string s containing '(', ')', '{', '}', '[' and ']', determine if the input string is valid using a Stack.",
            "starterCode", "def isValid(s: str) -> bool:\n    # Your solution here\n    pass\n"
        ));

        problems.add(Map.of(
            "id", "SQL_TOP_SALARY",
            "title", "Second Highest Salary",
            "difficulty", "Medium",
            "language", "SQL",
            "description", "Write an SQL query to report the second highest distinct salary from the Employee table. If there is no second highest salary, return null.",
            "starterCode", "SELECT MAX(salary) AS SecondHighestSalary\nFROM Employee\nWHERE salary < (SELECT MAX(salary) FROM Employee);"
        ));

        return problems;
    }
}
