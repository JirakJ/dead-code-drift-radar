package com.jakubjirak.deadcoderadar.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.wm.ToolWindowManager
import com.jakubjirak.deadcoderadar.engine.DriftDetectionEngine
import com.jakubjirak.deadcoderadar.ui.DriftRadarPanel

/**
 * Action to scan the currently open file for code drift.
 * Available in free tier.
 */
class ScanFileAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        LOG.info("Starting file drift scan for ${file.name}")

        ProgressManager.getInstance().run(object : Task.Backgroundable(
            project, "Scanning file for drift...", true
        ) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                val engine = DriftDetectionEngine.getInstance(project)
                val results = engine.scanFile(file)

                val message = if (results.isNotEmpty()) {
                    val score = results.first()
                    "Drift score for ${file.name}: ${score.score} (${score.level.label})"
                } else {
                    "No drift data available for ${file.name}"
                }

                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Dead Code Drift Radar")
                    .createNotification(message, NotificationType.INFORMATION)
                    .notify(project)

                refreshToolWindow(project)
            }
        })
    }

    private fun refreshToolWindow(project: com.intellij.openapi.project.Project) {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Drift Radar") ?: return
        val content = toolWindow.contentManager.getContent(0) ?: return
        val panel = content.component as? DriftRadarPanel ?: return
        panel.refreshData()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null && e.getData(CommonDataKeys.VIRTUAL_FILE) != null
    }

    companion object {
        private val LOG = Logger.getInstance(ScanFileAction::class.java)
    }
}
