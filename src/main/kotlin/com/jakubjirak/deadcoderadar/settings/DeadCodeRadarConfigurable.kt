package com.jakubjirak.deadcoderadar.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.dsl.builder.*

/**
 * Settings UI for Dead Code & Drift Radar.
 * Available under Settings → Tools → Dead Code & Drift Radar.
 */
class DeadCodeRadarConfigurable : BoundConfigurable("Dead Code & Drift Radar") {

    override fun createPanel() = panel {
        val settings = DeadCodeRadarSettings.getInstance()

        group("General") {
            row {
                checkBox("Enable drift analysis")
                    .bindSelected(settings::enabled)
                    .comment("When enabled, the plugin continuously monitors code drift levels across the project")
            }
        }

        group("Drift Thresholds") {
            row("Active threshold:") {
                spinner(0..100)
                    .bindIntValue(settings::activeThreshold)
                    .comment("Code with a drift score below this value is considered <b>Active</b> (recently touched)")
            }
            row("Cooling threshold:") {
                spinner(0..100)
                    .bindIntValue(settings::coolingThreshold)
                    .comment("Score between Active and this value marks code as <b>Cooling</b> (not recently modified)")
            }
            row("Drifting threshold:") {
                spinner(0..100)
                    .bindIntValue(settings::driftingThreshold)
                    .comment("Score between Cooling and this value marks code as <b>Drifting</b>; above it is <b>Dead</b>")
            }
        }

        group("Filtering") {
            row("Ignored paths:") {
                expandableTextField()
                    .bindText(settings::ignoredPathsText)
                    .comment("Comma-separated list of paths to ignore during scanning")
                    .align(AlignX.FILL)
            }
        }
    }
}
