package com.example.newgame

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mediaPlayer: MediaPlayer? = null
    private var statusMediaPlayer: MediaPlayer? = null
    private var resetMediaPlayer: MediaPlayer? = null
    private var playAgainMediaPlayer: MediaPlayer? = null

    companion object {
        private const val SHARED_PREFS_KEY = "game_prefs"
        private const val HIGHEST_SCORE_KEY = "highest_score"
    }

    var playerOneActive = false
    private var playerOneScore: TextView? = null
    private var playerTwoScore: TextView? = null
    private var playerOneNameTextView: TextView? = null
    private var playerTwoNameTextView: TextView? = null
    private var playerStatus: TextView? = null
    private val buttons = arrayOfNulls<Button>(9)
    private var reset: Button? = null
    private var playagain: Button? = null
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    var winningPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
    var rounds = 0
    private var playerOneScoreCount = 0
    private var playerTwoScoreCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.button_click_sound)
        statusMediaPlayer = MediaPlayer.create(this, R.raw.winn)
        resetMediaPlayer = MediaPlayer.create(this, R.raw.restart)
        playAgainMediaPlayer = MediaPlayer.create(this, R.raw.restart)

        playerOneScore = findViewById(R.id.score_Player1)
        playerTwoScore = findViewById(R.id.score_Player2)
        playerOneNameTextView = findViewById(R.id.text_player1)
        playerTwoNameTextView = findViewById(R.id.text_player2)
        playerStatus = findViewById(R.id.textStatus)
        reset = findViewById(R.id.btn_reset)
        playagain = findViewById(R.id.button)
        buttons[0] = findViewById(R.id.btn0)
        buttons[1] = findViewById(R.id.btn1)
        buttons[2] = findViewById(R.id.btn2)
        buttons[3] = findViewById(R.id.btn3)
        buttons[4] = findViewById(R.id.btn4)
        buttons[5] = findViewById(R.id.btn5)
        buttons[6] = findViewById(R.id.btn6)
        buttons[7] = findViewById(R.id.btn7)
        buttons[8] = findViewById(R.id.btn8)

        for (i in buttons.indices) {
            buttons[i]?.setOnClickListener(this)
        }

        playerOneScoreCount = 0
        playerTwoScoreCount = 0
        playerOneActive = true
        rounds = 0

        val playerOneName = intent.getStringExtra("PLAYER_ONE_NAME") ?: "Player 1"
        val playerTwoName = intent.getStringExtra("PLAYER_TWO_NAME") ?: "Player 2"


        playerOneNameTextView?.text = playerOneName
        playerTwoNameTextView?.text = playerTwoName


        val sharedPrefs = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val highestScore = sharedPrefs.getInt(HIGHEST_SCORE_KEY, 0)


        val highestScoreTextView: TextView = findViewById(R.id.highest_score)
        highestScoreTextView.text = getString(R.string.highest_score_label, highestScore)

        reset!!.setOnClickListener {
            resetMediaPlayer?.start()
            playAgain()
            playerOneScoreCount = 0
            playerTwoScoreCount = 0
            updatePlayerScore()
        }
        playagain!!.setOnClickListener {
            playAgainMediaPlayer?.start()
            playAgain()
        }
    }

    override fun onClick(view: View) {
        mediaPlayer?.start()
        if ((view as Button).text.toString() != "") {
            return
        } else if (checkWinner()) {
            return
        }
        val buttonID = view.resources.getResourceEntryName(view.id)
        val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()
        if (playerOneActive) {
            view.text = "X"
            view.setTextColor(Color.parseColor("#ffc34a"))
            gameState[gameStatePointer] = 0
        } else {
            view.text = "O"
            view.setTextColor(Color.parseColor("#70fc3a"))
            gameState[gameStatePointer] = 1
        }
        rounds++
        if (checkWinner()) {
            if (playerOneActive) {
                playerOneScoreCount++
                updatePlayerScore()
                playerStatus!!.text = "Player-1 has won"
                statusMediaPlayer?.start() // Play sound when updating status
            } else {
                playerTwoScoreCount++
                updatePlayerScore()
                playerStatus!!.text = "Player-2 has won"
                statusMediaPlayer?.start() // Play sound when updating status
            }
        } else if (rounds == 9) {
            playerStatus!!.text = "No Winner"
        } else {
             playerOneActive = !playerOneActive
        }
        reset!!.setOnClickListener {
            playAgain()
            playerOneScoreCount = 0
            playerTwoScoreCount = 0
            updatePlayerScore()
        }
        playagain!!.setOnClickListener { playAgain() }
    }

    private fun checkWinner(): Boolean {
        var winnerResults = false
        for (winningPositions in winningPositions) {
            if (gameState[winningPositions[0]] == gameState[winningPositions[1]] && gameState[winningPositions[1]] == gameState[winningPositions[2]] && gameState[winningPositions[0]] != 2) {
                winnerResults = true
            }
        }
        return winnerResults
    }

    private fun playAgain() {
        rounds = 0
        playerOneActive = true
        for (i in buttons.indices) {
            gameState[i] = 2
            buttons[i]!!.text = ""
        }
        playerStatus!!.text = "Status"
    }

    private fun updatePlayerScore() {
        playerOneScore!!.text = playerOneScoreCount.toString()
        playerTwoScore!!.text = playerTwoScoreCount.toString()


        val sharedPrefs = getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
        val highestScore = sharedPrefs.getInt(HIGHEST_SCORE_KEY, 0)
        val totalScore = playerOneScoreCount + playerTwoScoreCount
        if (totalScore > highestScore) {
            val editor = sharedPrefs.edit()
            editor.putInt(HIGHEST_SCORE_KEY, totalScore)
            editor.apply()


            val highestScoreTextView: TextView = findViewById(R.id.highest_score)
            highestScoreTextView.text = getString(R.string.highest_score_label, totalScore)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        statusMediaPlayer?.release()
        resetMediaPlayer?.release()
        playAgainMediaPlayer?.release()
    }
}
