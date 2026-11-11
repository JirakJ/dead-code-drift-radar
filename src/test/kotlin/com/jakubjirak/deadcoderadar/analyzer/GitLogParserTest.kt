package com.jakubjirak.deadcoderadar.analyzer

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.Instant

class GitLogParserTest {

    @Test
    fun `blank output is empty activity`() {
        val data = GitActivityAnalyzer.parseGitLogOutput("  \n\n")
        assertNull(data.lastModified)
        assertEquals(0, data.modificationCount)
        assertEquals(0, data.uniqueAuthors)
    }

    @Test
    fun `parses dates with offsets, counts commits and distinct authors`() {
        val out = """
            2024-03-01T10:00:00+01:00
            alice@example.com
            2024-01-15T08:30:00Z
            Bob@Example.com
            2023-12-01T12:00:00-05:00
            bob@example.com
        """.trimIndent()
        val data = GitActivityAnalyzer.parseGitLogOutput(out)
        assertEquals(Instant.parse("2024-03-01T09:00:00Z"), data.lastModified)
        assertEquals(3, data.modificationCount)
        assertEquals(2, data.uniqueAuthors)
    }

    @Test
    fun `entries with unparseable dates are skipped`() {
        val out = "fatal: not a git repository\nsomething\n2024-01-01T00:00:00Z\ncarol@example.com\n"
        val data = GitActivityAnalyzer.parseGitLogOutput(out)
        assertEquals(1, data.modificationCount)
        assertEquals(1, data.uniqueAuthors)
    }

    @Test
    fun `trailing date without author still counts`() {
        val data = GitActivityAnalyzer.parseGitLogOutput("2024-01-01T00:00:00Z\n")
        assertEquals(1, data.modificationCount)
        assertEquals(0, data.uniqueAuthors)
    }
}
