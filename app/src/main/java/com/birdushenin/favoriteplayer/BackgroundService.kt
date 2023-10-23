package com.birdushenin.favoriteplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BackgroundService: Service() {
    override fun onBind(Intent: Intent?): IBinder? = null



    override fun onCreate() {
        super.onCreate()
        println("BACK[1]")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        println("BACK[2]")




        stopSelf()

        return START_NOT_STICKY
    }
}