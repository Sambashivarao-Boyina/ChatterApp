package com.example.chatterapp

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.chatterapp.data.remote.Dto.UpdateData
import com.example.chatterapp.domain.repository.ChatterRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Random
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationSerivce: FirebaseMessagingService() {
    @Inject
    lateinit var chatterRepository: ChatterRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendTokenToBackend(token)
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    fun showNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random().nextInt(1000)

        val notification = NotificationCompat.Builder(this, "message")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
        notificationManager.notify(notificationId, notification)
    }

    private fun sendTokenToBackend(token: String) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
               chatterRepository.saveFcmToken(UpdateData(token))

            } catch (e: Exception) {
                Log.e("FCM", "Error sending token to backend: ${e.message}")
            }
        }
    }

}

