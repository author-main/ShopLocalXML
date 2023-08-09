package com.example.shoplocalxml.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
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
        const val NOTIFICATION_GROUP_ID = 102
        const val CHANNEL_ID       = "APP_SHOPLOCAL"
        const val CHANNEL_ID_GROUP = "APP_SHOPLOCAL_GROUP"
    }

    private val channelGroup: NotificationChannelGroup = NotificationChannelGroup(
        CHANNEL_ID_GROUP,
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
        val notificationManagerGroup = NotificationManagerCompat.from(context)
        notificationManagerGroup.createNotificationChannelGroup(channelGroup)
        val builderGroup            = NotificationCompat.Builder(context, CHANNEL_ID_GROUP)
        builderGroup.setContentTitle("Title")
               .setContentText("Notification text")
               .setSmallIcon(R.mipmap.ic_launcher)
               .setContentInfo(getStringResource(R.string.messages_contentinfo))
               .setGroup(GROUP_KEY)
               .setGroupSummary(true)
        notificationManagerGroup.notify(NOTIFICATION_GROUP_ID, builderGroup.build())

        channel.description = "My channel description"
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(false)
        channel.group = CHANNEL_ID_GROUP

    }

    fun notifyMessages(messages: List<UserMessage>){
            this.messages = messages
            if (permissionGranted()) {
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.createNotificationChannel(channel)
                val builder             = NotificationCompat.Builder(context, CHANNEL_ID)
                for (i in messages.indices) {
                    log("i=$i")
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Sender $i")
                        .setContentText("Subject text $i")
                        .setGroup(GROUP_KEY)
                    notificationManager.notify(NOTIFICATION_ID, builder.build())
               }

            }
    }

    private fun permissionGranted() =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

}