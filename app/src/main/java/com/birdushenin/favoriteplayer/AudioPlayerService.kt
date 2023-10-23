package com.birdushenin.favoriteplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class AudioPlayerService : Service() {

    private val mediaPlayer = MediaPlayer()
    private val binder = LocalBinder()

    inner class LocalBinder: Binder(){
        fun getService():AudioPlayerService = this@AudioPlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }



}