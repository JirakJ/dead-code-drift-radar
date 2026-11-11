package com.jakubjirak.deadcoderadar.ui

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.table.JBTable
import com.jakubjirak.deadcoderadar.engine.DriftDetectionEngine
import com.jakubjirak.deadcoderadar.model.DriftLevel
import com.jakubjirak.deadcoderadar.model.DriftScore
import java.awt.BorderLayout
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.table.AbstractTableModel

/**
 * Tool window factory that creates the Drift Radar dashboard.
 * Shows a sortable list of elements with their drift scores and levels.
 */
class DriftRadarToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        LOG.info("Creating Drift Radar tool window")
        val panel = DriftRadarPanel(project)
        val content = ContentFactory.getInstance().createContent(panel, "Drift Radar", false)
        toolWindow.contentManager.addContent(content)
    }

    companion object {
        private val LOG = Logger.getInstance(DriftRadarToolWindowFactory::class.java)
    }
}

/**
 * Main panel for the drift radar tool window.
 */
class DriftRadarPanel(private val project: Project) : JPanel(BorderLayout()) {

    private val tableModel = DriftTableModel()
    private val table = JBTable(tableModel)
    private val filterCombo = JComboBox(arrayOf<Any>("All", "Active", "Cooling", "Drifting", "Dead"))

    init {
        val toolbar = JPanel()
        toolbar.add(filterCombo)
        filterCombo.addActionListener { applyFilter() }

        add(toolbar, BorderLayout.NORTH)
        add(JBScrollPane(table), BorderLayout.CENTER)

        table.autoCreateRowSorter = true
        refreshData()
    }

    /** Reloads cached results, keeping the currently selected level filter. */
    fun refreshData() {
        applyFilter()
    }

    private fun applyFilter() {
        val engine = DriftDetectionEngine.getInstance(project)
        val allResults = engine.getCachedResults()
        val selectedFilter = filterCombo.selectedItem as String

        val filtered = if (selectedFilter == "All") {
            allResults
        } else {
            val level = DriftLevel.valueOf(selectedFilter.uppercase())
            allResults.filter { it.level == level }
        }

        tableModel.setData(filtered)
    }
}

/**
 * Table model for displaying drift scores.
 */
class DriftTableModel : AbstractTableModel() {

    private var data: List<DriftScore> = emptyList()

    fun setData(scores: List<DriftScore>) {
        data = scores.sortedByDescending { it.score }
        fireTableDataChanged()
    }

    override fun getRowCount(): Int = data.size

    override fun getColumnCount(): Int = COLUMNS.size

    override fun getColumnName(column: Int): String = COLUMNS[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val score = data[rowIndex]
        return when (columnIndex) {
            COL_ELEMENT -> score.element.name
            COL_TYPE -> score.element.type.name
            COL_SCORE -> score.score
            COL_LEVEL -> score.level.label
            COL_LAST_MODIFIED -> formatDate(score)
            COL_REFERENCES -> score.referenceCount
            else -> ""
        }
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return when (columnIndex) {
            COL_SCORE, COL_REFERENCES -> Integer::class.java
            else -> String::class.java
        }
    }

    private fun formatDate(score: DriftScore): String {
        val instant = score.lastModified ?: return "Unknown"
        return instant.atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    companion object {
        private const val COL_ELEMENT = 0
        private const val COL_TYPE = 1
        private const val COL_SCORE = 2
        private const val COL_LEVEL = 3
        private const val COL_LAST_MODIFIED = 4
        private const val COL_REFERENCES = 5

        private val COLUMNS = arrayOf("Element", "Type", "Drift Score", "Level", "Last Modified", "References")
    }
}
