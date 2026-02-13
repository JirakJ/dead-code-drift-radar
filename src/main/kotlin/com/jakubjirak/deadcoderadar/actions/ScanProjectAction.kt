package com.jakubjirak.deadcoderadar.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.wm.ToolWindowManager
import com.jakubjirak.deadcoderadar.engine.DriftDetectionEngine
import com.jakubjirak.deadcoderadar.licensing.FreemiumGate
import com.jakubjirak.deadcoderadar.ui.DriftRadarPanel

/**
 * Action to scan the entire project for code drift.
 * Requires paid license for project-wide scanning.
 */
class ScanProjectAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        LOG.info("Starting project drift scan")

        if (!FreemiumGate.getInstance().isPaidFeatureAvailable(FreemiumGate.FEATURE_PROJECT_SCAN)) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("Dead Code Drift Radar")
                .createNotification(
                    "Upgrade to Pro for project-wide scanning",
                    NotificationType.INFORMATION,
                )
                .notify(project)
            return
        }

        ProgressManager.getInstance().run(object : Task.Backgroundable(
            project, "Scanning project for drift...", true
        ) {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                val engine = DriftDetectionEngine.getInstance(project)
                val results = engine.scanProject()

                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Dead Code Drift Radar")
                    .createNotification(
                        "Drift scan complete: ${results.size} elements analyzed",
                        NotificationType.INFORMATION,
                    )
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
        e.presentation.isEnabledAndVisible = e.project != null
    }

    companion object {
        private val LOG = Logger.getInstance(ScanProjectAction::class.java)
    }
}
