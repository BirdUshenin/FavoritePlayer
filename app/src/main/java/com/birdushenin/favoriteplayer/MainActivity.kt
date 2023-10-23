package com.birdushenin.favoriteplayer

import MusicService
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.annotation.RequiresApi
import com.birdushenin.favoriteplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var mediaPlayer = MediaPlayer()
    private var currentSongIndex = 0
    private var serviceBound = false
    private var musicService: MusicService? = null
    private var audioFiles = listOf(R.raw.song1, R.raw.song2, R.raw.song3,
        R.raw.song4, R.raw.song5)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayer = MediaPlayer.create(this, audioFiles[currentSongIndex])

        val start = binding.start
        val next = binding.next
        val back = binding.back
        val pause = binding.pause

        next.setOnClickListener{
            musicNext()
        }

        back.setOnClickListener{
            musicBack()
        }

        start.setOnClickListener {
            musicStart()
        }
        pause.setOnClickListener{
            musicPause()
        }


    }
    private fun musicStart() {
        musicService?.playMusic()
    }
    private fun musicPause(){
        mediaPlayer.pause()
    }
    private fun musicNext(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        currentSongIndex++
        if (currentSongIndex >= audioFiles.size) {
            currentSongIndex = 0
        }
        mediaPlayer = MediaPlayer.create(this, audioFiles[currentSongIndex])
        mediaPlayer.start()
        updateSongName()
    }

    private fun musicBack(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        currentSongIndex--
        if (currentSongIndex < 0){
            currentSongIndex = audioFiles.size - 1
        }
        mediaPlayer = MediaPlayer.create(this, audioFiles[currentSongIndex])
        mediaPlayer.start()
        updateSongName()
    }

    fun updateSongName() {
        val nameSong = binding.nameMusic
        when (currentSongIndex) {
            0 -> nameSong.text = "Тишина"
            1 -> nameSong.text = "JKRS, AIZZO - Baby When the Light"
            2 -> nameSong.text = "Carter tomorrow - Kill Me Nice"
            3 -> nameSong.text = "not dvr - muscles"
            4 -> nameSong.text = "forgottenposse, myle, faeri - busy dying"
        }
    }


    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.LocalBinder
            musicService = binder.getService()
            serviceBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?){
            serviceBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        if (!serviceBound){
            val serviceIntent = Intent(this, MusicService::class.java)
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceBound){
            unbindService(serviceConnection)
            serviceBound = false
        }
    }
}


