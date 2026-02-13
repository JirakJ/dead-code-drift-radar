package com.jakubjirak.deadcoderadar.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.util.IconLoader
import com.jakubjirak.deadcoderadar.model.DriftLevel
import com.jakubjirak.deadcoderadar.model.DriftScore
import javax.swing.Icon

/**
 * Renders drift level icons in the editor gutter for methods and classes.
 */
class DriftGutterIconRenderer(private val driftScore: DriftScore) : GutterIconRenderer() {

    override fun getIcon(): Icon = getIconForLevel(driftScore.level)

    override fun getTooltipText(): String {
        val level = driftScore.level.label
        val score = driftScore.score
        val refs = driftScore.referenceCount
        val mods = driftScore.modificationCount
        return "Drift: $level (score: $score) | References: $refs | Modifications: $mods"
    }

    override fun getClickAction(): AnAction? = null

    override fun isNavigateAction(): Boolean = false

    override fun getAlignment(): Alignment = Alignment.RIGHT

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DriftGutterIconRenderer) return false
        return driftScore.element.qualifiedId == other.driftScore.element.qualifiedId
    }

    override fun hashCode(): Int = driftScore.element.qualifiedId.hashCode()

    companion object {
        private val ICON_ACTIVE = IconLoader.getIcon("/icons/drift_active.svg", DriftGutterIconRenderer::class.java)
        private val ICON_COOLING = IconLoader.getIcon("/icons/drift_cooling.svg", DriftGutterIconRenderer::class.java)
        private val ICON_DRIFTING = IconLoader.getIcon(
            "/icons/drift_drifting.svg",
            DriftGutterIconRenderer::class.java,
        )
        private val ICON_DEAD = IconLoader.getIcon("/icons/drift_dead.svg", DriftGutterIconRenderer::class.java)

        fun getIconForLevel(level: DriftLevel): Icon = when (level) {
            DriftLevel.ACTIVE -> ICON_ACTIVE
            DriftLevel.COOLING -> ICON_COOLING
            DriftLevel.DRIFTING -> ICON_DRIFTING
            DriftLevel.DEAD -> ICON_DEAD
        }
    }
}
