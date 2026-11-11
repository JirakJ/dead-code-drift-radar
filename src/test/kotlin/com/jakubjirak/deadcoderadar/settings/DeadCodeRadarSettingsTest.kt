package com.jakubjirak.deadcoderadar.settings

import com.jakubjirak.deadcoderadar.model.DriftThresholds
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DeadCodeRadarSettingsTest {

    @Test
    fun `defaults match the documented thresholds and ignore nothing`() {
        val settings = DeadCodeRadarSettings()
        assertEquals(DriftThresholds(), settings.thresholds)
        assertTrue(settings.ignoredPaths.isEmpty())
    }

    @Test
    fun `ignored paths accept newlines and commas`() {
        val settings = DeadCodeRadarSettings()
        settings.ignoredPathsText = "build/, out/\n\n  **/generated/**  \n,"
        assertEquals(listOf("build/", "out/", "**/generated/**"), settings.ignoredPaths)
    }

    @Test
    fun `thresholds are taken from settings and sorted when mis-ordered`() {
        val settings = DeadCodeRadarSettings()
        settings.activeThreshold = 60
        settings.coolingThreshold = 20
        settings.driftingThreshold = 90
        assertEquals(DriftThresholds(activeBelow = 20, coolingBelow = 60, driftingBelow = 90), settings.thresholds)
    }

    @Test
    fun `state round-trips through loadState`() {
        val settings = DeadCodeRadarSettings()
        settings.loadState(DeadCodeRadarSettings.State(activeThreshold = 10, ignoredPathsText = "x"))
        assertEquals(10, settings.activeThreshold)
        assertEquals("x", settings.getState().ignoredPathsText)
    }
}
