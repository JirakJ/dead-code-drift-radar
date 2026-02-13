package com.jakubjirak.deadcoderadar

import com.jakubjirak.deadcoderadar.analyzer.DriftScoreCalculator
import com.jakubjirak.deadcoderadar.model.CodeElement
import com.jakubjirak.deadcoderadar.model.DriftLevel
import com.jakubjirak.deadcoderadar.model.ElementType
import com.jakubjirak.deadcoderadar.model.GitActivityData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

class DriftScoreCalculatorTest {

    private lateinit var calculator: DriftScoreCalculator

    private val testElement = CodeElement(
        filePath = "src/main/kotlin/Test.kt",
        name = "TestClass",
        type = ElementType.CLASS,
        lineStart = 1,
        lineEnd = 50,
    )

    @BeforeEach
    fun setUp() {
        calculator = DriftScoreCalculator()
    }

    @Test
    fun testActiveCode() {
        val gitData = GitActivityData(
            lastModified = Instant.now().minus(2, ChronoUnit.DAYS),
            modificationCount = 15,
            uniqueAuthors = 3,
        )

        val result = calculator.calculate(testElement, gitData, referenceCount = 12)

        assertTrue(result.score <= 25, "Active code should have low drift score, got ${result.score}")
        assertEquals(DriftLevel.ACTIVE, result.level)
    }

    @Test
    fun testAbandonedCode() {
        val gitData = GitActivityData(
            lastModified = Instant.now().minus(500, ChronoUnit.DAYS),
            modificationCount = 1,
            uniqueAuthors = 1,
        )

        val result = calculator.calculate(testElement, gitData, referenceCount = 0)

        assertTrue(result.score >= 76, "Abandoned code should have high drift score, got ${result.score}")
        assertEquals(DriftLevel.DEAD, result.level)
    }

    @Test
    fun testDriftingCode() {
        val gitData = GitActivityData(
            lastModified = Instant.now().minus(200, ChronoUnit.DAYS),
            modificationCount = 3,
            uniqueAuthors = 1,
        )

        val result = calculator.calculate(testElement, gitData, referenceCount = 3)

        assertTrue(result.score in 26..75, "Drifting code should have medium drift score, got ${result.score}")
    }

    @Test
    fun testNoGitHistory() {
        val gitData = GitActivityData.empty()

        val result = calculator.calculate(testElement, gitData, referenceCount = 0)

        assertTrue(result.score >= 76, "Code without git history should have high drift score, got ${result.score}")
    }

    @Test
    fun testWithTestCoverage() {
        val gitData = GitActivityData(
            lastModified = Instant.now().minus(100, ChronoUnit.DAYS),
            modificationCount = 5,
            uniqueAuthors = 2,
        )

        val withCoverage = calculator.calculate(testElement, gitData, referenceCount = 5, testCoverage = 0.9)
        val withoutCoverage = calculator.calculate(testElement, gitData, referenceCount = 5, testCoverage = null)

        assertTrue(
            withCoverage.score <= withoutCoverage.score,
            "High coverage should reduce drift score: with=${withCoverage.score}, without=${withoutCoverage.score}",
        )
    }

    @Test
    fun testScoreBounds() {
        val gitData = GitActivityData(
            lastModified = Instant.now(),
            modificationCount = 100,
            uniqueAuthors = 10,
        )
        val result = calculator.calculate(testElement, gitData, referenceCount = 100, testCoverage = 1.0)

        assertTrue(result.score in 0..100, "Score must be within 0-100 bounds, got ${result.score}")
    }

    @Test
    fun testAgeScoreRecentModification() {
        val score = calculator.calculateAgeScore(Instant.now())
        assertEquals(0, score, "Just-modified code should have age score of 0")
    }

    @Test
    fun testAgeScoreOldModification() {
        val score = calculator.calculateAgeScore(Instant.now().minus(400, ChronoUnit.DAYS))
        assertEquals(100, score, "Very old code should have age score of 100")
    }

    @Test
    fun testAgeScoreNullModification() {
        val score = calculator.calculateAgeScore(null)
        assertEquals(100, score, "Unknown modification date should have max age score")
    }

    @Test
    fun testFrequencyScoreHighActivity() {
        val score = calculator.calculateFrequencyScore(15)
        assertEquals(0, score, "High activity should have frequency score of 0")
    }

    @Test
    fun testFrequencyScoreNoActivity() {
        val score = calculator.calculateFrequencyScore(0)
        assertEquals(100, score, "No activity should have frequency score of 100")
    }

    @Test
    fun testReferenceScoreHighReferences() {
        val score = calculator.calculateReferenceScore(15)
        assertEquals(0, score, "Highly referenced code should have reference score of 0")
    }

    @Test
    fun testReferenceScoreNoReferences() {
        val score = calculator.calculateReferenceScore(0)
        assertEquals(100, score, "Unreferenced code should have reference score of 100")
    }

    @Test
    fun testCoverageScoreFullCoverage() {
        val score = calculator.calculateCoverageScore(1.0)
        assertEquals(0, score, "Full coverage should have coverage score of 0")
    }

    @Test
    fun testCoverageScoreNoCoverage() {
        val score = calculator.calculateCoverageScore(0.0)
        assertEquals(100, score, "No coverage should have coverage score of 100")
    }

    @Test
    fun testCoverageScoreUnknown() {
        val score = calculator.calculateCoverageScore(null)
        assertEquals(50, score, "Unknown coverage should have neutral score of 50")
    }

    @Test
    fun testElementMetadataPreserved() {
        val gitData = GitActivityData(
            lastModified = Instant.now(),
            modificationCount = 5,
            uniqueAuthors = 2,
        )

        val result = calculator.calculate(testElement, gitData, referenceCount = 3, testCoverage = 0.5)

        assertEquals(testElement, result.element)
        assertEquals(5, result.modificationCount)
        assertEquals(3, result.referenceCount)
        assertEquals(0.5, result.testCoverage)
    }
}
