package com.jakubjirak.deadcoderadar.analyzer

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.jakubjirak.deadcoderadar.model.CodeElement

/**
 * Counts usages/references to code elements within the project scope.
 * Uses PSI tree walking to find references.
 */
class ReferenceAnalyzer(private val project: Project) {

    /**
     * Counts the number of references to a code element across the project.
     * For file-level analysis, counts references to all declarations in the file.
     */
    @Suppress("UNUSED_PARAMETER")
    fun countReferences(element: CodeElement): Int {
        return try {
            countFileReferences(element.filePath)
        } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
            LOG.warn("Failed to count references for ${element.name}", e)
            0
        }
    }

    private fun countFileReferences(filePath: String): Int {
        val basePath = project.basePath ?: return 0
        val fullPath = "$basePath/$filePath"

        val fileIndex = ProjectFileIndex.getInstance(project)
        var referenceCount = 0

        fileIndex.iterateContent { file ->
            if (file.extension in SUPPORTED_EXTENSIONS && file.path != fullPath) {
                referenceCount += countReferencesInFile(file, filePath)
            }
            true
        }

        return referenceCount
    }

    private fun countReferencesInFile(file: VirtualFile, targetPath: String): Int {
        val psiFile = PsiManager.getInstance(project).findFile(file) ?: return 0
        var count = 0

        psiFile.accept(object : PsiRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)
                val references: Array<PsiReference> = element.references
                for (ref in references) {
                    val resolved = ref.resolve()
                    if (resolved != null) {
                        val resolvedPath = resolved.containingFile?.virtualFile?.path ?: continue
                        if (resolvedPath.endsWith(targetPath)) {
                            count++
                        }
                    }
                }
            }
        })

        return count
    }

    companion object {
        private val LOG = Logger.getInstance(ReferenceAnalyzer::class.java)
        private val SUPPORTED_EXTENSIONS = setOf("java", "kt", "kts", "py", "js", "ts")
    }
}
