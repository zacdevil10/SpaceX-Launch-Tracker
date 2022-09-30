package uk.co.zac_h.spacex.utils.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        when (remoteMessage.data["tag"]) {
            "next" -> NextLaunchNotification.notify(this, remoteMessage.data["message"], 1)
        }
    }
}
