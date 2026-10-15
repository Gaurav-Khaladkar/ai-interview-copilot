package com.aicopilot;

import com.aicopilot.model.ApplicationPreference;
import com.aicopilot.model.ModelPreference;
import com.aicopilot.model.User;
import com.aicopilot.model.UserSettings;
import com.aicopilot.repository.ApplicationPreferenceRepository;
import com.aicopilot.repository.ModelPreferenceRepository;
import com.aicopilot.repository.UserRepository;
import com.aicopilot.repository.UserSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.aicopilot"})
public class AiInterviewCopilotApplication {

    private static final Logger log = LoggerFactory.getLogger(AiInterviewCopilotApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AiInterviewCopilotApplication.class, args);
        log.info("==================================================================");
        log.info("AI Interview Copilot Backend API Started Successfully on port 8080");
        log.info("Swagger OpenAPI Docs: http://localhost:8080/swagger-ui/index.html");
        log.info("==================================================================");
    }

    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository,
                                             UserSettingsRepository settingsRepository,
                                             ApplicationPreferenceRepository appPrefRepository,
                                             ModelPreferenceRepository modelPrefRepository,
                                             PasswordEncoder passwordEncoder) {
        return args -> {
            String demoEmail = "demo@aicopilot.com";
            if (!userRepository.existsByEmail(demoEmail)) {
                log.info("Seeding default demo candidate account: {}", demoEmail);
                User user = new User(demoEmail, passwordEncoder.encode("Password123!"), "Senior Java Candidate");
                user.setId("usr-1000-0000-0000-000000000001");
                user = userRepository.save(user);

                UserSettings settings = new UserSettings(user);
                settings.setId("set-1000-0000-0000-000000000001");
                settings.setTargetDomain("Java Full Stack & Cloud");
                settings.setYearsExperience(4);
                settingsRepository.save(settings);

                ApplicationPreference appPref = new ApplicationPreference(user);
                appPref.setId("pref-1000-0000-0000-000000000001");
                appPref.setTheme("DARK");
                appPref.setWindowOpacity(1.0);
                appPref.setAlwaysOnTop(false);
                appPref.setActiveMode("OFFLINE");
                appPrefRepository.save(appPref);

                ModelPreference modelPref = new ModelPreference(user);
                modelPref.setId("mod-1000-0000-0000-000000000001");
                modelPref.setOnlineProvider("OPENAI");
                modelPref.setOnlineModelName("gpt-4o-mini");
                modelPref.setLocalProvider("OLLAMA");
                modelPref.setLocalModelName("llama3:8b");
                modelPrefRepository.save(modelPref);

                log.info("Demo user initialized successfully.");
            }
        };
    }
}
