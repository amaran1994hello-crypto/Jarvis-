package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.model.JarvisReminder
import java.util.UUID

class ReminderNotificationHelper(private val context: Context) {

    private val channelId = "jarvis_alerts_channel"
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "JARVIS Protocols & Reminders"
            val descriptionText = "Notifications and voice reminders from J.A.R.V.I.S."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun dispatchNotification(title: String, message: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("J.A.R.V.I.S. Protocol Alert")
            .setContentText("$title: $message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    fun createInitialReminders(): List<JarvisReminder> {
        return listOf(
            JarvisReminder(
                id = UUID.randomUUID().toString(),
                title = "Review JARVIS Professional AI architecture package with Founder Amar",
                timeFormatted = "Today at 14:00",
                isCompleted = false,
                priority = "CRITICAL"
            ),
            JarvisReminder(
                id = UUID.randomUUID().toString(),
                title = "Verify Telecom Baseline & Video Calling Capabilities registration",
                timeFormatted = "Today at 16:30",
                isCompleted = true,
                priority = "HIGH"
            ),
            JarvisReminder(
                id = UUID.randomUUID().toString(),
                title = "Synchronize Google Workspace & Gmail Integration tokens",
                timeFormatted = "Tomorrow at 09:00",
                isCompleted = false,
                priority = "NORMAL"
            )
        )
    }
}
