package com.ayumi.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.ayumi.app.alarm.AlarmScheduler
import com.ayumi.app.ui.insights.InsightsScreen
import com.ayumi.app.ui.log.LogScreen
import com.ayumi.app.ui.nav.AyumiBottomNav
import com.ayumi.app.ui.nav.AyumiTab
import com.ayumi.app.ui.routine.RoutineScreen
import com.ayumi.app.ui.theme.AyumiTheme
import com.ayumi.app.ui.theme.Cream
import com.ayumi.app.ui.today.TodayScreen

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        // Make sure today's alarms reflect the current plan.
        AlarmScheduler.scheduleToday(this)
        setContent {
            AyumiTheme {
                AyumiApp()
            }
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AlarmScheduler.CHANNEL_ID,
            "Ayumi reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Chime when an activity starts" }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}

@Composable
fun AyumiApp() {
    var tab by rememberSaveable { mutableStateOf(AyumiTab.Today) }
    Scaffold(
        bottomBar = { AyumiBottomNav(selected = tab, onSelect = { tab = it }) },
        containerColor = Cream
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (tab) {
                AyumiTab.Today -> TodayScreen(onOpenLog = { tab = AyumiTab.Log })
                AyumiTab.Routine -> RoutineScreen()
                AyumiTab.Log -> LogScreen()
                AyumiTab.Insights -> InsightsScreen()
            }
        }
    }
}
