package com.jakubjirak.deadcoderadar.analyzer

import com.intellij.openapi.diagnostic.Logger
import com.jakubjirak.deadcoderadar.model.CodeElement
import com.jakubjirak.deadcoderadar.model.DriftScore
import com.jakubjirak.deadcoderadar.model.GitActivityData
import java.time.Duration
import java.time.Instant

/**
 * Combines git activity signals and reference counts into a single drift score (0-100).
 *
 * Scoring factors:
 * - Last modified age: older = higher drift (weight: 0.35)
 * - Modification frequency: less frequent = higher drift (weight: 0.25)
 * - Reference count: fewer references = higher drift (weight: 0.25)
 * - Test coverage: lower coverage = higher drift (weight: 0.15)
 */
class DriftScoreCalculator {

    /**
     * Calculates drift score for a code element.
     *
     * @param element The code element to score
     * @param gitData Git activity data for the element
     * @param referenceCount Number of incoming references
     * @param testCoverage Optional test coverage (0.0-1.0)
     * @return DriftScore with computed score 0-100
     */
    fun calculate(
        element: CodeElement,
        gitData: GitActivityData,
        referenceCount: Int,
        testCoverage: Double? = null,
    ): DriftScore {
        val ageScore = calculateAgeScore(gitData.lastModified)
        val frequencyScore = calculateFrequencyScore(gitData.modificationCount)
        val referenceScore = calculateReferenceScore(referenceCount)
        val coverageScore = calculateCoverageScore(testCoverage)

        val weightedScore = (
            ageScore * WEIGHT_AGE +
                frequencyScore * WEIGHT_FREQUENCY +
                referenceScore * WEIGHT_REFERENCES +
                coverageScore * WEIGHT_COVERAGE
            ).toInt().coerceIn(0, MAX_SCORE)

        LOG.debug(
            "Drift score for ${element.name}: age=$ageScore, freq=$frequencyScore, " +
                "refs=$referenceScore, cov=$coverageScore -> $weightedScore"
        )

        return DriftScore(
            element = element,
            score = weightedScore,
            lastModified = gitData.lastModified,
            modificationCount = gitData.modificationCount,
            referenceCount = referenceCount,
            testCoverage = testCoverage,
        )
    }

    /**
     * Scores how old the last modification is. Older = higher drift.
     * - Modified today: 0
     * - Modified 30 days ago: ~25
     * - Modified 180 days ago: ~60
     * - Modified 365+ days ago: ~90-100
     */
    internal fun calculateAgeScore(lastModified: Instant?): Int {
        if (lastModified == null) return MAX_SCORE

        val daysSinceModification = Duration.between(lastModified, Instant.now()).toDays()
        return when {
            daysSinceModification <= 0 -> 0
            daysSinceModification <= AGE_RECENT_DAYS -> (daysSinceModification * MAX_SCORE / AGE_OLD_DAYS).toInt()
            daysSinceModification <= AGE_OLD_DAYS -> (daysSinceModification * MAX_SCORE / AGE_OLD_DAYS).toInt()
            else -> MAX_SCORE
        }.coerceIn(0, MAX_SCORE)
    }

    /**
     * Scores modification frequency. Less frequent = higher drift.
     * - 10+ modifications: 0
     * - 5 modifications: ~50
     * - 0 modifications: 100
     */
    internal fun calculateFrequencyScore(modificationCount: Int): Int {
        if (modificationCount <= 0) return MAX_SCORE
        if (modificationCount >= FREQ_HIGH_THRESHOLD) return 0

        return (MAX_SCORE - (modificationCount * MAX_SCORE / FREQ_HIGH_THRESHOLD))
            .coerceIn(0, MAX_SCORE)
    }

    /**
     * Scores reference count. Fewer references = higher drift.
     * - 10+ references: 0
     * - 5 references: ~50
     * - 0 references: 100
     */
    internal fun calculateReferenceScore(referenceCount: Int): Int {
        if (referenceCount <= 0) return MAX_SCORE
        if (referenceCount >= REF_HIGH_THRESHOLD) return 0

        return (MAX_SCORE - (referenceCount * MAX_SCORE / REF_HIGH_THRESHOLD))
            .coerceIn(0, MAX_SCORE)
    }

    /**
     * Scores test coverage. Lower coverage = higher drift.
     * If coverage is unavailable, returns a neutral score.
     */
    internal fun calculateCoverageScore(testCoverage: Double?): Int {
        if (testCoverage == null) return NEUTRAL_SCORE
        return (MAX_SCORE - (testCoverage * MAX_SCORE).toInt()).coerceIn(0, MAX_SCORE)
    }

    companion object {
        private val LOG = Logger.getInstance(DriftScoreCalculator::class.java)

        const val MAX_SCORE = 100

        const val WEIGHT_AGE = 0.35
        const val WEIGHT_FREQUENCY = 0.25
        const val WEIGHT_REFERENCES = 0.25
        const val WEIGHT_COVERAGE = 0.15

        const val AGE_RECENT_DAYS = 30L
        const val AGE_OLD_DAYS = 365L

        const val FREQ_HIGH_THRESHOLD = 10
        const val REF_HIGH_THRESHOLD = 10

        const val NEUTRAL_SCORE = 50
    }
}
