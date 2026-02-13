package com.jakubjirak.deadcoderadar.model

/**
 * Drift level classification based on the drift score.
 * - ACTIVE: Code is healthy and actively maintained (score 0-25)
 * - COOLING: Code is showing early signs of reduced activity (score 26-50)
 * - DRIFTING: Code is significantly drifting toward dead code (score 51-75)
 * - DEAD: Code appears to be dead or abandoned (score 76-100)
 */
enum class DriftLevel(val label: String) {
    ACTIVE("Active"),
    COOLING("Cooling"),
    DRIFTING("Drifting"),
    DEAD("Dead");

    companion object {
        fun fromScore(score: Int, thresholds: DriftThresholds = DriftThresholds()): DriftLevel {
            return when {
                score <= thresholds.activeBelow -> ACTIVE
                score <= thresholds.coolingBelow -> COOLING
                score <= thresholds.driftingBelow -> DRIFTING
                else -> DEAD
            }
        }
    }
}

/**
 * Configurable thresholds for drift level classification.
 */
data class DriftThresholds(
    val activeBelow: Int = 25,
    val coolingBelow: Int = 50,
    val driftingBelow: Int = 75,
)
