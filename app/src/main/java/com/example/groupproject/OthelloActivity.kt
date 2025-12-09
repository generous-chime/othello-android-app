package com.example.groupproject

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class OthelloActivity : AppCompatActivity() {

    private lateinit var game: PlayBoard
    private lateinit var gameView: OthelloView
    private lateinit var moveInput: EditText
    private lateinit var turnOutput: TextView
    private lateinit var preferences: SharedPreferences
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE)

        game = PlayBoard()
        gameView = OthelloView(this, game)
        moveInput = EditText(this)
        turnOutput = TextView(this)

        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal)
        progressBar.max = 60

        /*
        progressBar.visibility = View.VISIBLE
        val progressBarParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        progressBar.layoutParams = progressBarParams
         */


        progressBarLabel = TextView(this)
        progressBarLabel.text = "Fraction of moves to game completion"

        val submitButton = Button(this)
        submitButton.text = "TYPE Move (Ex: a1 or h8)"

        turnOutput.text = "BLACK'S TURN TO PLAY"
        val turnViewParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        turnViewParams.bottomMargin = (resources.displayMetrics.widthPixels / 8)
        turnOutput.layoutParams = turnViewParams

        // Create a LinearLayout to hold the views
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val gameViewParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.displayMetrics.widthPixels + resources.displayMetrics.widthPixels/8
        )

        // Add the OthelloView and move input views to the layout
        layout.addView(gameView, gameViewParams)
        layout.addView(submitButton)
        layout.addView(moveInput)
        layout.addView(turnOutput)
        layout.addView(progressBarLabel)
        layout.addView(progressBar)



        // Set the layout as the content view
        setContentView(layout)


        submitButton.setOnClickListener {

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            val move = moveInput.text.toString().lowercase()
            var input_row = 0
            var input_column = 0

            if (move.length == 2){

                when (move[0]) {
                    'a' -> input_column = 1
                    'b' -> input_column = 2
                    'c' -> input_column = 3
                    'd' -> input_column = 4
                    'e' -> input_column = 5
                    'f' -> input_column = 6
                    'g' -> input_column = 7
                    'h' -> input_column = 8
                }

                when (move[1]){
                    '1' -> input_row = 1
                    '2' -> input_row = 2
                    '3' -> input_row = 3
                    '4' -> input_row = 4
                    '5' -> input_row = 5
                    '6' -> input_row = 6
                    '7' -> input_row = 7
                    '8' -> input_row = 8
                }
            }



            if (game.makeMove(input_row - 1, input_column - 1)) {

                gameView.updateView()
                progressBar.progress += 1


                var GameRun = 0
                if (!game.HasValidPositions()) {
                    GameRun++;
                    game.newTurn()

                    if (!game.HasValidPositions())
                        GameRun++;
                }

                //Nobody has valid moves so end the game
                if (GameRun > 1) {

                    // Read the number of games played
                    val gamesPlayed = preferences.getInt("games_played", 0)

                    // Increment the number of games played by one
                    val newGamesPlayed = gamesPlayed + 1

                    var editor = preferences.edit()

                    editor.putInt("games_played",newGamesPlayed)
                    editor.commit()

                    var intent = Intent(this, EndActivity::class.java)
                    intent.putExtra("winner", game.getWinner())
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
                }

                // Update turnOutput based on the current player's turn
                turnOutput.text = if (game.GamePieceColor() == PlayBoard.Color.BLACK) {
                    "BLACK'S TURN TO PLAY"
                } else {
                    "WHITE'S TURN TO PLAY"
                }

            } else {
                val invalidMoveSnackbar = Snackbar.make(
                    layout,
                    "Invalid move. Please enter a valid move.",
                    Snackbar.LENGTH_LONG
                )
                invalidMoveSnackbar.show()
            }

            moveInput.setText("")
        }
    }
}