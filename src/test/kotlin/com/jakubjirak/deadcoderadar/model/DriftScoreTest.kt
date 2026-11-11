package com.jakubjirak.deadcoderadar.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class DriftScoreTest {

    private val element = CodeElement("src/A.kt", "A", ElementType.CLASS, 1, 0)

    private fun score(value: Int, thresholds: DriftThresholds = DriftThresholds()) =
        DriftScore(element, value, null, 0, 0, thresholds = thresholds)

    @Test
    fun `score outside 0-100 is rejected`() {
        assertThrows(IllegalArgumentException::class.java) { score(-1) }
        assertThrows(IllegalArgumentException::class.java) { score(101) }
    }

    @Test
    fun `level honours the thresholds the score was computed with`() {
        assertEquals(DriftLevel.COOLING, score(40).level)
        assertEquals(DriftLevel.ACTIVE, score(40, DriftThresholds(activeBelow = 45, coolingBelow = 60, driftingBelow = 80)).level)
    }

    @Test
    fun `qualified id combines path, name and type`() {
        assertEquals("src/A.kt:A:CLASS", element.qualifiedId)
    }

    @Test
    fun `empty git activity has no history`() {
        val empty = GitActivityData.empty()
        assertEquals(null, empty.lastModified)
        assertEquals(0, empty.modificationCount)
    }
}
