package com.birdushenin.favoriteplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.birdushenin.favoriteplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val songChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "SONG_CHANGED") {
                val songLyrics = intent.getStringExtra("songLyrics")
                // Обновите отображение текста песни в вашей активности
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playButton = binding.start
        val stopButton = binding.pause
        val nextButton = binding.next
        val backButton = binding.back

        val intentFilter = IntentFilter("SONG_CHANGED")
        LocalBroadcastManager.getInstance(this).registerReceiver(songChangeReceiver, intentFilter)


        playButton.setOnClickListener {
            val serviceIntent = Intent(this, MyService::class.java)
            serviceIntent.action = "PLAY"
            startService(serviceIntent)
        }
        stopButton.setOnClickListener {
            val serviceIntent = Intent(this, MyService::class.java)
            serviceIntent.action = "STOP"
            startService(serviceIntent)
        }
        nextButton.setOnClickListener {
            val serviceIntent = Intent(this, MyService::class.java)
            serviceIntent.action = "NEXT"
            startService(serviceIntent)
        }
        backButton.setOnClickListener {
            val serviceIntent = Intent(this, MyService::class.java)
            serviceIntent.action = "PREVIOUS"
            startService(serviceIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Отмена регистрации приемника при уничтожении активности
        LocalBroadcastManager.getInstance(this).unregisterReceiver(songChangeReceiver)
    }
}
