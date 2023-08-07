package com.example.shoplocalxml.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.classes.UserMessage


class MessagesNotification(val context: Context) {
    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "APP_SHOPLOCAL"
    }
    private var messages = listOf<UserMessage>()
    private val notificationManager = NotificationManagerCompat.from(context)
    private val builder             = NotificationCompat.Builder(context, CHANNEL_ID)
    init {
        val channel = NotificationChannel(
            CHANNEL_ID, "ShopLocal",
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = "My channel description"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
        builder.setContentTitle("Title")
               .setContentText("Notification text")
              //.setSmallIcon(R.mipmap.ic_launcher)

    }

    fun notifyMessages(messages: List<UserMessage>){
        this.messages = messages
        if (ActivityCompat.checkSelfPermission(context,
                                               Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        )
            notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}