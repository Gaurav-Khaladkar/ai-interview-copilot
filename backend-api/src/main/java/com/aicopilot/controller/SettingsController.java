package com.aicopilot.controller;

import com.aicopilot.dto.ApiResponse;
import com.aicopilot.dto.SettingsDto;
import com.aicopilot.security.SecurityUtils;
import com.aicopilot.service.SettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@Tag(name = "Settings & Window Customization", description = "User settings, opacity, always-on-top, themes, and model management")
public class SettingsController {

    private final SettingsService settingsService;
    private final SecurityUtils securityUtils;

    public SettingsController(SettingsService settingsService, SecurityUtils securityUtils) {
        this.settingsService = settingsService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    @Operation(summary = "Get user settings and desktop stage preferences")
    public ResponseEntity<ApiResponse<SettingsDto>> getSettings() {
        String userId = securityUtils.getCurrentUserId();
        SettingsDto settings = settingsService.getSettings(userId);
        return ResponseEntity.ok(ApiResponse.ok(settings));
    }

    @PutMapping
    @Operation(summary = "Update user settings, transparency, always-on-top, and active mode")
    public ResponseEntity<ApiResponse<SettingsDto>> updateSettings(@RequestBody SettingsDto request) {
        String userId = securityUtils.getCurrentUserId();
        SettingsDto updated = settingsService.updateSettings(userId, request);
        return ResponseEntity.ok(ApiResponse.ok("Settings updated successfully.", updated));
    }
}
