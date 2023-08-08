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
import com.example.shoplocalxml.log


class MessagesNotification(val context: Context) {
    companion object {
        const val GROUP_KEY  = "SHOPLOCAL_KEY_GROUPMESSAGES"
        const val GROUP_NAME = "SHOPLOCAL_NAME_GROUPMESSAGES"
        const val NOTIFICATION_ID = 101
        const val NOTIFICATION_GROUP_ID = -101
        const val CHANNEL_ID        = "APP_CHANEL_ID"
        const val CHANNEL_GROUP_ID  = "APP_CHANEL_GROUP_ID"
    }

  /*  private val channelGroup: NotificationChannelGroup = NotificationChannelGroup(
        CHANNEL_GROUP_ID,
        GROUP_NAME
    )*/

   /* private val channel: NotificationChannel = NotificationChannel(
        CHANNEL_ID, "ShopLocal",
        NotificationManager.IMPORTANCE_HIGH
    )*/
    private var messages = listOf<UserMessage>()
    /*private val notificationManager = NotificationManagerCompat.from(context)
    private val builder             = NotificationCompat.Builder(context, CHANNEL_ID)*/
    init {
       /* channel.description = "My channel description"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(false)
        channel.group = CHANNEL_GROUP_ID*/
        if (permissionGranted()) {
            val channelGroup: NotificationChannelGroup = NotificationChannelGroup(
                CHANNEL_GROUP_ID,
                GROUP_NAME
            )
           // log("channel name = ${channelGroup.id}")
            val notificationManagerGroup = NotificationManagerCompat.from(context)
            notificationManagerGroup.createNotificationChannelGroup(channelGroup)
            val notification = NotificationCompat.Builder(context, CHANNEL_GROUP_ID)
                .setContentTitle("Title")
                .setContentText("Notification text")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentInfo(getStringResource(R.string.messages_contentinfo))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setChannelId(CHANNEL_GROUP_ID)
                .build()

            notificationManagerGroup.notify(NOTIFICATION_GROUP_ID, notification)
        }

    }

    fun notifyMessages(messages: List<UserMessage>){
            this.messages = messages
            /*if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )*/
        if (permissionGranted()) {
            val channel = NotificationChannel(
                CHANNEL_ID, "ShopLocal",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            channel.group = CHANNEL_GROUP_ID

            NotificationManagerCompat.from(context).apply {
                for (i in messages.indices) {
                    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                       // .setContentInfo(getStringResource(R.string.messages_contentinfo))
                        .setContentTitle("Sender $i")
                        .setContentText("Subject text $i")
                        .setGroup(GROUP_KEY)
                        .setGroupSummary(false)
                        .setChannelId(CHANNEL_ID)
                        .build()
                    notify(NOTIFICATION_ID, notification)
                }
            }
        }
    }

    private fun permissionGranted() =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
}