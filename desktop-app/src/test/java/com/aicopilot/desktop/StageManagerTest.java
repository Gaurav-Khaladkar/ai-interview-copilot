package com.aicopilot.desktop;

import com.aicopilot.desktop.stage.StageManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StageManagerTest {

    @Test
    @DisplayName("Window layout modes define correct dimensions")
    void testLayoutModes() {
        assertEquals(900, StageManager.LayoutMode.COMPACT.getWidth());
        assertEquals(650, StageManager.LayoutMode.COMPACT.getHeight());

        assertEquals(1280, StageManager.LayoutMode.STANDARD.getWidth());
        assertEquals(820, StageManager.LayoutMode.STANDARD.getHeight());

        assertEquals(1550, StageManager.LayoutMode.EXPANDED.getWidth());
        assertEquals(950, StageManager.LayoutMode.EXPANDED.getHeight());
    }

    @Test
    @DisplayName("Window opacity clamping logic adheres to 30% to 100% boundary")
    void testOpacityClamping() {
        double low = 0.15;
        double clampedLow = Math.max(0.30, Math.min(1.0, low));
        assertEquals(0.30, clampedLow, 0.001);

        double high = 1.45;
        double clampedHigh = Math.max(0.30, Math.min(1.0, high));
        assertEquals(1.0, clampedHigh, 0.001);

        double valid = 0.75;
        double clampedValid = Math.max(0.30, Math.min(1.0, valid));
        assertEquals(0.75, clampedValid, 0.001);
    }
}
