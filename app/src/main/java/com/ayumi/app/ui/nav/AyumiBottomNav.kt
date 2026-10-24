package com.ayumi.app.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ayumi.app.ui.theme.Cream
import com.ayumi.app.ui.theme.Ink
import com.ayumi.app.ui.theme.InkSoft
import com.ayumi.app.ui.theme.Manrope
import com.ayumi.app.ui.theme.Matcha
import com.ayumi.app.ui.theme.Sand

enum class AyumiTab(val label: String, val icon: ImageVector) {
    Today("Today", Icons.Filled.CalendarMonth),
    Routine("Routine", Icons.Filled.Autorenew),
    Log("Log", Icons.Filled.ReceiptLong),
    Insights("Insights", Icons.Filled.BarChart)
}

@Composable
fun AyumiBottomNav(selected: AyumiTab, onSelect: (AyumiTab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Cream)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AyumiTab.entries.forEach { tab ->
            val isSelected = tab == selected
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelect(tab) }
                    .background(if (isSelected) Sand else Cream)
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    tint = if (isSelected) Matcha else InkSoft
                )
                Text(
                    text = tab.label,
                    fontFamily = Manrope,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 12.sp,
                    color = if (isSelected) Matcha else InkSoft
                )
            }
        }
    }
}
