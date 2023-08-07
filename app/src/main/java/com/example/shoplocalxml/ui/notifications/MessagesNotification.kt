package com.example.shoplocalxml.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import androidx.core.app.NotificationManagerCompat
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.classes.UserMessage


class MessagesNotification(private val messages: List<UserMessage>) {
    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "APP_SHOPLOCAL"
    }
    init {
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        val channel = NotificationChannel(
            CHANNEL_ID, "My channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "My channel description"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(false)

    }
    fun notify(){

    }
}