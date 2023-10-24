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

        playButton.setOnClickListener {
            val serviceIntent = Intent(this, MyService::class.java)
            serviceIntent.action = "PLAY"
            startService(serviceIntent)
        }
    }
}
