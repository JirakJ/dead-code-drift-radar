package com.jakubjirak.deadcoderadar.git

import com.intellij.openapi.diagnostic.Logger
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * Detects stale ("zombie") branches that have not received commits for a configurable period.
 * Branches older than the zombie threshold are flagged for cleanup.
 */
class ZombieBranchDetector(
    private val staleThresholdDays: Long = DEFAULT_STALE_THRESHOLD_DAYS,
    private val zombieThresholdDays: Long = DEFAULT_ZOMBIE_THRESHOLD_DAYS,
) {

    fun classify(lastCommitDate: Instant): ZombieBranchStatus {
        val daysSinceActivity = ChronoUnit.DAYS.between(lastCommitDate, Instant.now())
        return when {
            daysSinceActivity > zombieThresholdDays -> ZombieBranchStatus.ZOMBIE
            daysSinceActivity > staleThresholdDays -> ZombieBranchStatus.STALE
            else -> ZombieBranchStatus.ACTIVE
        }
    }

    fun daysSinceActivity(lastCommitDate: Instant): Long {
        return ChronoUnit.DAYS.between(lastCommitDate, Instant.now())
    }

    fun createZombieBranch(
        name: String,
        lastCommitDate: Instant,
        hasUncommitted: Boolean,
    ): ZombieBranch {
        val days = daysSinceActivity(lastCommitDate)
        return ZombieBranch(
            name = name,
            lastCommitDate = lastCommitDate,
            daysSinceActivity = days,
            hasUncommitted = hasUncommitted,
            status = classify(lastCommitDate),
        )
    }

    fun filterZombies(branches: List<ZombieBranch>): List<ZombieBranch> {
        return branches.filter { it.status == ZombieBranchStatus.ZOMBIE }
    }

    fun filterStaleOrWorse(branches: List<ZombieBranch>): List<ZombieBranch> {
        return branches.filter { it.status != ZombieBranchStatus.ACTIVE }
    }

    companion object {
        @Suppress("unused")
        private val LOG = Logger.getInstance(ZombieBranchDetector::class.java)

        const val DEFAULT_STALE_THRESHOLD_DAYS = 30L
        const val DEFAULT_ZOMBIE_THRESHOLD_DAYS = 90L
    }
}

data class ZombieBranch(
    val name: String,
    val lastCommitDate: Instant,
    val daysSinceActivity: Long,
    val hasUncommitted: Boolean,
    val status: ZombieBranchStatus = ZombieBranchStatus.ACTIVE,
)

enum class ZombieBranchStatus {
    ACTIVE,
    STALE,
    ZOMBIE,
}
