package com.jakubjirak.deadcoderadar.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.wm.ToolWindowManager
import com.jakubjirak.deadcoderadar.engine.DriftDetectionEngine
import com.jakubjirak.deadcoderadar.ui.DriftRadarPanel

/**
 * Action to scan the entire project for code drift.
 */
class ScanProjectAction : AnAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        LOG.info("Starting project drift scan")

        ProgressManager.getInstance().run(object : Task.Backgroundable(
            project, "Scanning project for drift...", true
        ) {
            private var analyzed = 0

            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                analyzed = DriftDetectionEngine.getInstance(project).scanProject().size
            }

            // onSuccess runs on the EDT, so the Swing table model is updated on the right thread.
            override fun onSuccess() {
                NotificationGroupManager.getInstance()
                    .getNotificationGroup("Dead Code Drift Radar")
                    .createNotification(
                        "Drift scan complete: $analyzed elements analyzed",
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
