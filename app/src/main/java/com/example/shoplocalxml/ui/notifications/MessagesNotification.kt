package com.example.shoplocalxml.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
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
import com.example.shoplocalxml.ui.user_messages.UserMessagesActivity
import com.google.gson.Gson


class MessagesNotification(val context: Context) {
    companion object {

        const val GROUP_KEY = "GROUP_KEY"
        //const val GROUP_NAME = "GROUP_MESSAGES"
        const val NOTIFICATION_ID = 101
        const val NOTIFICATION_GROUP_ID = -101
        const val CHANNEL_ID  = "CHANNEL_ID"
        const val CHANNEL_NAME  = "CHANNEL_NAME"
        // const val CHANNEL_GROUP_ID  = "CHANNEL_GROUP_ID"

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

    fun notifyMessages(messages: List<UserMessage>){
        this.messages = messages

        if (permissionGranted() && messages.isNotEmpty()) {

            val notificationManager = NotificationManagerCompat.from(context)

            /* val channelGroup = NotificationChannelGroup(
                 CHANNEL_GROUP_ID,
                 GROUP_NAME
             )*/

            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = getStringResource(R.string.messages_contentinfo)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            //channel.group = CHANNEL_GROUP_ID

            //notificationManager.createNotificationChannelGroup(channelGroup)


            val intent = Intent(context, UserMessagesActivity::class.java)
            val gson = Gson()
            var messagesJson = gson.toJson(messages)
            intent.putExtra("messages", messagesJson)
            intent.putExtra("notification",    1)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)



            /*val intentMessage = Intent(context, UserMessagesActivity::class.java)
            intent.putExtra("index",    -1)
            val pendingIntentMessage = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)*/


            notificationManager.createNotificationChannel(channel)

            val notificationGroup = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("ShopLocal")
                .setContentText(getStringResource(R.string.messages_contentinfo))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentInfo(getStringResource(R.string.messages_contentinfo))
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                //.setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.apply {
                val listOneMessage = mutableListOf<UserMessage>()
                for (i in messages.indices) {
                    val notificationId = NOTIFICATION_ID + messages[i].id
                    val intentMessage = Intent(context, UserMessagesActivity::class.java)
                    listOneMessage.clear()
                    listOneMessage.add(messages[i])
                    messagesJson = gson.toJson(listOneMessage)
                    intentMessage.putExtra("messages", messagesJson)
                    intentMessage.putExtra("notification",    1)
                    val pendingIntentMessage = PendingIntent.getActivity(context, notificationId, intentMessage,
                        PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
                    val notificationMessage = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(messages[i].date)
                        .setContentText(messages[i].message)
                        .setGroup(GROUP_KEY)
                        //.setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntentMessage)
                        .setAutoCancel(true)
                        .build()
                    notify(notificationId, notificationMessage)
                }
                listOneMessage.clear()
                notify(NOTIFICATION_GROUP_ID, notificationGroup)
            }

        }
    }

    private fun permissionGranted() =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
}