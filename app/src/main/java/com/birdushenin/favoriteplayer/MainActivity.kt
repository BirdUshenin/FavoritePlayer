package com.birdushenin.favoriteplayer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.birdushenin.favoriteplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val playButton = binding.start
        val stopButton = binding.pause
        val nextButton = binding.next
        val backButton = binding.back

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
}
