package com.ayumi.app.ui.today

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayumi.app.alarm.AlarmScheduler
import com.ayumi.app.ui.day.DayOverrideSheet
import com.ayumi.app.ui.theme.CardWhite
import com.ayumi.app.ui.theme.Cream
import com.ayumi.app.ui.theme.Ink
import com.ayumi.app.ui.theme.InkSoft
import com.ayumi.app.ui.theme.Jakarta
import com.ayumi.app.ui.theme.Manrope
import com.ayumi.app.ui.theme.Matcha
import com.ayumi.app.ui.theme.Mint
import com.ayumi.app.ui.theme.Outline
import com.ayumi.app.ui.theme.Peach
import com.ayumi.app.ui.theme.PeachLight
import com.ayumi.app.ui.theme.Persimmon
import com.ayumi.app.ui.theme.SandDeep

private val QUOTES = listOf(
    "Continuous effort, not strength nor intelligence, is the key to unlocking our potential.",
    "A journey of a thousand miles begins with a single step.",
    "Fall seven times, stand up eight.",
    "What you do every day matters more than what you do once in a while."
)

@Composable
fun TodayScreen(
    onOpenLog: () -> Unit,
    vm: TodayViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current
    var showOverrideSheet by remember { mutableStateOf(false) }

    val total = state.slots.size
    val done = state.doneCount
    val pct = if (total == 0) 0 else (done * 100 / total)
    val remaining = total - done

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text("Today", fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Ink)
                Text("  歩み", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Matcha,
                    modifier = Modifier.padding(bottom = 4.dp))
            }
        }

        // Date navigation
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { vm.goToPreviousDay() }) {
                        Icon(Icons.Filled.ChevronLeft, "Previous day", tint = Ink)
                    }
                    Text(state.dateLabel, fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Ink)
                    IconButton(onClick = { vm.goToNextDay() }) {
                        Icon(Icons.Filled.ChevronRight, "Next day",
                            tint = if (state.isToday) Ink.copy(alpha = 0.25f) else Ink)
                    }
                }
                IconButton(
                    onClick = { vm.goToToday() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(PeachLight)
                        .size(40.dp)
                ) {
                    Icon(Icons.Filled.CalendarMonth, "Go to today", tint = Persimmon)
                }
            }
        }

        // Exact-alarm permission nudge
        if (state.isToday && state.slots.any { it.slot.reminderOn } &&
            !AlarmScheduler.canScheduleExactAlarms(context)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PeachLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Notifications, null, tint = Persimmon)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Reminders need permission",
                                fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                            Text("Allow exact alarms so Ayumi can chime on time.",
                                fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                        }
                        Text(
                            "Enable",
                            fontFamily = Manrope, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Persimmon,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        // Progress card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).clip(CircleShape).background(Persimmon))
                            Spacer(Modifier.width(8.dp))
                            Text("$done of $total done",
                                fontFamily = Jakarta, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Ink)
                        }
                        Text("$pct% Completed",
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Persimmon)
                    }
                    Spacer(Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { if (total == 0) 0f else done.toFloat() / total },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Persimmon,
                        trackColor = SandDeep
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (total == 0) "No rituals scheduled — add your flow in the Routine tab."
                        else "Quiet progress. $remaining ritual${if (remaining == 1) "" else "s"} remain in your flow today.",
                        fontFamily = Manrope, fontSize = 13.sp, color = InkSoft
                    )
                }
            }
        }

        // Timeline
        items(state.slots, key = { it.slot.id }) { item ->
            TimelineRow(
                item = item,
                timingText = item.completion?.let { vm.timingLabel(item.slot, it, state.date) },
                isEarly = item.completion?.let { vm.isEarly(item.slot, it, state.date) } ?: true,
                onToggle = { vm.toggleDone(item.slot) }
            )
        }

        // Mindful thought
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("MINDFUL THOUGHT",
                        fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Matcha)
                    Spacer(Modifier.height(6.dp))
                    Text("“${QUOTES[(state.date.dayOfYear) % QUOTES.size]}”",
                        fontFamily = Manrope, fontSize = 14.sp, color = Ink, lineHeight = 20.sp)
                }
            }
        }

        // Action buttons
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showOverrideSheet = true },
                    colors = ButtonDefaults.buttonColors(containerColor = PeachLight, contentColor = Ink),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Icon(Icons.Filled.Tune, null, tint = Ink)
                    Spacer(Modifier.width(8.dp))
                    Text("Customize Day", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Button(
                    onClick = onOpenLog,
                    colors = ButtonDefaults.buttonColors(containerColor = Matcha, contentColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Icon(Icons.Filled.ReceiptLong, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Daily Log", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
            if (state.hasOverride) {
                Text("This day uses a customized plan.",
                    fontFamily = Manrope, fontSize = 12.sp, color = InkSoft,
                    modifier = Modifier.padding(top = 6.dp))
            }
            Spacer(Modifier.height(8.dp))
        }
    }

    if (showOverrideSheet) {
        DayOverrideSheet(
            initialSlots = state.slots.map { it.slot },
            dateLabel = state.dateLabel,
            hasOverride = state.hasOverride,
            onSave = { vm.saveDayOverride(it); showOverrideSheet = false },
            onReset = { vm.resetDayOverride(); showOverrideSheet = false },
            onDismiss = { showOverrideSheet = false }
        )
    }
}

@Composable
private fun TimelineRow(
    item: SlotUi,
    timingText: String?,
    isEarly: Boolean,
    onToggle: () -> Unit
) {
    val slot = item.slot
    val isDone = item.completion != null
    Row(modifier = Modifier.fillMaxWidth()) {
        // Time rail
        Column(
            modifier = Modifier.width(52.dp).padding(top = 14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(slot.startTime, fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 12.sp,
                color = if (item.isCurrent) Persimmon else InkSoft)
            Text(slot.endTime, fontFamily = Manrope, fontSize = 11.sp, color = InkSoft.copy(alpha = 0.7f))
        }
        // Rail line + dot
        Box(modifier = Modifier.width(20.dp).fillMaxHeight()) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center)
                    .background(SandDeep)
            )
            Box(
                modifier = Modifier
                    .size(9.dp)
                    .clip(CircleShape)
                    .background(if (item.isCurrent) Persimmon else SandDeep)
                    .align(Alignment.TopCenter)
                    .padding(top = 0.dp)
            )
        }
        // Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box {
                if (item.isCurrent && !isDone) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .fillMaxHeight()
                            .background(Persimmon)
                            .align(Alignment.CenterStart)
                    )
                }
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (item.isCurrent && !isDone) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Mint)
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                            ) {
                                Text("● NOW", fontFamily = Manrope, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Matcha)
                            }
                            Spacer(Modifier.height(6.dp))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (slot.important) {
                                Icon(Icons.Filled.Star, null, tint = Matcha, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                            }
                            Text(slot.title,
                                fontFamily = Jakarta, fontWeight = FontWeight.SemiBold, fontSize = 16.sp,
                                color = if (isDone) Ink.copy(alpha = 0.45f) else Ink)
                        }
                        if (timingText != null) {
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isEarly) Mint else Peach)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(timingText,
                                        fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp,
                                        color = if (isEarly) Matcha else Persimmon)
                                }
                                if (slot.description.isNotBlank()) {
                                    Spacer(Modifier.width(8.dp))
                                    Text(slot.description,
                                        fontFamily = Manrope, fontSize = 12.sp, color = InkSoft,
                                        maxLines = 2)
                                }
                            }
                        } else if (slot.description.isNotBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(slot.description,
                                fontFamily = Manrope, fontSize = 13.sp, color = InkSoft, maxLines = 3)
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isDone) Matcha else Color.Transparent)
                            .border(
                                width = if (isDone) 0.dp else 2.dp,
                                color = if (isDone) Color.Transparent else Outline.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .clickable { onToggle() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(Icons.Filled.Check, "Done", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
    }
}
