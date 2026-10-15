package com.aicopilot.controller;

import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "System Diagnostics", description = "Application health, memory usage and operating status")
public class HealthController {

    private final AiServiceRouter aiRouter;

    public HealthController(AiServiceRouter aiRouter) {
        this.aiRouter = aiRouter;
    }

    @GetMapping
    @Operation(summary = "Check backend API health and current AI mode")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHealth() {
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);

        Map<String, Object> details = Map.of(
            "status", "UP",
            "operatingMode", aiRouter.getOperatingMode(),
            "activeAiProvider", aiRouter.getActiveProviderName(),
            "uptimeSeconds", uptimeMs / 1000,
            "memoryUsedMb", totalMemory - freeMemory,
            "totalMemoryMb", totalMemory,
            "timestamp", Instant.now().toString()
        );

        return ResponseEntity.ok(ApiResponse.ok(details));
    }
}
