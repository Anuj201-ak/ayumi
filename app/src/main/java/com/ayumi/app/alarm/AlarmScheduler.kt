package com.ayumi.app.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ayumi.app.data.AyumiDatabase
import com.ayumi.app.data.AyumiRepository
import com.ayumi.app.data.OverrideSlot
import com.ayumi.app.data.RoutineSlot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Schedules an exact alarm 5 minutes before each of today's slots that has
 * reminders on. Re-run whenever the plan changes (routine edit, day override)
 * and on device boot.
 */
object AlarmScheduler {

    const val CHANNEL_ID = "ayumi_reminders"
    private const val PREFS = "ayumi_alarms"
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun canScheduleExactAlarms(context: Context): Boolean {
        val am = context.getSystemService(AlarmManager::class.java)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) am.canScheduleExactAlarms()
        else true
    }

    fun scheduleToday(context: Context) {
        scope.launch {
            val appContext = context.applicationContext
            cancelAll(appContext)
            if (!canScheduleExactAlarms(appContext)) return@launch

            val repo = AyumiRepository(AyumiDatabase.getDatabase(appContext))
            val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val override = repo.getDayOverride(todayStr)
            val plan: List<OverrideSlot> = override?.slots
                ?: repo.getAllRoutineSlots().map { it.toOverrideSlot() }

            val am = appContext.getSystemService(AlarmManager::class.java)
            val scheduled = mutableSetOf<String>()
            plan.filter { it.reminderOn }.forEach { slot ->
                val trigger = slotStartMillis(slot) - 5 * 60_000L
                if (trigger > System.currentTimeMillis()) {
                    val code = requestCodeFor(slot)
                    val intent = Intent(appContext, AlarmReceiver::class.java).apply {
                        putExtra("title", slot.title)
                        putExtra("start", slot.startTime)
                    }
                    val pi = PendingIntent.getBroadcast(
                        appContext, code, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, trigger, pi)
                    scheduled.add(code.toString())
                }
            }
            appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putStringSet("codes", scheduled).apply()
        }
    }

    private fun cancelAll(appContext: Context) {
        val am = appContext.getSystemService(AlarmManager::class.java)
        val codes = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getStringSet("codes", emptySet()) ?: emptySet()
        codes.forEach { s ->
            val code = s.toIntOrNull() ?: return@forEach
            val pi = PendingIntent.getBroadcast(
                appContext, code, Intent(appContext, AlarmReceiver::class.java),
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pi != null) {
                am.cancel(pi)
                pi.cancel()
            }
        }
    }

    private fun requestCodeFor(slot: OverrideSlot): Int =
        ((slot.id * 31 + slot.startTime.hashCode()) and 0x7fffffff).toInt()

    private fun slotStartMillis(slot: OverrideSlot): Long {
        val start = LocalTime.parse(slot.startTime)
        return LocalDate.now().atTime(start)
            .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    private fun RoutineSlot.toOverrideSlot() = OverrideSlot(
        id = id, title = title, startTime = startTime, endTime = endTime,
        important = important, description = description, reminderOn = reminderOn
    )
}
