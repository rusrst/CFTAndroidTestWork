package com.example.cftandroidtestwork

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class Notification {
    fun createChannel (channelId: String, channelName: String, context: Context){
        lateinit var notificationChannel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                setShowBadge(false)
                enableLights(true)
                lightColor = Color.GREEN
                enableVibration(true)
                description = "Time for look news"
                lockscreenVisibility = -1
            }
            val notificator = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificator.createNotificationChannel(notificationChannel)
        }
    }

    fun sendNotification(context: Context, chan_id: String, drawable: Int,
                         title: String, text: String, NOTIFICATION_ID: Int)
    {
        val intent = Intent(context, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val builder = NotificationCompat.Builder(context, chan_id)
            .setSmallIcon(drawable)
            .setContentTitle(title)
            .setContentText(text)
            //.setContentInfo(content_info)
            .setAutoCancel(true)
        val contentIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(contentIntent)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, builder.build())
    }
}