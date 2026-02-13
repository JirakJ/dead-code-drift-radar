package com.jakubjirak.deadcoderadar.settings

import com.intellij.openapi.options.Configurable
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextArea
import javax.swing.SpinnerNumberModel

/**
 * Settings UI for Dead Code & Drift Radar.
 * Available under Settings → Tools → Dead Code & Drift Radar.
 */
class DeadCodeRadarConfigurable : Configurable {

    private var panel: JPanel? = null
    private var enabledCheckbox: JCheckBox? = null
    private var activeSpinner: JSpinner? = null
    private var coolingSpinner: JSpinner? = null
    private var driftingSpinner: JSpinner? = null
    private var ignoredPathsArea: JTextArea? = null

    override fun getDisplayName(): String = "Dead Code & Drift Radar"

    override fun createComponent(): JComponent {
        val settings = DeadCodeRadarSettings.getInstance()

        panel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints().apply {
            insets = Insets(4, 4, 4, 4)
            anchor = GridBagConstraints.WEST
            fill = GridBagConstraints.HORIZONTAL
        }

        enabledCheckbox = JCheckBox("Enable drift analysis", settings.enabled)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2
        panel!!.add(enabledCheckbox, gbc)

        gbc.gridwidth = 1

        gbc.gridx = 0; gbc.gridy = 1
        panel!!.add(JLabel("Active threshold (score below):"), gbc)
        activeSpinner = JSpinner(SpinnerNumberModel(settings.activeThreshold, 0, 100, 5))
        gbc.gridx = 1
        panel!!.add(activeSpinner, gbc)

        gbc.gridx = 0; gbc.gridy = 2
        panel!!.add(JLabel("Cooling threshold (score below):"), gbc)
        coolingSpinner = JSpinner(SpinnerNumberModel(settings.coolingThreshold, 0, 100, 5))
        gbc.gridx = 1
        panel!!.add(coolingSpinner, gbc)

        gbc.gridx = 0; gbc.gridy = 3
        panel!!.add(JLabel("Drifting threshold (score below):"), gbc)
        driftingSpinner = JSpinner(SpinnerNumberModel(settings.driftingThreshold, 0, 100, 5))
        gbc.gridx = 1
        panel!!.add(driftingSpinner, gbc)

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2
        panel!!.add(JLabel("Ignored paths (glob patterns, one per line):"), gbc)

        ignoredPathsArea = JTextArea(settings.ignoredPathsText, 5, 40)
        gbc.gridy = 5; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0
        panel!!.add(ignoredPathsArea, gbc)

        return panel!!
    }

    override fun isModified(): Boolean {
        val settings = DeadCodeRadarSettings.getInstance()
        return enabledCheckbox?.isSelected != settings.enabled ||
            (activeSpinner?.value as? Int) != settings.activeThreshold ||
            (coolingSpinner?.value as? Int) != settings.coolingThreshold ||
            (driftingSpinner?.value as? Int) != settings.driftingThreshold ||
            ignoredPathsArea?.text != settings.ignoredPathsText
    }

    override fun apply() {
        val settings = DeadCodeRadarSettings.getInstance()
        settings.enabled = enabledCheckbox?.isSelected ?: true
        settings.activeThreshold = (activeSpinner?.value as? Int) ?: 25
        settings.coolingThreshold = (coolingSpinner?.value as? Int) ?: 50
        settings.driftingThreshold = (driftingSpinner?.value as? Int) ?: 75
        settings.ignoredPathsText = ignoredPathsArea?.text ?: ""
    }

    override fun reset() {
        val settings = DeadCodeRadarSettings.getInstance()
        enabledCheckbox?.isSelected = settings.enabled
        activeSpinner?.value = settings.activeThreshold
        coolingSpinner?.value = settings.coolingThreshold
        driftingSpinner?.value = settings.driftingThreshold
        ignoredPathsArea?.text = settings.ignoredPathsText
    }

    override fun disposeUIResources() {
        panel = null
        enabledCheckbox = null
        activeSpinner = null
        coolingSpinner = null
        driftingSpinner = null
        ignoredPathsArea = null
    }
}
