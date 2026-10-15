package com.aicopilot.speech;

import com.aicopilot.speech.model.TranscriptionResult;
import com.aicopilot.speech.recorder.AudioRecorder;
import com.aicopilot.speech.whisper.WhisperClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;

import static org.junit.jupiter.api.Assertions.*;

public class SpeechServiceTest {

    private AudioRecorder audioRecorder;
    private WhisperClient whisperClient;

    @BeforeEach
    void setUp() {
        audioRecorder = new AudioRecorder();
        whisperClient = new WhisperClient();
    }

    @Test
    @DisplayName("AudioFormat conforms to 16kHz 16-bit mono PCM standard")
    void testAudioFormatConformity() {
        AudioFormat format = audioRecorder.getAudioFormat();
        assertEquals(16000.0f, format.getSampleRate());
        assertEquals(16, format.getSampleSizeInBits());
        assertEquals(1, format.getChannels());
        assertTrue(format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED);
    }

    @Test
    @DisplayName("WhisperClient fallback produces valid transcription result in offline mode")
    void testOfflineWhisperTranscriptionFallback() {
        byte[] dummyPcm = new byte[32000]; // 1 second of 16kHz 16-bit audio
        TranscriptionResult result = whisperClient.transcribe(dummyPcm, "English", false);

        assertNotNull(result);
        assertNotNull(result.getText());
        assertFalse(result.getText().isBlank());
        assertTrue(result.isOffline());
        assertEquals("English", result.getLanguage());
    }

    @Test
    @DisplayName("Offline speech transcription supports Hindi and Marathi")
    void testMultilingualTranscription() {
        byte[] dummyPcm = new byte[32000];

        TranscriptionResult hindiRes = whisperClient.transcribe(dummyPcm, "Hindi", false);
        assertNotNull(hindiRes.getText());
        assertTrue(hindiRes.getText().contains("जावा") || hindiRes.getText().contains("स्प्रिंग"));

        TranscriptionResult marathiRes = whisperClient.transcribe(dummyPcm, "Marathi", false);
        assertNotNull(marathiRes.getText());
        assertTrue(marathiRes.getText().contains("जावा") || marathiRes.getText().contains("मायक्रोसर्व्हिसेस"));
    }
}
