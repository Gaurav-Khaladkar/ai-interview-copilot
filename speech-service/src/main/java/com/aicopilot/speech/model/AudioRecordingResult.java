package com.aicopilot.speech.model;

import java.io.File;

public class AudioRecordingResult {
    private byte[] audioBytes;
    private File wavFile;
    private double durationSeconds;
    private float sampleRate;
    private int channels;
    private int bitsPerSample;

    public AudioRecordingResult() {}

    public AudioRecordingResult(byte[] audioBytes, File wavFile, double durationSeconds, float sampleRate, int channels, int bitsPerSample) {
        this.audioBytes = audioBytes;
        this.wavFile = wavFile;
        this.durationSeconds = durationSeconds;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitsPerSample = bitsPerSample;
    }

    public byte[] getAudioBytes() { return audioBytes; }
    public void setAudioBytes(byte[] audioBytes) { this.audioBytes = audioBytes; }

    public File getWavFile() { return wavFile; }
    public void setWavFile(File wavFile) { this.wavFile = wavFile; }

    public double getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(double durationSeconds) { this.durationSeconds = durationSeconds; }

    public float getSampleRate() { return sampleRate; }
    public void setSampleRate(float sampleRate) { this.sampleRate = sampleRate; }

    public int getChannels() { return channels; }
    public void setChannels(int channels) { this.channels = channels; }

    public int getBitsPerSample() { return bitsPerSample; }
    public void setBitsPerSample(int bitsPerSample) { this.bitsPerSample = bitsPerSample; }
}
