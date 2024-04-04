package com.judjingm.android002.common.utill

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.judjingm.android002.R

object Notifier {

    private const val channelId = "Default"

    fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val existingChannel = notificationManager.getNotificationChannel(channelId)
            if (existingChannel == null) {
                // Create the NotificationChannel
                val name = context.getString(R.string.default_chanel)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel(channelId, name, importance)
                mChannel.description = context.getString(R.string.notificationDescription)
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }

    fun postNotification(id: Long, context: Context, intent: PendingIntent) {
        val builder = NotificationCompat.Builder(context, channelId)
        builder.setContentTitle(context.getString(R.string.deepLinkNotificationTitle))
            .setSmallIcon(R.drawable.content_cover_rv)
        val text = context.getString(R.string.notification_text)
        val notification = builder.setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancelAll()
        notificationManager.notify(id.toInt(), notification)
    }
}