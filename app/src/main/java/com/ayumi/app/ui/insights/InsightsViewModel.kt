package com.ayumi.app.ui.insights

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ayumi.app.data.AyumiDatabase
import com.ayumi.app.data.AyumiRepository
import com.ayumi.app.data.OverrideSlot
import com.ayumi.app.data.RoutineSlot
import com.ayumi.app.export.ExportRow
import com.ayumi.app.export.ExportUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class StatsRange(val days: Int, val label: String) {
    Week(7, "week"), Month(30, "month")
}

data class DayDetail(
    val date: LocalDate,
    val slots: List<SlotStatus>
)

data class SlotStatus(
    val slot: OverrideSlot,
    /** minutes late vs planned end; negative = early. Null = not done. */
    val diffMin: Long?,
    val completedAtLabel: String?
)

data class InsightsData(
    val range: StatsRange,
    val completionPct: Int,
    val plannedTotal: Int,
    val doneTotal: Int,
    val early: Int,
    val onTime: Int,
    val late: Int,
    val missed: Int,
    val days: List<DayDetail>,
    val consistency: List<SlotConsistency>
)

data class SlotConsistency(
    val title: String,
    val important: Boolean,
    val daysDone: Int,
    val daysTotal: Int
)

class InsightsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AyumiRepository(AyumiDatabase.getDatabase(app))
    private val _range = MutableStateFlow(StatsRange.Week)
    val range: StateFlow<StatsRange> = _range.asStateFlow()

    private val _data = MutableStateFlow<InsightsData?>(null)
    val data: StateFlow<InsightsData?> = _data.asStateFlow()

    private val _exporting = MutableStateFlow(false)
    val exporting: StateFlow<Boolean> = _exporting.asStateFlow()

    init {
        refresh()
    }

    fun setRange(range: StatsRange) {
        _range.value = range
        refresh()
    }

    fun refresh() {
        viewModelScope.launch { _data.value = compute(_range.value) }
    }

    private suspend fun compute(range: StatsRange): InsightsData {
        val today = LocalDate.now()
        val days = (0 until range.days).map { today.minusDays(it.toLong()) }.reversed()
        val routine = repo.getAllRoutineSlots()

        var planned = 0
        var done = 0
        var early = 0
        var onTime = 0
        var late = 0
        var missed = 0

        val dayDetails = days.map { date ->
            val dateStr = date.format(DateTimeFormatter.ISO_DATE)
            val override = repo.getDayOverride(dateStr)
            val plan: List<OverrideSlot> = override?.slots
                ?: routine.map { it.toOverrideSlot() }
            val completions = repo.getCompletionsByDate(dateStr).associateBy { it.slotId }
            val statuses = plan.map { slot ->
                val c = completions[slot.id]
                if (c == null) {
                    SlotStatus(slot, null, null)
                } else {
                    val diff = (c.completedAt - slotEndMillis(slot, date)) / 60_000
                    val label = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                        .format(java.util.Date(c.completedAt))
                    SlotStatus(slot, diff, label)
                }
            }
            statuses.forEach { s ->
                if (s.diffMin == null) {
                    if (date.isBefore(today)) missed++
                } else {
                    done++
                    when {
                        s.diffMin < -10 -> early++
                        s.diffMin <= 10 -> onTime++
                        else -> late++
                    }
                }
            }
            planned += plan.size
            DayDetail(date, statuses)
        }

        // Per-slot consistency over the range (routine slots only).
        val startStr = days.first().format(DateTimeFormatter.ISO_DATE)
        val endStr = days.last().format(DateTimeFormatter.ISO_DATE)
        val rangeCompletions = repo.getCompletionsRange(startStr, endStr)
        val consistency = routine.map { slot ->
            val d = rangeCompletions.count { it.slotId == slot.id }
            SlotConsistency(slot.title, slot.important, d, range.days)
        }.sortedByDescending { if (it.daysTotal == 0) 0.0 else it.daysDone.toDouble() / it.daysTotal }

        return InsightsData(
            range = range,
            completionPct = if (planned == 0) 0 else (done * 100 / planned),
            plannedTotal = planned,
            doneTotal = done,
            early = early,
            onTime = onTime,
            late = late,
            missed = missed,
            days = dayDetails,
            consistency = consistency
        )
    }

    fun exportExcel() {
        val snapshot = _data.value ?: return
        _exporting.value = true
        viewModelScope.launch {
            try {
                val rows = buildExportRows(snapshot)
                val file = withContext(Dispatchers.IO) {
                    ExportUtils.exportExcel(getApplication(), rows, snapshot.range.label)
                }
                ExportUtils.share(
                    getApplication(), file,
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                _exporting.value = false
            }
        }
    }

    fun exportPdf() {
        val snapshot = _data.value ?: return
        _exporting.value = true
        viewModelScope.launch {
            try {
                val rows = buildExportRows(snapshot)
                val summary = listOf(
                    "Completion: ${snapshot.completionPct}% (${snapshot.doneTotal} of ${snapshot.plannedTotal} slots)",
                    "Early: ${snapshot.early}   On time: ${snapshot.onTime}   Late: ${snapshot.late}   Missed: ${snapshot.missed}"
                )
                val file = withContext(Dispatchers.IO) {
                    ExportUtils.exportPdf(getApplication(), rows, snapshot.range.label, summary)
                }
                ExportUtils.share(getApplication(), file, "application/pdf")
            } catch (e: Exception) {
                Toast.makeText(getApplication(), "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                _exporting.value = false
            }
        }
    }

    private fun buildExportRows(snapshot: InsightsData): List<ExportRow> {
        val fmt = DateTimeFormatter.ofPattern("d MMM")
        return snapshot.days.flatMap { day ->
            day.slots.map { s ->
                val status = when {
                    s.diffMin == null -> "Missed"
                    s.diffMin < -10 -> "Early"
                    s.diffMin <= 10 -> "On time"
                    else -> "Late"
                }
                ExportRow(
                    date = day.date.format(fmt),
                    title = s.slot.title,
                    start = s.slot.startTime,
                    end = s.slot.endTime,
                    status = status,
                    completedAt = s.completedAtLabel ?: "—"
                )
            }
        }
    }

    private fun slotEndMillis(slot: OverrideSlot, date: LocalDate): Long {
        val start = LocalTime.parse(slot.startTime)
        val end = LocalTime.parse(slot.endTime)
        val endDate = if (end <= start) date.plusDays(1) else date
        return endDate.atTime(end).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun RoutineSlot.toOverrideSlot() = OverrideSlot(
        id = id, title = title, startTime = startTime, endTime = endTime,
        important = important, description = description, reminderOn = reminderOn
    )
}
