package com.aicopilot.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "model_preferences")
public class ModelPreference {

    @Id
    @Column(length = 36)
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "online_provider", length = 50)
    private String onlineProvider = "OPENAI";

    @Column(name = "online_model_name", length = 100)
    private String onlineModelName = "gpt-4o-mini";

    @Column(name = "online_api_key_vault_ref")
    private String onlineApiKeyVaultRef;

    @Column(name = "local_provider", length = 50)
    private String localProvider = "OLLAMA";

    @Column(name = "local_model_name", length = 100)
    private String localModelName = "llama3:8b";

    @Column(name = "local_endpoint_url")
    private String localEndpointUrl = "http://localhost:11434";

    @Column(name = "whisper_model", length = 50)
    private String whisperModel = "whisper-base";

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "is_synced", nullable = false)
    private boolean synced = false;

    public ModelPreference() {
        this.id = UUID.randomUUID().toString();
    }

    public ModelPreference(User user) {
        this();
        this.user = user;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getOnlineProvider() { return onlineProvider; }
    public void setOnlineProvider(String onlineProvider) { this.onlineProvider = onlineProvider; }

    public String getOnlineModelName() { return onlineModelName; }
    public void setOnlineModelName(String onlineModelName) { this.onlineModelName = onlineModelName; }

    public String getOnlineApiKeyVaultRef() { return onlineApiKeyVaultRef; }
    public void setOnlineApiKeyVaultRef(String onlineApiKeyVaultRef) { this.onlineApiKeyVaultRef = onlineApiKeyVaultRef; }

    public String getLocalProvider() { return localProvider; }
    public void setLocalProvider(String localProvider) { this.localProvider = localProvider; }

    public String getLocalModelName() { return localModelName; }
    public void setLocalModelName(String localModelName) { this.localModelName = localModelName; }

    public String getLocalEndpointUrl() { return localEndpointUrl; }
    public void setLocalEndpointUrl(String localEndpointUrl) { this.localEndpointUrl = localEndpointUrl; }

    public String getWhisperModel() { return whisperModel; }
    public void setWhisperModel(String whisperModel) { this.whisperModel = whisperModel; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public boolean isSynced() { return synced; }
    public void setSynced(boolean synced) { this.synced = synced; }
}
