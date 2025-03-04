package com.example.labwork.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.labwork.R
import com.example.labwork.MainActivity

class MusicService : Service() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer.isLooping = true
        createNotificationChannel()
        Log.d("MusicService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MusicService", "onStartCommand: ${intent?.action}")
        when (intent?.action) {
            ACTION_START -> startMusic()
            ACTION_PAUSE -> pauseMusic()
            ACTION_STOP -> stopMusic()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        Log.d("MusicService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startMusic() {
        Log.d("MusicService", "startMusic")
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            showNotification("Playing music")
        }
    }

    private fun pauseMusic() {
        Log.d("MusicService", "pauseMusic")
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            showNotification("Music paused")
        }
    }

    private fun stopMusic() {
        Log.d("MusicService", "stopMusic")
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            stopForeground(true)
            stopSelf()
        }
    }

    private fun showNotification(status: String) {
        Log.d("MusicService", "Showing notification: $status")
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "music_service_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val playPauseAction = if (mediaPlayer.isPlaying) {
            NotificationCompat.Action(R.drawable.ic_pause, "Pause", getPendingIntent(ACTION_PAUSE))
        } else {
            NotificationCompat.Action(R.drawable.ic_stop, "Play", getPendingIntent(ACTION_START))
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Music Player")
            .setContentText(status)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .addAction(playPauseAction)
            .addAction(R.drawable.ic_stop, "Stop", getPendingIntent(ACTION_STOP))
            .setOngoing(true)
            .build()

        Log.d("MusicService", "Notification built: $notification")
        startForeground(1, notification)
        Log.d("MusicService", "Notification started in foreground")
    }

    private fun getPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
            Log.d("MusicService", "Notification channel created")
        }
    }

    companion object {
        const val ACTION_START = "com.example.Labwork.action.START"
        const val ACTION_PAUSE = "com.example.Labwork.action.PAUSE"
        const val ACTION_STOP = "com.example.Labwork.action.STOP"
        const val CHANNEL_ID = "music_service_channel"
    }
}