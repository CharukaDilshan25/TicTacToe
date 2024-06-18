package com.example.newgame

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Register : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null // MediaPlayer instance for the sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the MediaPlayer with the sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.restart)

        val continueBtn1 = findViewById<Button>(R.id.continueBtn1)
        continueBtn1.setOnClickListener {

            mediaPlayer?.start()

            val playerOneName = findViewById<EditText>(R.id.editTextText).text.toString().trim()
            val playerTwoName = findViewById<EditText>(R.id.editTextText2).text.toString().trim()


            if (playerOneName.isEmpty() || playerTwoName.isEmpty()) {
                Toast.makeText(this, "Please enter both player names", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("PLAYER_ONE_NAME", playerOneName)
                intent.putExtra("PLAYER_TWO_NAME", playerTwoName)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release the MediaPlayer when the activity is destroyed to free up resources
        mediaPlayer?.release()
    }
}
