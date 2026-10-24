package com.ayumi.app.ui.day

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.ayumi.app.data.OverrideSlot
import com.ayumi.app.ui.theme.CardWhite
import com.ayumi.app.ui.theme.Cream
import com.ayumi.app.ui.theme.Ink
import com.ayumi.app.ui.theme.InkSoft
import com.ayumi.app.ui.theme.Jakarta
import com.ayumi.app.ui.theme.Manrope
import com.ayumi.app.ui.theme.Matcha
import com.ayumi.app.ui.theme.PeachLight
import com.ayumi.app.ui.theme.Persimmon

data class EditableSlot(
    val id: Long,
    var title: String,
    var startTime: String,
    var endTime: String,
    var important: Boolean,
    var description: String = "",
    var reminderOn: Boolean = true
)

private val TIME_REGEX = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayOverrideSheet(
    initialSlots: List<OverrideSlot>,
    dateLabel: String,
    hasOverride: Boolean,
    onSave: (List<OverrideSlot>) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    var rows by remember {
        mutableStateOf(
            initialSlots.map {
                EditableSlot(it.id, it.title, it.startTime, it.endTime, it.important, it.description, it.reminderOn)
            }
        )
    }
    var error by remember { mutableStateOf<String?>(null) }

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
                Column {
                    Text("Customize Day", fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Ink)
                    Text(dateLabel + " · default routine stays untouched",
                        fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                }
                IconButton(onClick = onDismiss,
                    modifier = Modifier.clip(androidx.compose.foundation.shape.CircleShape).background(PeachLight)) {
                    Icon(Icons.Filled.Close, "Close", tint = Ink)
                }
            }
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.heightIn(max = 420.dp)
            ) {
                itemsIndexed(rows, key = { index, _ -> index }) { index, row ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(1.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = row.title,
                                    onValueChange = {
                                        rows = rows.toMutableList().also { l -> l[index] = row.copy(title = it) }
                                    },
                                    label = { Text("Activity") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    rows = rows.toMutableList().also { l -> l[index] = row.copy(important = !row.important) }
                                }) {
                                    Icon(
                                        if (row.important) Icons.Filled.Star else Icons.Filled.StarBorder,
                                        "Important", tint = Matcha
                                    )
                                }
                                IconButton(onClick = {
                                    rows = rows.toMutableList().also { l -> l.removeAt(index) }
                                }) {
                                    Icon(Icons.Filled.Delete, "Remove", tint = Persimmon)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = row.startTime,
                                    onValueChange = {
                                        rows = rows.toMutableList().also { l -> l[index] = row.copy(startTime = it) }
                                    },
                                    label = { Text("Start (HH:mm)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = row.endTime,
                                    onValueChange = {
                                        rows = rows.toMutableList().also { l -> l[index] = row.copy(endTime = it) }
                                    },
                                    label = { Text("End (HH:mm)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
                item {
                    Text(
                        "+ Add activity",
                        fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Persimmon,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                rows = rows + EditableSlot(
                                    id = -System.nanoTime(),
                                    title = "New activity",
                                    startTime = "09:00",
                                    endTime = "10:00",
                                    important = false
                                )
                            }
                            .padding(8.dp)
                    )
                }
            }

            error?.let {
                Text(it, fontFamily = Manrope, fontSize = 12.sp, color = Persimmon,
                    modifier = Modifier.padding(vertical = 4.dp))
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    val bad = rows.indexOfFirst { !TIME_REGEX.matches(it.startTime) || !TIME_REGEX.matches(it.endTime) }
                    if (bad >= 0) {
                        error = "Row ${bad + 1}: use HH:mm format (e.g. 09:00)."
                        return@Button
                    }
                    if (rows.isEmpty()) {
                        error = "Add at least one activity, or reset to default."
                        return@Button
                    }
                    onSave(rows.map {
                        OverrideSlot(
                            id = it.id,
                            title = it.title.ifBlank { "Untitled" },
                            startTime = it.startTime, endTime = it.endTime,
                            important = it.important, description = it.description,
                            reminderOn = it.reminderOn
                        )
                    })
                },
                colors = ButtonDefaults.buttonColors(containerColor = Persimmon, contentColor = Color.White),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Icon(Icons.Filled.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Save for this day only", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
            if (hasOverride) {
                TextButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
                    Text("Reset to default routine", fontFamily = Manrope, fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp, color = Persimmon)
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
