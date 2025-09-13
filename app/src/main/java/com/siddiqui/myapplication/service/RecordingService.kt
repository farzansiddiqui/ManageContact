package com.siddiqui.myapplication.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.siddiqui.myapplication.utils.NotificationHelper


class RecordingService: Service() {

    private lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = notificationHelper.buildNotification(
            "Recording...","Your audio is being recorded"
        )
        startForeground(NotificationHelper.NOTIFICATION_ID,notification)
        return START_NOT_STICKY
    }
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

}