package com.example.pdm_tg

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

const val CHANNEL_ID: String = "com.example.pdm_tg.reminders.notifications"

/**
 * This class is responsible for receiving broadcasts
 * fired by the AlarmManager service for all reminders
 * in this app. Once a reminder is fired it will contain
 * the task name and it will be sent through a notification
 * to the user.
 */
class ReminderReceiver : BroadcastReceiver() {
    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve the string from the intent that came from the fired Alarm.
        val taskName = intent.extras?.getCharSequence("taskName")
            ?.let { StringBuilder(it).toString() }

        // Build a notification.
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.todo_list)
            .setContentTitle(context.resources.getString(R.string.taskReminder))
            .setContentText(taskName)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(taskName)
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)

        // Get the notification manager service.
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a channel for our notifications.
        createNotificationChannel(context)

        // Check if we have permissions to send notifications (Android 13+).
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("ReminderReceiver", "Lacking permission to send notifications.")
            return
        }

        // Send the notification.
        notificationManager.notify(1, notification.build())
    }

    /**
     * Create a notification channel to send
     * notifications through.
     *
     * @param context The context to work with.
     */
    private fun createNotificationChannel(context: Context) {
        // https://developer.android.com/develop/ui/views/notifications/build-notification
        val name = context.resources.getString(R.string.app_name)
        val descriptionText = context.resources.getString(R.string.app_name)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system.
        notificationManager.createNotificationChannel(channel)
    }
}
