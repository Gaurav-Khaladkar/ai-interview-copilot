package com.aicopilot.dto;

public class AuthResponse {
    private String userId;
    private String email;
    private String fullName;
    private String token;
    private String role;
    private long expiresInSeconds;

    public AuthResponse() {}

    public AuthResponse(String userId, String email, String fullName, String token, String role, long expiresInSeconds) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
        this.role = role;
        this.expiresInSeconds = expiresInSeconds;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public long getExpiresInSeconds() { return expiresInSeconds; }
    public void setExpiresInSeconds(long expiresInSeconds) { this.expiresInSeconds = expiresInSeconds; }
}
