package com.jakubjirak.deadcoderadar.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.jakubjirak.deadcoderadar.model.DriftThresholds

/**
 * Persistent settings for the Dead Code & Drift Radar plugin.
 * Configures drift thresholds, scan scope, and ignored paths.
 */
@State(
    name = "DeadCodeRadarSettings",
    storages = [Storage("deadCodeRadarSettings.xml")]
)
class DeadCodeRadarSettings : PersistentStateComponent<DeadCodeRadarSettings.State> {

    data class State(
        var activeThreshold: Int = 25,
        var coolingThreshold: Int = 50,
        var driftingThreshold: Int = 75,
        var ignoredPathsText: String = "",
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    var activeThreshold: Int
        get() = state.activeThreshold
        set(value) { state.activeThreshold = value }

    var coolingThreshold: Int
        get() = state.coolingThreshold
        set(value) { state.coolingThreshold = value }

    var driftingThreshold: Int
        get() = state.driftingThreshold
        set(value) { state.driftingThreshold = value }

    /** Ignored path patterns; one per line and/or comma-separated. */
    val ignoredPaths: List<String>
        get() = parsePatterns(state.ignoredPathsText)

    /** Level boundaries as configured, sorted so that a mis-ordered configuration still classifies sanely. */
    val thresholds: DriftThresholds
        get() {
            val (active, cooling, drifting) = listOf(activeThreshold, coolingThreshold, driftingThreshold).sorted()
            return DriftThresholds(activeBelow = active, coolingBelow = cooling, driftingBelow = drifting)
        }

    var ignoredPathsText: String
        get() = state.ignoredPathsText
        set(value) { state.ignoredPathsText = value }

    companion object {
        internal fun parsePatterns(text: String): List<String> =
            text.split('\n', ',')
                .map { it.trim() }
                .filter { it.isNotEmpty() }

        fun getInstance(): DeadCodeRadarSettings =
            ApplicationManager.getApplication().getService(DeadCodeRadarSettings::class.java)
    }
}
