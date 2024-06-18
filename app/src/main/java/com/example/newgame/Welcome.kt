package com.example.newgame

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Welcome : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        mediaPlayer = MediaPlayer.create(this, R.raw.launching) // Replace 'launch_sound' with your sound file name
        mediaPlayer?.start()


        Handler().postDelayed({
            startActivity(Intent(this, Register::class.java))
            finish() // Finish the current activity so the user can't go back to it
        }, 2000) // 2000 milliseconds = 2 seconds
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}