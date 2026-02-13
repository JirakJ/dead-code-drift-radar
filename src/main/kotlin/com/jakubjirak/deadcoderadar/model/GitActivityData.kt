package com.jakubjirak.deadcoderadar.model

import java.time.Instant

/**
 * Git activity data for a code element, extracted from git log.
 */
data class GitActivityData(
    val lastModified: Instant?,
    val modificationCount: Int,
    val uniqueAuthors: Int,
) {
    companion object {
        fun empty(): GitActivityData = GitActivityData(
            lastModified = null,
            modificationCount = 0,
            uniqueAuthors = 0,
        )
    }
}
