package com.siddiqui.myapplication.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import com.siddiqui.myapplication.R
import com.siddiqui.myapplication.utils.NotificationHelper


class RecordingService : Service() {

    private lateinit var notificationHelper: NotificationHelper
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)

        // 🎵 MediaSession for system playback controls
        mediaSession = MediaSessionCompat(this, "RecordingService").apply {
            isActive = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "ACTION_PLAY" -> sendBroadcast(Intent("PLAYER_PLAY"))
            "ACTION_PAUSE" -> sendBroadcast(Intent("PLAYER_PAUSE"))
            "ACTION_SEEK" -> {
                val pos = intent.getLongExtra("SEEK_TO", 0)
                sendBroadcast(Intent("PLAYER_SEEK").putExtra("pos", pos))
            }
            "ACTION_UPDATE" -> {
                val isPlaying = intent.getBooleanExtra("IS_PLAYING", false)
                val position = intent.getLongExtra("POSITION", 0L)
                val duration = intent.getLongExtra("DURATION", 0L)
                updateNotification(isPlaying, position, duration)
            }
        }
        return START_NOT_STICKY
    }

    fun updateNotification(isPlaying: Boolean, position: Long, duration: Long) {
        // 🔹 Playback state
        val state = PlaybackStateCompat.Builder()
            .setState(
                if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                position,
                1.0f
            )
            .setActions(
                PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )
            .build()

        mediaSession.setPlaybackState(state)

        // 🔹 Metadata (for seekbar range)
        val metadata = MediaMetadataCompat.Builder()
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
            .build()
        mediaSession.setMetadata(metadata)

        // 🔹 Notification
        val notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setContentTitle("Recording Playback")
            .setContentText(if (isPlaying) "Playing..." else "Paused")
            .setSmallIcon(R.drawable.baseline_mic_24)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1)
            )
            .addAction(
                R.drawable.baseline_play_circle_24, "Play",
                getPendingIntent("ACTION_PLAY")
            )
            .addAction(
                R.drawable.baseline_pause_circle_24, "Pause",
                getPendingIntent("ACTION_PAUSE")
            )
            .setOngoing(isPlaying)
            .setOnlyAlertOnce(true)
            .build()

        startForeground(NotificationHelper.NOTIFICATION_ID, notification)
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        stopForeground(true)
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, RecordingService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(this, action.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onBind(p0: Intent?): IBinder? = null
}
