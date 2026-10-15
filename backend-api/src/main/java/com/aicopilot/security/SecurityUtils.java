package com.aicopilot.security;

import com.aicopilot.model.User;
import com.aicopilot.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("No authenticated candidate in security context.");
        }
        String principal = auth.getName();
        return userRepository.findByEmail(principal)
            .map(User::getId)
            .orElse(principal);
    }
}
