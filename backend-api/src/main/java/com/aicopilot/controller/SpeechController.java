package com.aicopilot.controller;

import com.aicopilot.ai.service.AiServiceRouter;
import com.aicopilot.dto.ApiResponse;
import com.aicopilot.speech.model.TranscriptionResult;
import com.aicopilot.speech.recorder.AudioRecorder;
import com.aicopilot.speech.tts.TextToSpeechService;
import com.aicopilot.speech.whisper.WhisperClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/speech")
@Tag(name = "Speech & Voice", description = "Whisper audio transcription, microphone diagnostics, and speech synthesis")
public class SpeechController {

    private final WhisperClient whisperClient = new WhisperClient();
    private final TextToSpeechService ttsService = new TextToSpeechService();
    private final AiServiceRouter aiRouter;

    public SpeechController(AiServiceRouter aiRouter) {
        this.aiRouter = aiRouter;
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Transcribe recorded WAV audio via Whisper or offline speech engine")
    public ResponseEntity<ApiResponse<TranscriptionResult>> transcribe(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam(value = "language", defaultValue = "English") String language) throws IOException {

        boolean isOnline = aiRouter.getOperatingMode() == AiServiceRouter.OperatingMode.ONLINE;
        byte[] bytes = audioFile.getBytes();

        TranscriptionResult result = whisperClient.transcribe(bytes, language, isOnline);
        return ResponseEntity.ok(ApiResponse.ok("Audio transcribed successfully.", result));
    }

    @PostMapping("/synthesize")
    @Operation(summary = "Play speech audio aloud for answers or questions")
    public ResponseEntity<ApiResponse<String>> synthesize(@RequestBody Map<String, String> payload) {
        String text = payload.get("text");
        if (text != null && !text.isBlank()) {
            ttsService.speakAsync(text);
            return ResponseEntity.ok(ApiResponse.ok("Speech synthesis initiated.", null));
        }
        return ResponseEntity.badRequest().body(ApiResponse.error("Text parameter is required."));
    }

    @GetMapping("/diagnostics")
    @Operation(summary = "Check host microphone hardware availability")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkMicrophone() {
        boolean available = AudioRecorder.isMicrophoneAvailable();
        return ResponseEntity.ok(ApiResponse.ok(Map.of(
            "microphoneAvailable", available,
            "status", available ? "READY" : "NO_RECORDING_LINE_FOUND"
        )));
    }
}
