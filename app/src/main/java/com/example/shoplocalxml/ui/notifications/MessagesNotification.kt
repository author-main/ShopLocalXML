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
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log


class MessagesNotification(val context: Context) {
    companion object {

        const val GROUP_KEY = "GROUP_KEY"
        const val GROUP_NAME = "GROUP_MESSAGES"
        //const val NOTIFICATION_ID = 101
        const val NOTIFICATION_GROUP_ID = -101
        const val CHANNEL_ID  = "CHANNEL_ID"
        const val CHANNEL_GROUP_ID  = "CHANNEL_GROUP_ID"

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

        if (permissionGranted()) {

                val channelGroup: NotificationChannelGroup = NotificationChannelGroup(
                    CHANNEL_GROUP_ID,
                    GROUP_NAME
                )

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.createNotificationChannelGroup(channelGroup)

                val notificationGroup = NotificationCompat.Builder(context, CHANNEL_GROUP_ID)
                    .setContentTitle("Title")
                    .setContentText("Notification text")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentInfo(getStringResource(R.string.messages_contentinfo))
                    .setGroup(GROUP_KEY)
                    .setGroupSummary(true)
                    .setChannelId(CHANNEL_GROUP_ID)
                    .build()
                //notificationManager.notify(NOTIFICATION_GROUP_ID, notificationGroup)



            val channel = NotificationChannel(
                CHANNEL_ID, "ShopLocal",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            channel.group = CHANNEL_GROUP_ID
            notificationManager.createNotificationChannel(channel)

           notificationManager.apply {
                for (i in messages.indices) {
                    val notificationId = 1000 + messages[i].id
                    val notificationMessage = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Sender $i")
                        .setContentText("Subject text $i")
                        .setGroup(GROUP_KEY)
                        .setChannelId(CHANNEL_ID)
                        .build()
                    notify(notificationId, notificationMessage)
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


/*
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private static final String GROUP_ID_SMS = "group_id_sms";
private static final String GROUP_ID_PROMOTION = "promotion_id_sms";

private static final String SMS_CHANNEL_ID = "sms_channel_id";
private static final String PICTURE_CHANNEL_ID = "picture_channel_id";
private static final String PROMOTION_CHANNEL_ID = "promotion_channel_id";

private static int NOTIFICATION_ID;
private static final int PRIORITY_DEFAULT = NotificationManager.IMPORTANCE_DEFAULT;
private NotificationManager mNotificationManager;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.createNotificationChannelGroup(
        new NotificationChannelGroup(GROUP_ID_SMS, getString(R.string.SMS)));
    mNotificationManager.createNotificationChannelGroup(
        new NotificationChannelGroup(GROUP_ID_PROMOTION, getString(R.string.Promotions)));


    NotificationChannel smsNotificationChannel = new NotificationChannel(SMS_CHANNEL_ID,
        getString(R.string.sms_channel_name), PRIORITY_DEFAULT);
    NotificationChannel pictureNotificationChannel = new NotificationChannel(PICTURE_CHANNEL_ID,
        getString(R.string.picture_channel_name), PRIORITY_DEFAULT);
    pictureNotificationChannel.setGroup(GROUP_ID_SMS);
    smsNotificationChannel.setGroup(GROUP_ID_SMS);


    NotificationChannel promotionNotificationChannel = new NotificationChannel(PROMOTION_CHANNEL_ID,
        getString(R.string.ads_channel_name), PRIORITY_DEFAULT);
    promotionNotificationChannel.setGroup(GROUP_ID_PROMOTION);


    mNotificationManager.createNotificationChannel(smsNotificationChannel);
    mNotificationManager.createNotificationChannel(pictureNotificationChannel);
    mNotificationManager.createNotificationChannel(promotionNotificationChannel);


    findViewById(R.id.send_sms_notification).setOnClickListener(this);
    findViewById(R.id.send_picture_notification).setOnClickListener(this);
    findViewById(R.id.send_promotion_notification).setOnClickListener(this);
}

@Override
public void onClick(View view) {
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this)
        .setSmallIcon(R.drawable.ic_action_android);
    switch (view.getId()) {
        case R.id.send_sms_notification:
        mBuilder.setContentTitle(getString(R.string.sms_notification_title))
            .setContentText(getString(R.string.sms_notification_content)).setChannelId(SMS_CHANNEL_ID);
        NOTIFICATION_ID = 1234;
        break;
        case R.id.send_picture_notification:
        mBuilder.setContentTitle(getString(R.string.picture_notification_title))
            .setContentText(getString(R.string.picture_notification_content))
            .setChannelId(PICTURE_CHANNEL_ID);
        NOTIFICATION_ID = 2345;
        break;
        case R.id.send_promotion_notification:
        mBuilder.setContentTitle(getString(R.string.promotion_notification_title))
            .setContentText(getString(R.string.promotion_notification_content))
            .setChannelId(PROMOTION_CHANNEL_ID);
        NOTIFICATION_ID = 3456;
        break;
    }

    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
}
} */