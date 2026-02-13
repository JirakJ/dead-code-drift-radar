package com.jakubjirak.deadcoderadar.git

import com.intellij.openapi.diagnostic.Logger

/**
 * Compares local vs remote (origin) for tracked files and calculates a divergence score.
 * Helps identify files that have drifted significantly from the remote repository.
 */
class RemoteDriftAnalyzer(
    private val remoteBranch: String = DEFAULT_REMOTE_BRANCH,
) {

    fun calculateDriftScore(localChanges: Int, remoteChanges: Int): Int {
        if (localChanges == 0 && remoteChanges == 0) return 0
        val total = localChanges + remoteChanges
        return (total.coerceAtMost(MAX_CHANGES).toDouble() / MAX_CHANGES * MAX_SCORE).toInt()
    }

    fun determineDriftDirection(localChanges: Int, remoteChanges: Int): DriftDirection {
        return when {
            localChanges == 0 && remoteChanges == 0 -> DriftDirection.SYNCED
            localChanges > 0 && remoteChanges == 0 -> DriftDirection.LOCAL_AHEAD
            localChanges == 0 && remoteChanges > 0 -> DriftDirection.REMOTE_AHEAD
            else -> DriftDirection.DIVERGED
        }
    }

    fun createRemoteDrift(
        filePath: String,
        localChanges: Int,
        remoteChanges: Int,
    ): RemoteDrift {
        return RemoteDrift(
            filePath = filePath,
            localChanges = localChanges,
            remoteChanges = remoteChanges,
            divergenceScore = calculateDriftScore(localChanges, remoteChanges),
            direction = determineDriftDirection(localChanges, remoteChanges),
        )
    }

    fun getRemoteBranch(): String = remoteBranch

    companion object {
        @Suppress("unused")
        private val LOG = Logger.getInstance(RemoteDriftAnalyzer::class.java)

        const val DEFAULT_REMOTE_BRANCH = "origin/main"
        private const val MAX_CHANGES = 100
        private const val MAX_SCORE = 100
    }
}

data class RemoteDrift(
    val filePath: String,
    val localChanges: Int,
    val remoteChanges: Int,
    val divergenceScore: Int,
    val direction: DriftDirection = DriftDirection.SYNCED,
)

enum class DriftDirection {
    LOCAL_AHEAD,
    REMOTE_AHEAD,
    DIVERGED,
    SYNCED,
}
