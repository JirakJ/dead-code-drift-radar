package com.jakubjirak.deadcoderadar

import com.jakubjirak.deadcoderadar.model.DriftLevel
import com.jakubjirak.deadcoderadar.model.DriftThresholds
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DriftLevelTest {

    @Test
    fun testDefaultThresholds() {
        assertEquals(DriftLevel.ACTIVE, DriftLevel.fromScore(0))
        assertEquals(DriftLevel.ACTIVE, DriftLevel.fromScore(25))
        assertEquals(DriftLevel.COOLING, DriftLevel.fromScore(26))
        assertEquals(DriftLevel.COOLING, DriftLevel.fromScore(50))
        assertEquals(DriftLevel.DRIFTING, DriftLevel.fromScore(51))
        assertEquals(DriftLevel.DRIFTING, DriftLevel.fromScore(75))
        assertEquals(DriftLevel.DEAD, DriftLevel.fromScore(76))
        assertEquals(DriftLevel.DEAD, DriftLevel.fromScore(100))
    }

    @Test
    fun testCustomThresholds() {
        val thresholds = DriftThresholds(activeBelow = 30, coolingBelow = 60, driftingBelow = 80)

        assertEquals(DriftLevel.ACTIVE, DriftLevel.fromScore(30, thresholds))
        assertEquals(DriftLevel.COOLING, DriftLevel.fromScore(31, thresholds))
        assertEquals(DriftLevel.COOLING, DriftLevel.fromScore(60, thresholds))
        assertEquals(DriftLevel.DRIFTING, DriftLevel.fromScore(61, thresholds))
        assertEquals(DriftLevel.DRIFTING, DriftLevel.fromScore(80, thresholds))
        assertEquals(DriftLevel.DEAD, DriftLevel.fromScore(81, thresholds))
    }

    @Test
    fun testLevelLabels() {
        assertEquals("Active", DriftLevel.ACTIVE.label)
        assertEquals("Cooling", DriftLevel.COOLING.label)
        assertEquals("Drifting", DriftLevel.DRIFTING.label)
        assertEquals("Dead", DriftLevel.DEAD.label)
    }
}
