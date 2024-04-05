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

    private const val CHANNEL_ID = "Default"

    fun init(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel == null) {
                val name = context.getString(R.string.default_chanel)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                mChannel.description = context.getString(R.string.notificationDescription)
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }

    fun postNotification(
        id: Int,
        context: Context,
        intent: PendingIntent,
        title: String?,
        text: String?
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        val titleText = title ?: context.getString(R.string.deepLinkNotificationTitle)
        builder.setContentTitle(titleText)
            .setSmallIcon(R.drawable.ic_movies_film)
        val textText = text ?: context.getString(R.string.notification_text)
        val notification = builder.setContentText(textText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .build()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancelAll()
        notificationManager.notify(id, notification)
    }

}