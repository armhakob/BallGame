package com.example.ballgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var pointsTextView: TextView
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointsTextView = findViewById(R.id.pointsTextView)
        retryButton = findViewById(R.id.retryButton)
        gameView = GameView(this, pointsTextView, retryButton)

        findViewById<FrameLayout>(R.id.gameContainer).addView(gameView)

        // Retry button functionality
        retryButton.setOnClickListener {
            gameView.resetGame()
            retryButton.visibility = Button.GONE
        }

        // Exit button functionality
        findViewById<Button>(R.id.exitButton).setOnClickListener {
            finish()
        }
    }
}