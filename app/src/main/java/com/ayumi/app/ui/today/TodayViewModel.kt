package com.ayumi.app.ui.today

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ayumi.app.alarm.AlarmScheduler
import com.ayumi.app.data.AyumiDatabase
import com.ayumi.app.data.AyumiRepository
import com.ayumi.app.data.Completion
import com.ayumi.app.data.DayOverride
import com.ayumi.app.data.OverrideSlot
import com.ayumi.app.data.RoutineSlot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class SlotUi(
    val slot: OverrideSlot,
    val completion: Completion?,
    val isCurrent: Boolean
)

data class TodayUiState(
    val date: LocalDate = LocalDate.now(),
    val dateLabel: String = "",
    val isToday: Boolean = true,
    val hasOverride: Boolean = false,
    val slots: List<SlotUi> = emptyList(),
    val doneCount: Int = 0
)

class TodayViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AyumiRepository(AyumiDatabase.getDatabase(app))
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    val uiState: StateFlow<TodayUiState> = _selectedDate.flatMapLatest { date ->
        val dateStr = date.format(DateTimeFormatter.ISO_DATE)
        val isToday = date == LocalDate.now()
        combine(
            repo.getDayOverrideFlow(dateStr),
            repo.getAllRoutineSlotsFlow(),
            repo.getCompletionsByDateFlow(dateStr)
        ) { override, routine, completions ->
            val plan: List<OverrideSlot> = override?.slots
                ?: routine.map { it.toOverrideSlot() }.sortedBy { it.startTime }
            val byId = completions.associateBy { it.slotId }
            val now = LocalTime.now()
            val items = plan.map { slot ->
                SlotUi(
                    slot = slot,
                    completion = byId[slot.id],
                    isCurrent = isToday && isNowInSlot(now, slot.startTime, slot.endTime)
                )
            }
            TodayUiState(
                date = date,
                dateLabel = date.format(DateTimeFormatter.ofPattern("EEEE, MMM d")),
                isToday = isToday,
                hasOverride = override != null,
                slots = items,
                doneCount = items.count { it.completion != null }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodayUiState())

    init {
        // First launch: seed a starter routine (replaceable in the Routine tab).
        viewModelScope.launch {
            val prefs = getApplication<Application>()
                .getSharedPreferences("ayumi", Context.MODE_PRIVATE)
            if (!prefs.getBoolean("seeded_v1", false)) {
                if (repo.getAllRoutineSlots().isEmpty()) {
                    listOf(
                        RoutineSlot(title = "Morning walk", startTime = "06:00", endTime = "06:30"),
                        RoutineSlot(title = "Deep work", startTime = "09:00", endTime = "12:00", important = true),
                        RoutineSlot(title = "Lunch break", startTime = "12:00", endTime = "13:00"),
                        RoutineSlot(title = "Study Kotlin", startTime = "18:00", endTime = "19:00"),
                        RoutineSlot(title = "Sleep", startTime = "23:00", endTime = "07:00")
                    ).forEach { repo.insertRoutineSlot(it) }
                }
                prefs.edit().putBoolean("seeded_v1", true).apply()
            }
        }
    }

    fun goToPreviousDay() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun goToNextDay() {
        val next = _selectedDate.value.plusDays(1)
        if (!next.isAfter(LocalDate.now())) _selectedDate.value = next
    }

    fun goToToday() {
        _selectedDate.value = LocalDate.now()
    }

    /** Tap the circle: mark done (stores timestamp) or undo (clears it). Works for past days too. */
    fun toggleDone(slot: OverrideSlot) {
        val dateStr = _selectedDate.value.format(DateTimeFormatter.ISO_DATE)
        viewModelScope.launch {
            val existing = repo.getCompletion(slot.id, dateStr)
            if (existing == null) {
                repo.insertCompletion(Completion(slot.id, dateStr, System.currentTimeMillis()))
            } else {
                repo.deleteCompletion(slot.id, dateStr)
            }
        }
    }

    /** Early/late vs the slot's planned end. */
    fun timingLabel(slot: OverrideSlot, completion: Completion, date: LocalDate): String {
        val diffMin = (completion.completedAt - slotEndMillis(slot, date)) / 60_000
        return when {
            diffMin < 0 -> "${-diffMin} min early"
            diffMin == 0L -> "On time"
            else -> "$diffMin min late"
        }
    }

    fun isEarly(slot: OverrideSlot, completion: Completion, date: LocalDate): Boolean {
        return completion.completedAt <= slotEndMillis(slot, date)
    }

    /** Save a customized plan for the selected day (never touches the default routine). */
    fun saveDayOverride(slots: List<OverrideSlot>) {
        val dateStr = _selectedDate.value.format(DateTimeFormatter.ISO_DATE)
        viewModelScope.launch {
            repo.insertDayOverride(DayOverride(dateStr, slots))
            AlarmScheduler.scheduleToday(getApplication())
        }
    }

    fun resetDayOverride() {
        val dateStr = _selectedDate.value.format(DateTimeFormatter.ISO_DATE)
        viewModelScope.launch {
            repo.deleteDayOverride(dateStr)
            AlarmScheduler.scheduleToday(getApplication())
        }
    }

    private fun slotEndMillis(slot: OverrideSlot, date: LocalDate): Long {
        val start = LocalTime.parse(slot.startTime)
        val end = LocalTime.parse(slot.endTime)
        val endDate = if (end <= start) date.plusDays(1) else date
        return endDate.atTime(end).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun isNowInSlot(now: LocalTime, start: String, end: String): Boolean {
        val s = LocalTime.parse(start)
        val e = LocalTime.parse(end)
        return if (e > s) now in s..e else now >= s || now <= e
    }

    private fun RoutineSlot.toOverrideSlot() = OverrideSlot(
        id = id,
        title = title,
        startTime = startTime,
        endTime = endTime,
        important = important,
        description = description,
        reminderOn = reminderOn
    )
}
