package com.birdushenin.favoriteplayer

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.media.app.NotificationCompat.MediaStyle
import android.content.Context



class MyService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val channelId = "MusicChannel"
    private val notificationId = 1
    private val songList = intArrayOf(R.raw.song2, R.raw.song3, R.raw.song4,  R.raw.song1,
        R.raw.song5)
    private var currentSongPosition = 0
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.song2)
    }

    private val songLyrics = arrayOf(
        "Mood Swing Misery - Pouya, Rocci",
        "muscles - not dvr",
        "Baby When the Light - JKRS, AIZZO",
        "Kill Me Nice - Carter tomorrow",
        "busy dying - forgottenposse, myle$, faeri"
    )
    private var isPlaying = false

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotification() {
        createNotificationChannel()

        val playIntent = if (isPlaying) {
            Intent(this, MyService::class.java).apply {
                action = "PAUSE"
            }
        } else {
            Intent(this, MyService::class.java).apply {
                action = "PLAY"
            }
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
                .setColor(ContextCompat.getColor(this, R.color.purple_200))

                .setSmallIcon(R.drawable.ic_music)
                .setContentTitle("Now Playing")
                .setContentText(songLyrics[currentSongPosition])
                .addAction(R.drawable.ic_previous, "Previous", previousPendingIntent)
//                .addAction(R.drawable.ic_play, "Play", playPendingIntent)
                .addAction(if (isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_play
                , if (isPlaying) "Pause" else "Play", playPendingIntent)
//                .addAction(R.drawable.ic_stop2, "Stop", stopPendingIntent)
                .addAction(R.drawable.ic_next, "Next", nextPendingIntent)

                .setSound(null)

                .setStyle(
                    MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2, 3)
                        .setMediaSession(null)
                )

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .build()

            startForeground(notificationId, notification)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Music Channel", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(NotificationManager::class.java)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
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
        if (intent != null) when (intent.action) {
            "PLAY" -> {
                if (mediaPlayer?.isPlaying != true) {
                    mediaPlayer?.start()
                }
//                isPlaying = true
                playSong()
                showNotification()
            }
            "PAUSE" -> {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.pause()
                }
                    isPlaying = false
                    showNotification()
            }
            "STOP" -> {
                if (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.stop()

                }
                isPlaying = false
                showNotification()
            }
            "PREVIOUS" -> {
                playPreviousSong()
                showNotification()
            }
            "NEXT" -> {
                playNextSong()
                showNotification()
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
            isPlaying = true
            sendSongChangeBroadcast()
        }
    }

    private fun sendSongChangeBroadcast(){
        val intent = Intent("SONG_CHANGE")
        intent.putExtra("songLyrics", songLyrics[currentSongPosition])
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun playSong(){
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer.create(this, songList[currentSongPosition])
        isPlaying = true
        mediaPlayer?.start()
    }

    private fun playNextSong() {
        if (currentSongPosition < songList.size - 1) {
            currentSongPosition++
            mediaPlayer?.reset()
            isPlaying = true
            mediaPlayer = MediaPlayer.create(this, songList[currentSongPosition])
            mediaPlayer?.start()
        }
    }
}


