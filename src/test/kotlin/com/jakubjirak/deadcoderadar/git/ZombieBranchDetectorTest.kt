package com.jakubjirak.deadcoderadar.git

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class ZombieBranchDetectorTest {

    private val detector = ZombieBranchDetector()

    @Test
    fun `recent branch is classified as ACTIVE`() {
        val lastCommit = Instant.now().minus(5, ChronoUnit.DAYS)
        val status = detector.classify(lastCommit)
        assertEquals(ZombieBranchStatus.ACTIVE, status)
    }

    @Test
    fun `branch at 29 days is still ACTIVE`() {
        val lastCommit = Instant.now().minus(29, ChronoUnit.DAYS)
        val status = detector.classify(lastCommit)
        assertEquals(ZombieBranchStatus.ACTIVE, status)
    }

    @Test
    fun `branch at 31 days is STALE`() {
        val lastCommit = Instant.now().minus(31, ChronoUnit.DAYS)
        val status = detector.classify(lastCommit)
        assertEquals(ZombieBranchStatus.STALE, status)
    }

    @Test
    fun `branch at 60 days is STALE`() {
        val lastCommit = Instant.now().minus(60, ChronoUnit.DAYS)
        val status = detector.classify(lastCommit)
        assertEquals(ZombieBranchStatus.STALE, status)
    }

    @Test
    fun `branch at 91 days is ZOMBIE`() {
        val lastCommit = Instant.now().minus(91, ChronoUnit.DAYS)
        val status = detector.classify(lastCommit)
        assertEquals(ZombieBranchStatus.ZOMBIE, status)
    }

    @Test
    fun `branch at 365 days is ZOMBIE`() {
        val lastCommit = Instant.now().minus(365, ChronoUnit.DAYS)
        val status = detector.classify(lastCommit)
        assertEquals(ZombieBranchStatus.ZOMBIE, status)
    }

    @Test
    fun `days since activity calculation`() {
        val tenDaysAgo = Instant.now().minus(10, ChronoUnit.DAYS)
        val days = detector.daysSinceActivity(tenDaysAgo)
        assertEquals(10, days)
    }

    @Test
    fun `createZombieBranch sets correct status`() {
        val lastCommit = Instant.now().minus(100, ChronoUnit.DAYS)
        val branch = detector.createZombieBranch("old-feature", lastCommit, false)

        assertEquals("old-feature", branch.name)
        assertEquals(ZombieBranchStatus.ZOMBIE, branch.status)
        assertFalse(branch.hasUncommitted)
        assertEquals(100, branch.daysSinceActivity)
    }

    @Test
    fun `filterZombies returns only zombie branches`() {
        val branches = listOf(
            detector.createZombieBranch("active", Instant.now().minus(5, ChronoUnit.DAYS), false),
            detector.createZombieBranch("stale", Instant.now().minus(50, ChronoUnit.DAYS), false),
            detector.createZombieBranch("zombie", Instant.now().minus(100, ChronoUnit.DAYS), true),
        )

        val zombies = detector.filterZombies(branches)
        assertEquals(1, zombies.size)
        assertEquals("zombie", zombies[0].name)
    }

    @Test
    fun `filterStaleOrWorse returns stale and zombie branches`() {
        val branches = listOf(
            detector.createZombieBranch("active", Instant.now().minus(5, ChronoUnit.DAYS), false),
            detector.createZombieBranch("stale", Instant.now().minus(50, ChronoUnit.DAYS), false),
            detector.createZombieBranch("zombie", Instant.now().minus(100, ChronoUnit.DAYS), true),
        )

        val staleOrWorse = detector.filterStaleOrWorse(branches)
        assertEquals(2, staleOrWorse.size)
        assertTrue(staleOrWorse.any { it.name == "stale" })
        assertTrue(staleOrWorse.any { it.name == "zombie" })
    }

    @Test
    fun `custom thresholds are respected`() {
        val customDetector = ZombieBranchDetector(
            staleThresholdDays = 10,
            zombieThresholdDays = 30,
        )

        val at15Days = Instant.now().minus(15, ChronoUnit.DAYS)
        assertEquals(ZombieBranchStatus.STALE, customDetector.classify(at15Days))

        val at35Days = Instant.now().minus(35, ChronoUnit.DAYS)
        assertEquals(ZombieBranchStatus.ZOMBIE, customDetector.classify(at35Days))
    }

    @Test
    fun `branch with uncommitted changes is tracked`() {
        val lastCommit = Instant.now().minus(100, ChronoUnit.DAYS)
        val branch = detector.createZombieBranch("wip-branch", lastCommit, true)

        assertTrue(branch.hasUncommitted)
        assertEquals(ZombieBranchStatus.ZOMBIE, branch.status)
    }
}
