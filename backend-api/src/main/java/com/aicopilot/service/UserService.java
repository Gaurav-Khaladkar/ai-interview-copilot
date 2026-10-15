package com.aicopilot.service;

import com.aicopilot.dto.AuthRequest;
import com.aicopilot.dto.AuthResponse;
import com.aicopilot.dto.RegisterRequest;
import com.aicopilot.model.ApplicationPreference;
import com.aicopilot.model.ModelPreference;
import com.aicopilot.model.User;
import com.aicopilot.model.UserSettings;
import com.aicopilot.repository.ApplicationPreferenceRepository;
import com.aicopilot.repository.ModelPreferenceRepository;
import com.aicopilot.repository.UserRepository;
import com.aicopilot.repository.UserSettingsRepository;
import com.aicopilot.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSettingsRepository settingsRepository;
    private final ApplicationPreferenceRepository appPrefRepository;
    private final ModelPreferenceRepository modelPrefRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public UserService(UserRepository userRepository,
                       UserSettingsRepository settingsRepository,
                       ApplicationPreferenceRepository appPrefRepository,
                       ModelPreferenceRepository modelPrefRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
        this.appPrefRepository = appPrefRepository;
        this.modelPrefRepository = modelPrefRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("An account with email " + request.getEmail() + " already exists.");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getEmail(), hashedPassword, request.getFullName());
        user = userRepository.save(user);

        // Initialize User Settings
        UserSettings settings = new UserSettings(user);
        settings.setTargetDomain(request.getTargetDomain());
        settings.setYearsExperience(request.getYearsExperience());
        settings.setPreferredLanguage(request.getPreferredLanguage());
        settingsRepository.save(settings);

        // Initialize App Preferences
        ApplicationPreference appPref = new ApplicationPreference(user);
        appPrefRepository.save(appPref);

        // Initialize Model Preferences
        ModelPreference modelPref = new ModelPreference(user);
        modelPrefRepository.save(modelPref);

        String token = tokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(user.getId(), user.getEmail(), user.getFullName(), token, user.getRole(), 86400);
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password.");
        }

        String token = tokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(user.getId(), user.getEmail(), user.getFullName(), token, user.getRole(), 86400);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
