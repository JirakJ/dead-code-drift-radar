package com.jakubjirak.deadcoderadar.ui

import com.jakubjirak.deadcoderadar.model.CodeElement
import com.jakubjirak.deadcoderadar.model.DriftScore
import com.jakubjirak.deadcoderadar.model.ElementType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DriftTableModelTest {

    private fun score(name: String, value: Int, lastModified: Instant? = null) =
        DriftScore(CodeElement("src/$name.kt", name, ElementType.CLASS, 1, 0), value, lastModified, 1, 2)

    @Test
    fun `rows are sorted by descending drift score`() {
        val model = DriftTableModel()
        model.setData(listOf(score("Low", 10), score("High", 90), score("Mid", 50)))
        assertEquals(3, model.rowCount)
        assertEquals(listOf("High", "Mid", "Low"), (0 until 3).map { model.getValueAt(it, 0) })
    }

    @Test
    fun `columns expose type, score, level, date and references`() {
        val instant = Instant.parse("2024-05-06T12:00:00Z")
        val model = DriftTableModel()
        model.setData(listOf(score("A", 80, instant)))
        assertEquals(6, model.columnCount)
        assertEquals("Drift Score", model.getColumnName(2))
        assertEquals("CLASS", model.getValueAt(0, 1))
        assertEquals(80, model.getValueAt(0, 2))
        assertEquals("Dead", model.getValueAt(0, 3))
        val expectedDate = instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        assertEquals(expectedDate, model.getValueAt(0, 4))
        assertEquals(2, model.getValueAt(0, 5))
        assertEquals(Integer::class.java, model.getColumnClass(2))
        assertEquals(String::class.java, model.getColumnClass(3))
    }

    @Test
    fun `unknown last-modified date is shown as Unknown`() {
        val model = DriftTableModel()
        model.setData(listOf(score("A", 10)))
        assertEquals("Unknown", model.getValueAt(0, 4))
    }
}
