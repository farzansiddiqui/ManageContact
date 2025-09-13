package com.siddiqui.myapplication.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    companion object{
        const val CHANNEL_ID = "recording_chanel"
        const val CHANNEL_NAME = "Recording Service Channel"
        const val NOTIFICATION_ID = 101

    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "used for recording service notification"
                enableLights(true)
                enableVibration(true)
                lightColor = Color.BLUE
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    fun buildNotification(title:String,message: String): Notification {
        return NotificationCompat.Builder(context,CHANNEL_ID)
            .setContentTitle(title).setContentText(message)
            .setSmallIcon(android.R.drawable.ic_btn_speak_now).setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW).build()

    }
}