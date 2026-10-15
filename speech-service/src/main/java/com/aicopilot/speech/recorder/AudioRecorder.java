package com.aicopilot.speech.recorder;

import com.aicopilot.speech.model.AudioRecordingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class AudioRecorder {

    private static final Logger log = LoggerFactory.getLogger(AudioRecorder.class);

    private static final float SAMPLE_RATE = 16000.0f;
    private static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int CHANNELS = 1; // Mono
    private static final boolean SIGNED = true;
    private static final boolean BIG_ENDIAN = false;

    private TargetDataLine targetLine;
    private final AtomicBoolean isRecording = new AtomicBoolean(false);
    private ByteArrayOutputStream audioBuffer;
    private Thread recordingThread;
    private Consumer<Double> levelMeterListener;

    public AudioFormat getAudioFormat() {
        return new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
    }

    public static boolean isMicrophoneAvailable() {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            return AudioSystem.isLineSupported(info);
        } catch (Exception e) {
            log.warn("Microphone hardware check failed: {}", e.getMessage());
            return false;
        }
    }

    public void setLevelMeterListener(Consumer<Double> listener) {
        this.levelMeterListener = listener;
    }

    public synchronized void startRecording() throws LineUnavailableException {
        if (isRecording.get()) {
            log.warn("Recording is already in progress.");
            return;
        }

        AudioFormat format = getAudioFormat();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone input line is not supported on this Windows audio configuration.");
        }

        targetLine = (TargetDataLine) AudioSystem.getLine(info);
        targetLine.open(format);
        targetLine.start();

        isRecording.set(true);
        audioBuffer = new ByteArrayOutputStream();

        recordingThread = new Thread(() -> {
            byte[] buffer = new byte[2048];
            while (isRecording.get()) {
                int read = targetLine.read(buffer, 0, buffer.length);
                if (read > 0) {
                    audioBuffer.write(buffer, 0, read);

                    // Compute RMS for microphone volume meter (0.0 to 1.0)
                    if (levelMeterListener != null) {
                        double sum = 0;
                        for (int i = 0; i < read - 1; i += 2) {
                            short sample = (short) ((buffer[i + 1] << 8) | (buffer[i] & 0xFF));
                            sum += sample * sample;
                        }
                        double rms = Math.sqrt(sum / (read / 2.0));
                        double normalized = Math.min(1.0, rms / 32768.0);
                        levelMeterListener.accept(normalized);
                    }
                }
            }
        }, "Audio-Recorder-Thread");

        recordingThread.setDaemon(true);
        recordingThread.start();
        log.info("Audio recording started (16kHz Mono 16-bit PCM)");
    }

    public synchronized AudioRecordingResult stopRecording() throws IOException {
        if (!isRecording.get()) {
            return null;
        }

        isRecording.set(false);

        if (targetLine != null) {
            targetLine.stop();
            targetLine.close();
        }

        if (recordingThread != null) {
            try {
                recordingThread.join(2000);
            } catch (InterruptedException ignored) {}
        }

        byte[] rawPcmData = audioBuffer.toByteArray();
        double durationSeconds = (double) rawPcmData.length / (SAMPLE_RATE * (SAMPLE_SIZE_IN_BITS / 8) * CHANNELS);

        // Convert raw PCM bytes to WAV format with proper RIFF header
        ByteArrayInputStream bais = new ByteArrayInputStream(rawPcmData);
        AudioInputStream audioInputStream = new AudioInputStream(bais, getAudioFormat(), rawPcmData.length / (SAMPLE_SIZE_IN_BITS / 8));

        File tempWav = File.createTempFile("interview_audio_", ".wav");
        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, tempWav);
        tempWav.deleteOnExit();

        byte[] wavBytes = java.nio.file.Files.readAllBytes(tempWav.toPath());

        log.info("Recording stopped. Duration: {}s, Size: {} bytes", String.format("%.2f", durationSeconds), wavBytes.length);

        return new AudioRecordingResult(
            wavBytes,
            tempWav,
            durationSeconds,
            SAMPLE_RATE,
            CHANNELS,
            SAMPLE_SIZE_IN_BITS
        );
    }

    public boolean isRecording() {
        return isRecording.get();
    }
}
