package com.jakubjirak.deadcoderadar.model

import java.time.Instant

/**
 * Drift score computed for a code element, combining git activity and reference analysis signals.
 *
 * @property element The code element being scored
 * @property score Drift score from 0 (active) to 100 (dead)
 * @property lastModified When the element was last modified in git
 * @property modificationCount Number of modifications in the analysis window
 * @property referenceCount Number of incoming references found
 * @property testCoverage Optional test coverage ratio (0.0 to 1.0)
 */
data class DriftScore(
    val element: CodeElement,
    val score: Int,
    val lastModified: Instant?,
    val modificationCount: Int,
    val referenceCount: Int,
    val testCoverage: Double? = null,
) {
    val level: DriftLevel
        get() = DriftLevel.fromScore(score)

    init {
        require(score in 0..100) { "Drift score must be between 0 and 100, got $score" }
    }
}
