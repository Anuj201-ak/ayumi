package com.ayumi.app.ui.log

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.ayumi.app.ui.theme.CardWhite
import com.ayumi.app.ui.theme.Cream
import com.ayumi.app.ui.theme.Ink
import com.ayumi.app.ui.theme.InkSoft
import com.ayumi.app.ui.theme.Jakarta
import com.ayumi.app.ui.theme.Manrope
import com.ayumi.app.ui.theme.Matcha
import com.ayumi.app.ui.theme.PeachLight
import com.ayumi.app.ui.theme.Persimmon
import java.time.LocalDate

@Composable
fun LogScreen(vm: LogViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    var draft by remember(state.date) { mutableStateOf(state.text) }
    val wordCount = draft.trim().split(Regex("\\s+")).count { it.isNotEmpty() }
    val isToday = state.date == LocalDate.now()

    // Keep the draft in sync when the saved text changes elsewhere.
    LaunchedEffect(state.text, state.date) {
        if (draft != state.text && !state.justSaved) draft = state.text
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("Log", fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Ink)
            Text("  歩み", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                color = Matcha, modifier = Modifier.padding(bottom = 4.dp))
        }

        // Date nav + completion pill
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { vm.goToPreviousDay() }) {
                    Icon(Icons.Filled.ChevronLeft, "Previous day", tint = Ink)
                }
                Text(state.dateLabel, fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink)
                IconButton(onClick = { vm.goToNextDay() }) {
                    Icon(Icons.Filled.ChevronRight, "Next day",
                        tint = if (isToday) Ink.copy(alpha = 0.25f) else Ink)
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(PeachLight)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Check, null, tint = Matcha, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("${state.doneCount} of ${state.totalCount} activities completed",
                        fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Ink)
                }
            }
        }

        Text("日々の記録 • EVENING RECOLLECTION",
            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Matcha)
        Text(state.dateLabel,
            fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Ink)
        Text("One step at a time – reflect on today's rhythm and quiet breakthroughs.",
            fontFamily = Manrope, fontSize = 14.sp, color = InkSoft, lineHeight = 20.sp)

        // Mindful prompt
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PeachLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("MINDFUL PROMPT",
                    fontFamily = Manrope, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Persimmon)
                Spacer(Modifier.height(6.dp))
                Text("“Notice the spaces between your movements today. Where was the stillness hiding?”",
                    fontFamily = Manrope, fontSize = 14.sp, color = Ink, lineHeight = 20.sp)
            }
        }

        // Inscription editor
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
                        Icon(Icons.Filled.Edit, null, tint = Matcha, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Daily Inscription",
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Ink)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(PeachLight)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("$wordCount words written",
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Ink)
                    }
                }
                Spacer(Modifier.height(10.dp))
                TextField(
                    value = draft,
                    onValueChange = { draft = it; vm.clearSavedFlag() },
                    placeholder = { Text("What did today feel like?") },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Cream, unfocusedContainerColor = Cream,
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }

        Button(
            onClick = { vm.save(draft) },
            colors = ButtonDefaults.buttonColors(containerColor = Persimmon, contentColor = Color.White),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth().height(54.dp)
        ) {
            Icon(Icons.Filled.Check, null)
            Spacer(Modifier.width(8.dp))
            Text("Save reflection", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
        if (state.justSaved) {
            Text("Saved ✓  •  Auto-kept on this device",
                fontFamily = Manrope, fontSize = 12.sp, color = Matcha,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text("Keep in drafts • Stored only on your phone",
                fontFamily = Manrope, fontSize = 12.sp, color = InkSoft,
                modifier = Modifier.align(Alignment.CenterHorizontally))
        }
        Spacer(Modifier.height(8.dp))
    }
}
