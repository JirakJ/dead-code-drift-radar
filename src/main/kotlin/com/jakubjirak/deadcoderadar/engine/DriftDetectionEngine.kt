package com.jakubjirak.deadcoderadar.engine

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.jakubjirak.deadcoderadar.analyzer.DriftScoreCalculator
import com.jakubjirak.deadcoderadar.analyzer.GitActivityAnalyzer
import com.jakubjirak.deadcoderadar.analyzer.ReferenceAnalyzer
import com.jakubjirak.deadcoderadar.model.CodeElement
import com.jakubjirak.deadcoderadar.model.DriftScore
import com.jakubjirak.deadcoderadar.model.ElementType
import com.jakubjirak.deadcoderadar.settings.DeadCodeRadarSettings
import java.util.concurrent.ConcurrentHashMap

/**
 * Orchestrates drift analysis by combining git activity analysis and reference counting.
 * Maintains a cache of results per project.
 */
@Service(Service.Level.PROJECT)
class DriftDetectionEngine(private val project: Project) {

    private val cache = ConcurrentHashMap<String, DriftScore>()
    private val calculator = DriftScoreCalculator()

    /**
     * Scans a single file and returns drift scores for its elements.
     */
    fun scanFile(file: VirtualFile): List<DriftScore> {
        val basePath = project.basePath ?: return emptyList()
        val relativePath = file.path.removePrefix("$basePath/")

        if (isIgnored(relativePath)) return emptyList()

        return try {
            val element = CodeElement(
                filePath = relativePath,
                name = file.nameWithoutExtension,
                type = ElementType.CLASS,
                lineStart = 1,
                lineEnd = 0,
            )

            val gitAnalyzer = GitActivityAnalyzer(project)
            val refAnalyzer = ReferenceAnalyzer(project)

            val gitData = gitAnalyzer.analyzeFile(file)
            val refCount = refAnalyzer.countReferences(element)

            val score = calculator.calculate(element, gitData, refCount)
            cache[element.qualifiedId] = score

            listOf(score)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            LOG.warn("Failed to scan file: ${file.path}", e)
            emptyList()
        }
    }

    /**
     * Scans all project files and returns drift scores.
     */
    fun scanProject(): List<DriftScore> {
        val results = mutableListOf<DriftScore>()
        val fileIndex = ProjectFileIndex.getInstance(project)

        fileIndex.iterateContent { file ->
            if (file.extension in SUPPORTED_EXTENSIONS && !file.isDirectory) {
                results.addAll(scanFile(file))
            }
            true
        }

        LOG.info("Project scan complete: ${results.size} elements analyzed")
        return results
    }

    /**
     * Returns cached results or empty list if no scan has been performed.
     */
    fun getCachedResults(): List<DriftScore> = cache.values.toList()

    /**
     * Clears the results cache.
     */
    fun clearCache() {
        cache.clear()
    }

    private fun isIgnored(path: String): Boolean {
        val settings = DeadCodeRadarSettings.getInstance()
        return settings.ignoredPaths.any { pattern ->
            path.startsWith(pattern) || matchGlob(pattern, path)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(DriftDetectionEngine::class.java)
        private val SUPPORTED_EXTENSIONS = setOf("java", "kt", "kts", "py", "js", "ts", "tsx", "jsx")

        fun getInstance(project: Project): DriftDetectionEngine =
            project.getService(DriftDetectionEngine::class.java)

        internal fun matchGlob(pattern: String, path: String): Boolean {
            val regex = pattern
                .replace(".", "\\.")
                .replace("**/", "(.*/)?")
                .replace("*", "[^/]*")
                .let { "^$it$" }
            return try {
                Regex(regex).matches(path)
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                LOG.debug("Invalid glob pattern: $pattern", e)
                false
            }
        }
    }
}
