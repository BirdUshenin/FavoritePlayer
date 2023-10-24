package com.birdushenin.favoriteplayer

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle

class MyService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val channelId = "MusicChannel"
    private val notificationId = 1
    private val songList = intArrayOf(R.raw.song1, R.raw.song2, R.raw.song3)
    private var currentSongPosition = 0
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.song1)
    }

    private fun showNotification() {
        createNotificationChannel()

        val playIntent = Intent(this, MyService::class.java).apply {
            action = "PLAY"
        }
        val playPendingIntent = PendingIntent.getService(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val stopIntent = Intent(this, MyService::class.java).apply {
            action = "STOP"
        }
        val stopPendingIntent = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val previousIntent = Intent(this, MyService::class.java).apply {
            action = "PREVIOUS"
        }
        val previousPendingIntent = PendingIntent.getService(this, 2, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(this, MyService::class.java).apply {
            action = "NEXT"
        }
        val nextPendingIntent = PendingIntent.getService(this, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_music)
            .setContentTitle("Now Playing")
            .setContentText("Song Title")
            .addAction(R.drawable.ic_previous, "Previous", previousPendingIntent)
            .addAction(R.drawable.ic_play, "Play", playPendingIntent)
            .addAction(R.drawable.ic_play, "Stop", stopPendingIntent)
            .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
            .setStyle(MediaStyle()
                .setShowActionsInCompactView(0, 1, 2, 3))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(false)
            .setOngoing(true)
            .build()

        startForeground(notificationId, notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Music Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            when (action) {
                "PLAY" -> {
                    //if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.start()
                    showNotification()
                    //}
                }
                "STOP" -> {
                    if (mediaPlayer?.isPlaying == true) {
                        mediaPlayer?.stop()
                        showNotification()
                    }
                }
                "PREVIOUS" -> {
                    // Обработка действия "Previous"
                    playPreviousSong()
                }
                "NEXT" -> {
                    // Обработка действия "Next"
                    playNextSong()
                }
            }
        }
        return START_STICKY
    }

    private fun playPreviousSong() {
        if (currentSongPosition > 0) {
            currentSongPosition--
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, songList[currentSongPosition])
            mediaPlayer?.start()
            showNotification()
        }
    }

    private fun playNextSong() {
        if (currentSongPosition < songList.size - 1) {
            currentSongPosition++
            mediaPlayer?.reset()
            mediaPlayer = MediaPlayer.create(this, songList[currentSongPosition])
            mediaPlayer?.start()
            showNotification()
        }
    }
}


