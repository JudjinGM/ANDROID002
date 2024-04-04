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
            val title = message.data[TITLE]
            val text = message.data[MESSAGE]

            if (deepLink != null) {
                handleDeepLink(deepLink, title, text)
                return
            }
        }
        super.onMessageReceived(message)
    }

    private fun handleDeepLink(deepLink: String, title: String?, text: String?) {
        Notifier.init(this)

        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE,
            Intent(Intent.ACTION_VIEW, Uri.parse(deepLink))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_CANCEL_CURRENT
        )

        Notifier.postNotification(NOTIFICATION_ID, this, pendingIntent, title, text)
    }

    companion object {
        private const val DEEP_LINK = "deeplink"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val REQUEST_CODE = 0
        private const val NOTIFICATION_ID = 1
    }
}
