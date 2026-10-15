package com.aicopilot.speech.tts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextToSpeechService {

    private static final Logger log = LoggerFactory.getLogger(TextToSpeechService.class);

    private final ExecutorService executor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "TTS-Speech-Thread");
        t.setDaemon(true);
        return t;
    });

    public void speakAsync(String text) {
        if (text == null || text.isBlank()) return;

        executor.submit(() -> {
            try {
                // Sanitize string to prevent command injection
                String sanitized = text.replaceAll("[\"'`$;|&]", " ");
                if (sanitized.length() > 300) {
                    sanitized = sanitized.substring(0, 300) + "...";
                }

                // Use Windows SAPI speech synthesis via powershell non-blocking
                String psCommand = String.format(
                    "Add-Type -AssemblyName System.Speech; $s = New-Object System.Speech.Synthesis.SpeechSynthesizer; $s.Rate = 0; $s.Speak('%s');",
                    sanitized
                );

                ProcessBuilder pb = new ProcessBuilder("powershell", "-NoProfile", "-NonInteractive", "-Command", psCommand);
                Process p = pb.start();
                p.waitFor();
            } catch (Exception e) {
                log.warn("Offline TTS playback failed: {}", e.getMessage());
            }
        });
    }

    public void stop() {
        executor.shutdownNow();
    }
}
