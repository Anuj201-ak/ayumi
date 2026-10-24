package com.ayumi.app.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ayumi.app.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Activity"
        val start = intent.getStringExtra("start") ?: ""
        val openIntent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            context, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = Notification.Builder(context, AlarmScheduler.CHANNEL_ID)
            .setContentTitle("Ayumi 歩み — starting in 5 min")
            .setContentText("$title ($start)")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()
        context.getSystemService(NotificationManager::class.java)
            .notify(title.hashCode(), notification)
    }
}
