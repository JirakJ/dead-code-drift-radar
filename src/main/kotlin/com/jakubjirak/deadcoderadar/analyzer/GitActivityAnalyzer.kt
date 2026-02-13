package com.jakubjirak.deadcoderadar.analyzer

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jakubjirak.deadcoderadar.model.CodeElement
import com.jakubjirak.deadcoderadar.model.GitActivityData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.Instant

/**
 * Analyzes git history to determine modification frequency and last modified date for code elements.
 * Uses git log commands to extract commit data for files.
 */
class GitActivityAnalyzer(private val project: Project) {

    /**
     * Analyzes git activity for a given code element by examining the file's git log.
     */
    fun analyze(element: CodeElement): GitActivityData {
        val basePath = project.basePath ?: return GitActivityData.empty()
        return try {
            val result = executeGitLog(basePath, element.filePath)
            parseGitLogOutput(result)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            LOG.warn("Failed to analyze git activity for ${element.filePath}", e)
            GitActivityData.empty()
        }
    }

    /**
     * Analyzes git activity for all elements in a file.
     */
    fun analyzeFile(file: VirtualFile): GitActivityData {
        val basePath = project.basePath ?: return GitActivityData.empty()
        val relativePath = file.path.removePrefix("$basePath/")
        return try {
            val result = executeGitLog(basePath, relativePath)
            parseGitLogOutput(result)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            LOG.warn("Failed to analyze git activity for ${file.path}", e)
            GitActivityData.empty()
        }
    }

    private fun executeGitLog(basePath: String, filePath: String): String {
        val process = ProcessBuilder(
            "git", "log", "--format=%aI%n%ae", "--follow", "--", filePath
        )
            .directory(java.io.File(basePath))
            .redirectErrorStream(true)
            .start()

        val output = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
        process.waitFor()
        return output
    }

    private fun parseGitLogOutput(output: String): GitActivityData {
        if (output.isBlank()) return GitActivityData.empty()

        val lines = output.trim().lines().filter { it.isNotBlank() }
        if (lines.isEmpty()) return GitActivityData.empty()

        val dates = mutableListOf<Instant>()
        val authors = mutableSetOf<String>()

        var i = 0
        while (i < lines.size) {
            try {
                dates.add(Instant.parse(lines[i].trim()))
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                LOG.debug("Could not parse date: ${lines[i]}", e)
            }
            if (i + 1 < lines.size) {
                authors.add(lines[i + 1].trim())
            }
            i += 2
        }

        return GitActivityData(
            lastModified = dates.maxOrNull(),
            modificationCount = dates.size,
            uniqueAuthors = authors.size,
        )
    }

    companion object {
        private val LOG = Logger.getInstance(GitActivityAnalyzer::class.java)
    }
}
