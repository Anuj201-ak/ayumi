package com.ayumi.app.ui.log

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ayumi.app.data.AyumiDatabase
import com.ayumi.app.data.AyumiRepository
import com.ayumi.app.data.DayLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class LogUiState(
    val date: LocalDate = LocalDate.now(),
    val dateLabel: String = "",
    val text: String = "",
    val doneCount: Int = 0,
    val totalCount: Int = 0,
    val justSaved: Boolean = false
)

class LogViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AyumiRepository(AyumiDatabase.getDatabase(app))
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _justSaved = MutableStateFlow(false)

    val uiState: StateFlow<LogUiState> = _selectedDate.flatMapLatest { date ->
        val dateStr = date.format(DateTimeFormatter.ISO_DATE)
        combine(
            repo.getDayLogFlow(dateStr),
            repo.getDayOverrideFlow(dateStr),
            repo.getAllRoutineSlotsFlow(),
            repo.getCompletionsByDateFlow(dateStr),
            _justSaved
        ) { log, override, routine, completions, saved ->
            val total = override?.slots?.size ?: routine.size
            LogUiState(
                date = date,
                dateLabel = date.format(DateTimeFormatter.ofPattern("EEEE, d MMM")),
                text = log?.text ?: "",
                doneCount = completions.size,
                totalCount = total,
                justSaved = saved
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LogUiState())

    fun selectedDate(): LocalDate = _selectedDate.value

    fun goToPreviousDay() {
        _justSaved.value = false
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    fun goToNextDay() {
        val next = _selectedDate.value.plusDays(1)
        if (!next.isAfter(LocalDate.now())) {
            _justSaved.value = false
            _selectedDate.value = next
        }
    }

    fun save(text: String) {
        val dateStr = _selectedDate.value.format(DateTimeFormatter.ISO_DATE)
        viewModelScope.launch {
            if (text.isBlank()) repo.deleteDayLog(dateStr)
            else repo.insertDayLog(DayLog(dateStr, text.trim()))
            _justSaved.value = true
        }
    }

    fun clearSavedFlag() {
        _justSaved.value = false
    }
}
