package com.ayumi.app.ui.insights

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
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
import com.ayumi.app.ui.theme.Mint
import com.ayumi.app.ui.theme.PeachLight
import com.ayumi.app.ui.theme.Persimmon
import com.ayumi.app.ui.theme.SandDeep
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun InsightsScreen(vm: InsightsViewModel = viewModel()) {
    val range by vm.range.collectAsState()
    val data by vm.data.collectAsState()
    val exporting by vm.exporting.collectAsState()

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
            Text("Insights", fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 26.sp, color = Ink)
            Text("  歩み", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 15.sp,
                color = Matcha, modifier = Modifier.padding(bottom = 4.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("QUIET METRICS",
                    fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = Matcha)
                Text("Progress & Insights",
                    fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Ink)
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(PeachLight)
                    .padding(4.dp)
            ) {
                StatsRange.entries.forEach { r ->
                    val selected = r == range
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selected) Persimmon else Color.Transparent)
                            .clickable { vm.setRange(r) }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            r.name,
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                            color = if (selected) Color.White else Ink
                        )
                    }
                }
            }
        }
        Text("A gentle mirror of your movement, honoring rhythm over rigidity.",
            fontFamily = Manrope, fontSize = 13.sp, color = InkSoft)

        val d = data
        if (d == null) {
            Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Persimmon)
            }
        } else {
            // Stat cards
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "Completion",
                        value = "${d.completionPct}%",
                        valueColor = Persimmon,
                        subtitle = "${d.doneTotal} of ${d.plannedTotal} slots",
                        icon = Icons.Filled.Star,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Early",
                        value = "${d.early}",
                        valueColor = Ink,
                        subtitle = "finished ahead",
                        icon = Icons.Filled.Spa,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        title = "On time",
                        value = "${d.onTime}",
                        valueColor = Ink,
                        subtitle = "steady rhythm",
                        icon = Icons.Filled.Schedule,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Late / Missed",
                        value = "${d.late + d.missed}",
                        valueColor = Ink,
                        subtitle = "gentle pause",
                        icon = Icons.Filled.Bedtime,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Rhythm chart
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
                        Column {
                            Text(if (range == StatsRange.Week) "Weekly Rhythm" else "Monthly Rhythm",
                                fontFamily = Jakarta, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Ink)
                            Text("Daily completion",
                                fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(8.dp).clip(CircleShape).background(Persimmon))
                            Spacer(Modifier.width(4.dp))
                            Text("Actual", fontFamily = Manrope, fontSize = 11.sp, color = InkSoft)
                            Spacer(Modifier.width(10.dp))
                            Box(Modifier.size(8.dp).clip(CircleShape).background(Matcha))
                            Spacer(Modifier.width(4.dp))
                            Text("Peak", fontFamily = Manrope, fontSize = 11.sp, color = InkSoft)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    RhythmChart(d.days.map { day ->
                        val total = day.slots.size
                        val doneN = day.slots.count { it.diffMin != null }
                        Pair(
                            day.date.format(DateTimeFormatter.ofPattern("E", Locale.ENGLISH)).take(3),
                            if (total == 0) 0f else doneN.toFloat() / total
                        )
                    })
                }
            }

            // Mindful note
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Matcha),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Spa, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Daily Mindful Note",
                            fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White)
                        Text("“Stillness is where balance begins.”",
                            fontFamily = Manrope, fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                }
            }

            // Consistency
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Routine Consistency",
                        fontFamily = Jakarta, fontWeight = FontWeight.SemiBold, fontSize = 17.sp, color = Ink)
                    Text("Core practices over the last ${range.days} days",
                        fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                    Spacer(Modifier.height(12.dp))
                    if (d.consistency.isEmpty()) {
                        Text("No routine slots yet — add them in the Routine tab.",
                            fontFamily = Manrope, fontSize = 13.sp, color = InkSoft)
                    } else {
                        d.consistency.forEachIndexed { index, c ->
                            val pct = if (c.daysTotal == 0) 0 else (c.daysDone * 100 / c.daysTotal)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (c.important) {
                                    Icon(Icons.Filled.Star, null, tint = Matcha, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                }
                                Text(c.title,
                                    fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Ink,
                                    modifier = Modifier.weight(1f), maxLines = 1)
                                Text("${c.daysDone}/${c.daysTotal} days  $pct%",
                                    fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
                            }
                            Spacer(Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { if (c.daysTotal == 0) 0f else c.daysDone.toFloat() / c.daysTotal },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = if (index % 2 == 0) Matcha else Persimmon,
                                trackColor = PeachLight
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }

            // Export
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { vm.exportExcel() },
                    enabled = !exporting,
                    colors = ButtonDefaults.buttonColors(containerColor = PeachLight, contentColor = Ink),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Icon(Icons.Filled.TableChart, null, tint = Matcha)
                    Spacer(Modifier.width(8.dp))
                    Text("Export Excel", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
                Button(
                    onClick = { vm.exportPdf() },
                    enabled = !exporting,
                    colors = ButtonDefaults.buttonColors(containerColor = Persimmon, contentColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    Icon(Icons.Filled.PictureAsPdf, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Export PDF", fontFamily = Manrope, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
            if (exporting) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(color = Persimmon, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Preparing file…", fontFamily = Manrope, fontSize = 13.sp, color = InkSoft)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    valueColor: Color,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontFamily = Manrope, fontSize = 13.sp, color = InkSoft)
                Icon(icon, null, tint = Persimmon, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.height(6.dp))
            Text(value, fontFamily = Jakarta, fontWeight = FontWeight.Bold, fontSize = 32.sp, color = valueColor)
            Text(subtitle, fontFamily = Manrope, fontSize = 12.sp, color = InkSoft)
        }
    }
}

@Composable
private fun RhythmChart(bars: List<Pair<String, Float>>) {
    val maxV = (bars.maxOfOrNull { it.second } ?: 0f).coerceAtLeast(0.01f)
    val peakIndex = bars.indexOfFirst { it.second == bars.maxOfOrNull { b -> b.second } }
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (bars.size > 10) 150.dp else 170.dp)
    ) {
        val barCount = bars.size
        val slotW = size.width / barCount
        val barW = (slotW * 0.52f).coerceAtMost(36.dp.toPx())
        val chartH = size.height - 28.dp.toPx()
        bars.forEachIndexed { i, (label, v) ->
            val h = (v / maxV) * chartH
            val x = slotW * i + (slotW - barW) / 2
            val y = chartH - h
            drawRoundRect(
                color = if (v >= 1f) Persimmon else Persimmon.copy(alpha = 0.75f),
                topLeft = Offset(x, y),
                size = Size(barW, h.coerceAtLeast(4.dp.toPx())),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            if (i == peakIndex && v > 0f) {
                drawCircle(
                    color = Matcha,
                    radius = 6.dp.toPx(),
                    center = Offset(x + barW / 2, y - 12.dp.toPx())
                )
            }
            if (barCount <= 10 || i % 5 == 0 || i == barCount - 1) {
                val layout = textMeasurer.measure(
                    text = label,
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = Manrope,
                        color = InkSoft,
                        textAlign = TextAlign.Center
                    )
                )
                drawText(
                    textLayoutResult = layout,
                    topLeft = Offset(
                        x + barW / 2 - layout.size.width / 2,
                        size.height - 4.dp.toPx() - layout.size.height
                    )
                )
            }
        }
    }
}
