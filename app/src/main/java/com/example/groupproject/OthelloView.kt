package com.example.groupproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class OthelloView : View {

    private var game : PlayBoard
    private var squareSize : Float
    private var paint : Paint

    constructor(context: Context, othello : PlayBoard) : super( context ) {
        game = othello

        var screenWidth = resources.displayMetrics.widthPixels
        squareSize = (screenWidth / 8).toFloat()

        paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawCoins(canvas)
        drawGrid(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        // Set up paint for drawing the grid lines
        paint.color = Color.parseColor("#40E0D0")
        paint.strokeWidth = 2f
        paint.textSize = squareSize / 2.toFloat()

        // Draw horizontal lines and numbers
        for (i in 0 until 9) {
            val y = i * squareSize
            canvas.drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), paint)

            // Draw numbers for each row
            if (i > 0) {
                val text = (i).toString()
                val textX = squareSize / 4.toFloat()
                val textY = y - squareSize / 4.toFloat()
                canvas.drawText(text, textX, textY, paint)
            }
        }

        // Draw vertical lines and letters
        for (i in 0 until 9) {
            val x = i * squareSize
            canvas.drawLine(x.toFloat(), 0f, x.toFloat(), 8 * squareSize.toFloat(), paint)

            // Draw letters for each column
            if (i < 8) {
                val text = ('a' + i).toString()
                val textX = x + squareSize / 4.toFloat()
                val textY = 9 * squareSize - squareSize / 4.toFloat()
                canvas.drawText(text, textX, textY, paint)
            }
        }
    }

    private fun drawCoins(canvas: Canvas) {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                if (game.getBoard()[i][j] == PlayBoard.Color.BLACK) {
                        drawCoin(canvas, i, j, Color.BLACK)
                }
                else if (game.getBoard()[i][j] == PlayBoard.Color.WHITE) {
                   drawCoin(canvas, i, j, Color.GRAY)
                }
            }
        }
    }

    private fun drawCoin(canvas: Canvas, row: Int, col: Int, color: Int) {
        paint.color = color
        val centerX = (col + 0.5) * squareSize
        val centerY = (row + 0.5) * squareSize
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), squareSize / 2 - 5, paint)
    }

    fun updateView(){
        postInvalidate()
    }
}