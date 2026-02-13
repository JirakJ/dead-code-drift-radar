package com.jakubjirak.deadcoderadar.git

import com.intellij.openapi.diagnostic.Logger
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Detects uncommitted code (modified but not staged) that has no references elsewhere.
 * Uses VCS diff information to find locally-only changes that may be dead code.
 */
class GhostCodeDetector(
    private val ignorePatterns: List<String> = DEFAULT_IGNORE_PATTERNS,
) {

    fun isIgnored(filePath: String): Boolean {
        return ignorePatterns.any { pattern ->
            matchesGlobPattern(filePath, pattern)
        }
    }

    fun calculateConfidence(incomingReferences: Int, daysSinceModified: Long): Double {
        val referenceScore = if (incomingReferences == 0) 1.0 else (1.0 / (1 + incomingReferences))
        val ageScore = (daysSinceModified.coerceAtMost(MAX_AGE_DAYS).toDouble() / MAX_AGE_DAYS)
        return ((referenceScore * REFERENCE_WEIGHT) + (ageScore * AGE_WEIGHT)).coerceIn(0.0, 1.0)
    }

    fun createGhostCode(
        filePath: String,
        lineRange: IntRange,
        content: String,
        @Suppress("UNUSED_PARAMETER") lastModified: Instant,
    ): GhostCode {
        val daysSince = ChronoUnit.DAYS.between(lastModified, Instant.now())
        return GhostCode(
            filePath = filePath,
            lineRange = lineRange,
            content = content,
            daysSinceModified = daysSince,
        )
    }

    companion object {
        @Suppress("unused")
        private val LOG = Logger.getInstance(GhostCodeDetector::class.java)

        private const val REFERENCE_WEIGHT = 0.7
        private const val AGE_WEIGHT = 0.3
        private const val MAX_AGE_DAYS = 365L

        val DEFAULT_IGNORE_PATTERNS: List<String> = listOf(
            "build/**",
            "out/**",
            ".gradle/**",
            "*.generated.*",
        )

        fun matchesGlobPattern(path: String, pattern: String): Boolean {
            val regex = pattern
                .replace(".", "\\.")
                .replace("**/", "(.+/)?")
                .replace("**", ".+")
                .replace("*", "[^/]*")
                .let { Regex("^$it$") }
            return regex.matches(path)
        }
    }
}

data class GhostCode(
    val filePath: String,
    val lineRange: IntRange,
    val content: String,
    val daysSinceModified: Long,
)

enum class GitFileStatus {
    UNTRACKED,
    MODIFIED,
    ADDED,
    RENAMED,
}
