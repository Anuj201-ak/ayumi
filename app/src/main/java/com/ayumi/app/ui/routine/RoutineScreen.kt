package com.ayumi.app.ui.routine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ayumi.app.data.RoutineSlot
import com.ayumi.app.ui.theme.CardWhite
import com.ayumi.app.ui.theme.Cream
import com.ayumi.app.ui.theme.Ink
import com.ayumi.app.ui.theme.InkSoft
import com.ayumi.app.ui.theme.Jakarta
import com.ayumi.app.ui.theme.Manrope
import com.ayumi.app.ui.theme.Matcha
import com.ayumi.app.ui.theme.Mint
import com.ayumi.app.ui.theme.PeachLight
import com.ayumi.app.ui.theme.Persimmon
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineScreen(vm: RoutineViewModel = viewModel()) {
    val slots by vm.slots.collectAsState()
    var editing by remember { mutableStateOf<RoutineSlot?>(null) }
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Cream,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { editing = null; showSheet = true },
                containerColor = Persimmon,
                contentColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(Icons.Filled.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Add slot", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text("Routine", fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Ink)
                    Text("  歩み", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                        color = Matcha, modifier = Modifier.padding(bottom = 4.dp))
                }
                Spacer(Modifier.height(10.dp))
                Text("Master Blueprint", fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Ink)
                Text("Your default flow – changes won't disrupt past days.",
                    fontFamily = Manrope, fontSize = 13.sp, color = InkSoft)
            }

            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = PeachLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Icon(Icons.Filled.Info, null, tint = Persimmon, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("Every dawn begins anew",
                                fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                            Text("This template populates each day at midnight. Customize any individual day in “Today” without altering this master rhythm.",
                                fontFamily = Manrope, fontSize = 13.sp, color = InkSoft, lineHeight = 18.sp)
                        }
                    }
                }
            }

            items(slots, key = { it.id }) { slot ->
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(PeachLight)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text("${slot.startTime} – ${slot.endTime}",
                                        fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Ink)
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(durationOf(slot), fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                                if (slot.important) {
                                    Spacer(Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Mint)
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("★ Important",
                                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Matcha)
                                    }
                                }
                            }
                            Spacer(Modifier.height(6.dp))
                            Text(slot.title,
                                fontFamily = Jakarta, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Ink,
                                maxLines = 1)
                        }
                        IconButton(onClick = { editing = slot; showSheet = true }) {
                            Icon(Icons.Filled.Edit, "Edit", tint = InkSoft)
                        }
                        IconButton(onClick = { vm.deleteSlot(slot) }) {
                            Icon(Icons.Filled.Delete, "Delete", tint = InkSoft)
                        }
                    }
                }
            }

            item {
                Text(
                    "${slots.size} INTENTIONAL BLOCKS • ${vm.totalScheduled(slots)} SCHEDULED",
                    fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = InkSoft,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                )
                Spacer(Modifier.height(80.dp))
            }
        }
    }

    if (showSheet) {
        SlotEditSheet(
            existing = editing,
            onSave = { vm.saveSlot(it); showSheet = false },
            onDismiss = { showSheet = false }
        )
    }
}

private fun durationOf(slot: RoutineSlot): String {
    val s = LocalTime.parse(slot.startTime)
    val e = LocalTime.parse(slot.endTime)
    var m = Duration.between(s, e).toMinutes()
    if (m <= 0) m += 24 * 60
    return if (m >= 60) "${m / 60}h ${m % 60}m" else "${m}m"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SlotEditSheet(
    existing: RoutineSlot?,
    onSave: (RoutineSlot) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(existing?.title ?: "") }
    var start by remember { mutableStateOf(existing?.let { LocalTime.parse(it.startTime) } ?: LocalTime.of(9, 0)) }
    var end by remember { mutableStateOf(existing?.let { LocalTime.parse(it.endTime) } ?: LocalTime.of(10, 30)) }
    var important by remember { mutableStateOf(existing?.important ?: false) }
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var reminderOn by remember { mutableStateOf(existing?.reminderOn ?: true) }
    var pickingStart by remember { mutableStateOf(false) }
    var pickingEnd by remember { mutableStateOf(false) }

    var durMin = Duration.between(start, end).toMinutes()
    if (durMin <= 0) durMin += 24 * 60
    val durText = if (durMin >= 60) "Duration: ${durMin / 60}h ${durMin % 60}m" else "Duration: ${durMin}m"

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Cream
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.foundation.layout.Box(
                        Modifier.size(10.dp).clip(CircleShape).background(Matcha)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (existing == null) "Add activity" else "Edit activity",
                        fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Ink)
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.clip(CircleShape).background(PeachLight).size(36.dp)
                ) { Icon(Icons.Filled.Close, "Close", tint = Ink) }
            }
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Activity title", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                Text("Mindful flow", fontFamily = Manrope, fontSize = 12.sp, color = Matcha)
            }
            Spacer(Modifier.height(6.dp))
            TextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("e.g. Deep work & tea ceremony") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = PeachLight, unfocusedContainerColor = PeachLight,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Time allocation", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Mint)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(durText, fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Matcha)
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TimeBox(label = "Start time", time = start, onClick = { pickingStart = true }, modifier = Modifier.weight(1f))
                TimeBox(label = "End time", time = end, onClick = { pickingEnd = true }, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(14.dp))

            // Important toggle
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PeachLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("★ Important activity",
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Ink)
                        Text("Highlights with a matcha star and prioritizes in timeline.",
                            fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                    }
                    Switch(
                        checked = important,
                        onCheckedChange = { important = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Matcha)
                    )
                }
            }
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Focus notes / Description", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                Text("Optional quiet cue", fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
            }
            Spacer(Modifier.height(6.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("e.g. Protect this window from meetings.") },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = PeachLight, unfocusedContainerColor = PeachLight,
                    focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth().height(88.dp)
            )
            Spacer(Modifier.height(14.dp))

            // Reminder toggle
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PeachLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Notifications, null, tint = Persimmon,
                        modifier = Modifier.clip(CircleShape).background(Color.White).padding(8.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Reminder chime",
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Ink)
                        Text("5 min before start",
                            fontFamily = Manrope, fontSize = 12.sp, color = Persimmon)
                    }
                    Switch(
                        checked = reminderOn,
                        onCheckedChange = { reminderOn = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Persimmon)
                    )
                }
            }
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val fmt = DateTimeFormatter.ofPattern("HH:mm")
                    onSave(
                        RoutineSlot(
                            id = existing?.id ?: 0,
                            title = title.ifBlank { "Untitled" },
                            startTime = start.format(fmt),
                            endTime = end.format(fmt),
                            important = important,
                            description = description,
                            reminderOn = reminderOn
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Persimmon, contentColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().height(54.dp)
            ) {
                Icon(Icons.Filled.Check, null)
                Spacer(Modifier.width(8.dp))
                Text("Save activity", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel and discard", fontFamily = Manrope, fontSize = 14.sp, color = InkSoft)
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (pickingStart) {
        val tpState = rememberTimePickerState(initialHour = start.hour, initialMinute = start.minute, is24Hour = false)
        TimePickerDialog(
            onDismissRequest = { pickingStart = false },
            title = { Text("Start time") },
            confirmButton = {
                TextButton(onClick = {
                    start = LocalTime.of(tpState.hour, tpState.minute)
                    pickingStart = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { pickingStart = false }) { Text("Cancel") }
            }
        ) { TimePicker(state = tpState) }
    }
    if (pickingEnd) {
        val tpState = rememberTimePickerState(initialHour = end.hour, initialMinute = end.minute, is24Hour = false)
        TimePickerDialog(
            onDismissRequest = { pickingEnd = false },
            title = { Text("End time") },
            confirmButton = {
                TextButton(onClick = {
                    end = LocalTime.of(tpState.hour, tpState.minute)
                    pickingEnd = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { pickingEnd = false }) { Text("Cancel") }
            }
        ) { TimePicker(state = tpState) }
    }
}

@Composable
private fun TimeBox(label: String, time: LocalTime, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val display = time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(PeachLight)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Text(label, fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(display, fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Ink,
                modifier = Modifier.weight(1f))
            Icon(Icons.Filled.Schedule, null, tint = InkSoft)
        }
    }
}
