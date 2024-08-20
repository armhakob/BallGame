package com.example.ballgame

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class GameView(context: Context, private val pointsTextView: TextView, private val retryButton: Button) : View(context) {
    private val paint = Paint()
    private var playerX = 0f
    private var playerWidth = 300f
    private var playerHeight = 50f
    private var screenWidth = 0
    private var screenHeight = 0
    private var ballX = 0f
    private var ballY = 0f
    private var ballRadius = 30f
    private var ballSpeedX = 0f
    private var ballSpeedY = 0f
    private var ballMoving = false
    private var points = 0
    private var gameOver = false

    init {
        paint.color = Color.BLUE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w
        screenHeight = h
        resetGame()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(playerX, screenHeight - playerHeight, playerX + playerWidth, screenHeight.toFloat(), paint)

        canvas.drawCircle(ballX, ballY, ballRadius, paint)

        if (ballMoving) {
            moveBall()
        }

        invalidate()
    }

    private fun moveBall() {
        ballX += ballSpeedX
        ballY += ballSpeedY

        if (ballX <= 0 || ballX >= screenWidth) {
            ballSpeedX *= -1
        }

        if (ballY <= 0) {
            ballSpeedY *= -1
        }

        if (ballY + ballRadius >= screenHeight - playerHeight &&
            ballX >= playerX && ballX <= playerX + playerWidth
        ) {
            ballSpeedY *= -1
            points++
            ballSpeedX *= 1.1f
            ballSpeedY *= 1.1f

            pointsTextView.text = "Points: $points"

            if (points >= 10) {
                gameOver = true
                Toast.makeText(context, "You Win!", Toast.LENGTH_SHORT).show()
                endGame()
            }
        }

        if (ballY > screenHeight) {
            gameOver = true
            Toast.makeText(context, "Game Over!", Toast.LENGTH_SHORT).show()
            endGame()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (gameOver) {
            return super.onTouchEvent(event)
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                playerX = event.x - playerWidth / 2

                if (!ballMoving && isBallOnPlayer() && isBallClicked(event.x, event.y)) {
                    ballMoving = true
                    ballSpeedX = 10f
                    ballSpeedY = -20f
                }
            }
            MotionEvent.ACTION_MOVE -> {
                playerX = event.x - playerWidth / 2
            }
        }
        invalidate()
        return true
    }

    private fun isBallOnPlayer(): Boolean {
        return ballY + ballRadius >= screenHeight - playerHeight && ballY + ballRadius <= screenHeight
    }

    private fun isBallClicked(touchX: Float, touchY: Float): Boolean {
        val dx = touchX - ballX
        val dy = touchY - ballY
        return dx * dx + dy * dy <= ballRadius * ballRadius
    }

    private fun endGame() {
        ballMoving = false
        retryButton.visibility = Button.VISIBLE
    }

    fun resetGame() {
        playerX = (screenWidth / 2 - playerWidth / 2)
        ballX = (screenWidth / 2).toFloat()
        ballY = (screenHeight - playerHeight - ballRadius)
        ballSpeedX = 0f
        ballSpeedY = 0f
        ballMoving = false
        points = 0
        gameOver = false
        pointsTextView.text = "Points: $points"
        invalidate()
    }
}