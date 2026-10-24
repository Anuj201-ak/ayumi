package com.ayumi.app.ui.routine

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ayumi.app.alarm.AlarmScheduler
import com.ayumi.app.data.AyumiDatabase
import com.ayumi.app.data.AyumiRepository
import com.ayumi.app.data.RoutineSlot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime

class RoutineViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AyumiRepository(AyumiDatabase.getDatabase(app))

    val slots: StateFlow<List<RoutineSlot>> = repo.getAllRoutineSlotsFlow()
        .map { list -> list.sortedBy { it.startTime } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveSlot(slot: RoutineSlot) {
        viewModelScope.launch {
            if (slot.id == 0L) repo.insertRoutineSlot(slot) else repo.updateRoutineSlot(slot)
            AlarmScheduler.scheduleToday(getApplication())
        }
    }

    fun deleteSlot(slot: RoutineSlot) {
        viewModelScope.launch {
            repo.deleteRoutineSlot(slot)
            AlarmScheduler.scheduleToday(getApplication())
        }
    }

    /** "8H 15M" total scheduled time across the routine. */
    fun totalScheduled(slots: List<RoutineSlot>): String {
        var minutes = 0L
        slots.forEach { slot ->
            val s = LocalTime.parse(slot.startTime)
            val e = LocalTime.parse(slot.endTime)
            var diff = java.time.Duration.between(s, e).toMinutes()
            if (diff <= 0) diff += 24 * 60
            minutes += diff
        }
        val h = minutes / 60
        val m = minutes % 60
        return if (h == 0L) "${m}M" else "${h}H ${m}M"
    }
}
