package com.jakubjirak.deadcoderadar.analyzer

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jakubjirak.deadcoderadar.model.GitActivityData
import java.io.File
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.concurrent.TimeUnit

/**
 * Analyzes git history to determine modification frequency and last modified date of a file.
 * Runs `git log --follow` in the project root.
 */
class GitActivityAnalyzer(private val project: Project) {

    /**
     * Analyzes git activity for a file. Returns [GitActivityData.empty] when the project is not a git
     * repository, git is unavailable, or the file has no history.
     */
    fun analyzeFile(file: VirtualFile): GitActivityData {
        val basePath = project.basePath ?: return GitActivityData.empty()
        val relativePath = file.path.removePrefix("$basePath/")
        return try {
            executeGitLog(basePath, relativePath)?.let(::parseGitLogOutput) ?: GitActivityData.empty()
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            LOG.warn("Failed to analyze git activity for ${file.path}", e)
            GitActivityData.empty()
        }
    }

    private fun executeGitLog(basePath: String, filePath: String): String? {
        val process = ProcessBuilder("git", "log", "--format=%aI%n%ae", "--follow", "--", filePath)
            .directory(File(basePath))
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start()

        val output = process.inputStream.bufferedReader().use { it.readText() }
        if (!process.waitFor(GIT_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            process.destroyForcibly()
            return null
        }
        return if (process.exitValue() == 0) output else null
    }

    companion object {
        private val LOG = Logger.getInstance(GitActivityAnalyzer::class.java)
        private const val GIT_TIMEOUT_SECONDS = 30L

        /**
         * Parses `git log --format=%aI%n%ae` output: alternating ISO-8601 author date and author e-mail lines.
         * Entries with an unparseable date are skipped entirely.
         */
        internal fun parseGitLogOutput(output: String): GitActivityData {
            val lines = output.lines().map { it.trim() }.filter { it.isNotEmpty() }
            if (lines.isEmpty()) return GitActivityData.empty()

            val dates = mutableListOf<Instant>()
            val authors = mutableSetOf<String>()

            for (entry in lines.chunked(2)) {
                val date = parseInstant(entry[0]) ?: continue
                dates += date
                entry.getOrNull(1)?.let { authors += it.lowercase() }
            }

            return GitActivityData(
                lastModified = dates.maxOrNull(),
                modificationCount = dates.size,
                uniqueAuthors = authors.size,
            )
        }

        private fun parseInstant(raw: String): Instant? = try {
            java.time.OffsetDateTime.parse(raw).toInstant()
        } catch (e: DateTimeParseException) {
            LOG.debug("Could not parse date: $raw", e)
            null
        }
    }
}
