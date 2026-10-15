package com.aicopilot.service;

import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.SettingsDto;
import com.aicopilot.model.ApplicationPreference;
import com.aicopilot.model.ModelPreference;
import com.aicopilot.model.User;
import com.aicopilot.model.UserSettings;
import com.aicopilot.repository.ApplicationPreferenceRepository;
import com.aicopilot.repository.ModelPreferenceRepository;
import com.aicopilot.repository.UserRepository;
import com.aicopilot.repository.UserSettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class SettingsService {

    private final UserRepository userRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final ApplicationPreferenceRepository appPrefRepository;
    private final ModelPreferenceRepository modelPrefRepository;
    private final AiServiceRouter aiRouter;

    public SettingsService(UserRepository userRepository,
                           UserSettingsRepository userSettingsRepository,
                           ApplicationPreferenceRepository appPrefRepository,
                           ModelPreferenceRepository modelPrefRepository,
                           AiServiceRouter aiRouter) {
        this.userRepository = userRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.appPrefRepository = appPrefRepository;
        this.modelPrefRepository = modelPrefRepository;
        this.aiRouter = aiRouter;
    }

    public SettingsDto getSettings(String userId) {
        UserSettings us = userSettingsRepository.findByUserId(userId).orElse(new UserSettings());
        ApplicationPreference ap = appPrefRepository.findByUserId(userId).orElse(new ApplicationPreference());
        ModelPreference mp = modelPrefRepository.findByUserId(userId).orElse(new ModelPreference());

        SettingsDto dto = new SettingsDto();
        dto.setTargetDomain(us.getTargetDomain());
        dto.setYearsExperience(us.getYearsExperience());
        dto.setPreferredLanguage(us.getPreferredLanguage());
        dto.setDefaultDifficulty(us.getDefaultDifficulty());
        dto.setAutoAudioPlayback(us.isAutoAudioPlayback());

        dto.setTheme(ap.getTheme());
        dto.setWindowOpacity(ap.getWindowOpacity());
        dto.setAlwaysOnTop(ap.isAlwaysOnTop());
        dto.setWindowWidth(ap.getWindowWidth());
        dto.setWindowHeight(ap.getWindowHeight());
        dto.setWindowX(ap.getWindowX());
        dto.setWindowY(ap.getWindowY());
        dto.setActiveMode(ap.getActiveMode());

        dto.setOnlineProvider(mp.getOnlineProvider());
        dto.setOnlineModelName(mp.getOnlineModelName());
        dto.setLocalProvider(mp.getLocalProvider());
        dto.setLocalModelName(mp.getLocalModelName());
        dto.setLocalEndpointUrl(mp.getLocalEndpointUrl());
        dto.setWhisperModel(mp.getWhisperModel());

        return dto;
    }

    @Transactional
    public SettingsDto updateSettings(String userId, SettingsDto dto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        UserSettings us = userSettingsRepository.findByUserId(userId)
            .orElseGet(() -> new UserSettings(user));
        if (dto.getTargetDomain() != null) us.setTargetDomain(dto.getTargetDomain());
        if (dto.getYearsExperience() > 0) us.setYearsExperience(dto.getYearsExperience());
        if (dto.getPreferredLanguage() != null) us.setPreferredLanguage(dto.getPreferredLanguage());
        if (dto.getDefaultDifficulty() != null) us.setDefaultDifficulty(dto.getDefaultDifficulty());
        us.setAutoAudioPlayback(dto.isAutoAudioPlayback());
        us.setUpdatedAt(Instant.now());
        userSettingsRepository.save(us);

        ApplicationPreference ap = appPrefRepository.findByUserId(userId)
            .orElseGet(() -> new ApplicationPreference(user));
        if (dto.getTheme() != null) ap.setTheme(dto.getTheme());
        if (dto.getWindowOpacity() > 0) ap.setWindowOpacity(dto.getWindowOpacity());
        ap.setAlwaysOnTop(dto.isAlwaysOnTop());
        if (dto.getWindowWidth() > 0) ap.setWindowWidth(dto.getWindowWidth());
        if (dto.getWindowHeight() > 0) ap.setWindowHeight(dto.getWindowHeight());
        if (dto.getWindowX() != 0) ap.setWindowX(dto.getWindowX());
        if (dto.getWindowY() != 0) ap.setWindowY(dto.getWindowY());

        if (dto.getActiveMode() != null) {
            ap.setActiveMode(dto.getActiveMode());
            if ("ONLINE".equalsIgnoreCase(dto.getActiveMode())) {
                aiRouter.setOperatingMode(AiServiceRouter.OperatingMode.ONLINE);
            } else {
                aiRouter.setOperatingMode(AiServiceRouter.OperatingMode.OFFLINE);
            }
        }
        ap.setUpdatedAt(Instant.now());
        appPrefRepository.save(ap);

        ModelPreference mp = modelPrefRepository.findByUserId(userId)
            .orElseGet(() -> new ModelPreference(user));
        if (dto.getOnlineProvider() != null) mp.setOnlineProvider(dto.getOnlineProvider());
        if (dto.getOnlineModelName() != null) mp.setOnlineModelName(dto.getOnlineModelName());
        if (dto.getLocalProvider() != null) mp.setLocalProvider(dto.getLocalProvider());
        if (dto.getLocalModelName() != null) mp.setLocalModelName(dto.getLocalModelName());
        if (dto.getLocalEndpointUrl() != null) mp.setLocalEndpointUrl(dto.getLocalEndpointUrl());
        if (dto.getWhisperModel() != null) mp.setWhisperModel(dto.getWhisperModel());
        mp.setUpdatedAt(Instant.now());
        modelPrefRepository.save(mp);

        return getSettings(userId);
    }
}
