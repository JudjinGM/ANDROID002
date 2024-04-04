package com.judjingm.android002.app

import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import app.cashadvisor.common.utill.extensions.logDebugMessage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.judjingm.android002.common.utill.Notifier

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        logDebugMessage("Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {

        if (message.data.isNotEmpty()) {
            val deepLink = message.data[DEEP_LINK]

            if (deepLink != null) {
                handleDeepLink(deepLink)
                return
            }
        }
        super.onMessageReceived(message)
    }

    private fun handleDeepLink(deepLink: String) {
        Notifier.init(this)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        Notifier.postNotification(1, this, pendingIntent)
    }


    companion object {
        private const val DEEP_LINK = "deeplink"
    }
}
