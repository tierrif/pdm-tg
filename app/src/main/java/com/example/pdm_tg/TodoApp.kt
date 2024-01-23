package com.example.pdm_tg

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

const val CHANNEL_ID: String = "notificationsTODOAPP"
class TodoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            CHANNEL_ID,
            "High priority notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}