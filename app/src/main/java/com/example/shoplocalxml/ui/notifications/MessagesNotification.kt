package com.example.shoplocalxml.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.getStringResource


class MessagesNotification(val context: Context) {
    companion object {
        const val GROUP_KEY  = "SHOPLOCAL_KEY_GROUPMESSAGES"
        const val GROUP_NAME = "SHOPLOCAL_NAME_GROUPMESSAGES"
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "APP_SHOPLOCAL"
    }

    private val channelGroup: NotificationChannelGroup = NotificationChannelGroup(
        CHANNEL_ID,
        GROUP_NAME
    )

    private val channel: NotificationChannel = NotificationChannel(
        CHANNEL_ID, "ShopLocal",
        NotificationManager.IMPORTANCE_HIGH
    )
    private var messages = listOf<UserMessage>()
    /*private val notificationManager = NotificationManagerCompat.from(context)
    private val builder             = NotificationCompat.Builder(context, CHANNEL_ID)*/
    init {
        channel.description = "My channel description"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(false)
/*        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.createNotificationChannel(channel)
        val builder             = NotificationCompat.Builder(context, CHANNEL_ID)
        builder.setContentTitle("Title")
               .setContentText("Notification text")
               .setSmallIcon(R.mipmap.ic_launcher)
               /*.setContentInfo(getStringResource(R.string.messages_contentinfo))
               .setGroup(GROUP_KEY)
               .setGroupSummary(true)*/*/

    }

    fun notifyMessages(messages: List<UserMessage>){
            this.messages = messages
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
              /*  messages.forEach {
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentInfo(getStringResource(R.string.messages_contentinfo))
                        .setGroup(GROUP_KEY)
                        .setGroupSummary(true)
                    notificationManager.notify(NOTIFICATION_ID, builder.build())
                }*/

            }
    }
}