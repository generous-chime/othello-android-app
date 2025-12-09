package com.example.groupproject

import android.util.Log

//PlayBoard Class implements the Othello game
// This class should be sufficient to handle all game logic
// We are locking down the myBoard to 8x8

class PlayBoard {
    //A cell in the myBoard can be  "BLANK" or "BLACK" or "WHITE",
    companion object {
        private const val BlackPlayer = 0
        private const val WhitePlayer = 1

        //Constructor which creates an 8x8 Reversi Board with the correct coin setup.
        //
        private const val BSIZE = 8
    }

    enum class Color {
        BLACK, WHITE, BLANK
    }

    //CurrentPlayer keeps track of who plays next. Black starts
    private var CurrentPlayer = 0

    //Creating two 2D arrays
    private val myBoard: Array<Array<Color?>>
    private val tempBoard: Array<Array<Color?>>

    init {
        myBoard = Array(BSIZE) {
            arrayOfNulls(
                BSIZE
            )
        }
        tempBoard = Array(BSIZE) {
            arrayOfNulls(
                BSIZE
            )
        }
        //Initialize to blank
        for (i in 0 until BSIZE) {
            for (j in 0 until BSIZE) {
                myBoard[i][j] = Color.BLANK
                tempBoard[i][j] = Color.BLANK
            }
        }

        // Add two black and two white coins to start
        myBoard[BSIZE / 2][BSIZE / 2 - 1] = Color.BLACK
        myBoard[BSIZE / 2 - 1][BSIZE / 2] = Color.BLACK
        myBoard[BSIZE / 2 - 1][BSIZE / 2 - 1] = Color.WHITE
        myBoard[BSIZE / 2][BSIZE / 2] = Color.WHITE
        tempBoard[BSIZE / 2][BSIZE / 2 - 1] = Color.BLACK
        tempBoard[BSIZE / 2 - 1][BSIZE / 2] = Color.BLACK
        tempBoard[BSIZE / 2 - 1][BSIZE / 2 - 1] = Color.WHITE
        tempBoard[BSIZE / 2][BSIZE / 2] = Color.WHITE
    }

    // Returns Color of current player
    fun GamePieceColor(): Color {
        return if (CurrentPlayer == BlackPlayer) Color.BLACK else Color.WHITE
    }

    // REturns color of other player
    fun OpponentColor(): Color {
        return if (CurrentPlayer == BlackPlayer) Color.WHITE else Color.BLACK
    }

    private fun lowerBound(a: Int, b: Int): Int {
        return if (a > b) b else a
    }

    private fun upperBound(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    //This method checks if a move is valid. If it is, it returns true
    // DOES NOT MAKE THE MOVE
    fun isValidMove(row: Int, column: Int): Boolean {

        //illegal move
        if (row < 0 || row > 7 || column < 0 || column > 7 || myBoard[row][column] != Color.BLANK) {
            return false
        }
        val UserColor = GamePieceColor()
        val OpponentColor = OpponentColor()

        //Checking to see if the any coins flip along a row or column.
        for (i in 0 until BSIZE) {
            for (j in 0 until BSIZE) {
                var counter = 0 //how many opponent coins between two coins of player
                if (myBoard[i][j] == UserColor && !(row == i && column == j)) { //checks if a coin is of User's color.
                    if (row == i && Math.abs(column - j) > 1) { //checks if row is the same as placed coin; column space must also be > 1
                        for (k in lowerBound(column, j) + 1 until upperBound(
                            column,
                            j
                        )) { //counts no.of opponent's coins in between.
                            if (myBoard[i][k] == OpponentColor) counter++
                        }
                        if (counter == upperBound(column, j) - lowerBound(
                                column,
                                j
                            ) - 1
                        ) return true //true if all coins in between belong to the opponent!
                    } else if (column == j && Math.abs(row - i) > 1) { //checks if column is the same as placed coin; row space must be > 1
                        for (k in lowerBound(row, i) + 1 until upperBound(
                            row,
                            i
                        )) { //counts no.of opponent's coins in between.
                            if (myBoard[k][j] == OpponentColor) counter++
                        }
                        if (counter == upperBound(row, i) - lowerBound(
                                row,
                                i
                            ) - 1
                        ) return true //true if all coins in between belong to the opponent!
                    } else if (Math.abs(column - j) == Math.abs(row - i) && Math.abs(row - i) > 1 && Math.abs(
                            column - j
                        ) > 1
                    ) {
                        var a = i
                        var b = j
                        while (row != a && column != b) {
                            if (row > i && column > j) { //Determining mode of iteration
                                a++
                                b++
                                if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                                    counter++
                            } else if (i > row && j > column) { //Determining mode of iteration
                                a--
                                b--
                                if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                                    counter++
                            } else if (i > row && column > j) { //Determining mode of iteration
                                a--
                                b++
                                if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                                    counter++
                            } else if (row > i && j > column) { //Determining mode of iteration
                                a++
                                b--
                                if (myBoard[a][b] == OpponentColor) //counts no.of opponent's coins in between.
                                    counter++
                            }
                        }
                        if (counter == Math.abs(row - i) - 1) return true //true if all coins in between belong to the opponent!
                    }
                }
            }
        }
        return false //false when no coins are flipped!
    }

    //Checks to see if there are any ValidPositions for Current Player.
    fun HasValidPositions(): Boolean {
        for (i in 0 until BSIZE) {
            for (j in 0 until BSIZE) {
                if (isValidMove(i, j) == true) {
                    return true
                }
            }
        }
        return false
    }

    //This method modifies myBoard with the next move isValidMove() is true .
    fun makeMove(row: Int, column: Int): Boolean {
        if (isValidMove(row, column)) {
            flipCoins(row, column)
            myBoard[row][column] = GamePieceColor()
            tempBoard[row][column] = GamePieceColor()
            newTurn()
            return true
        } else Log.w("OthelloActivity", "You have played an illegal move. Please try again!")
        return false
    }

    fun newTurn(){
        CurrentPlayer =
            if (CurrentPlayer == BlackPlayer) WhitePlayer else BlackPlayer
    }

    private fun flipCoins(row: Int, column: Int) {
        //Move in every direction and flip all the coins
        val UserColor = GamePieceColor()
        val OpponentColor = OpponentColor()

        //Checking to see if the any coins flip along a row or column.
        for (i in 0 until BSIZE) {
            for (j in 0 until BSIZE) {
                var validFlip = true
                var counter = 0 //how many opponent coins between two coins of player
                if (myBoard[i][j] == UserColor && !(row == i && column == j)) { //checks if a coin is of User's color.
                    if (row == i && Math.abs(column - j) > 1) { //checks if row is the same as placed coin; column space must also be > 1
                        for (k in lowerBound(column, j) + 1 until upperBound(
                            column,
                            j
                        )) { //counts no.of opponent's coins in between.
                            if (myBoard[i][k] == OpponentColor) {
                                counter++
                                tempBoard[i][k] = UserColor
                            }
                        }
                        if (counter != upperBound(column, j) - lowerBound(column, j) - 1) {
                            validFlip = false
                            revertTempBoard()
                        }
                    } else if (column == j && Math.abs(row - i) > 1) { //checks if column is the same as placed coin; row space must be > 1
                        for (k in lowerBound(row, i) + 1 until upperBound(
                            row,
                            i
                        )) { //counts no.of opponent's coins in between.
                            if (myBoard[k][j] == OpponentColor) {
                                counter++
                                tempBoard[k][j] = UserColor
                            }
                        }
                        if (counter != upperBound(row, i) - lowerBound(row, i) - 1) {
                            validFlip = false
                            revertTempBoard()
                        }
                    } else if (Math.abs(column - j) == Math.abs(row - i) && Math.abs(row - i) > 1 && Math.abs(
                            column - j
                        ) > 1
                    ) {
                        var a = i
                        var b = j
                        while (row != a && column != b) {
                            if (row > i && column > j) { //Determining mode of iteration
                                a++
                                b++
                                if (myBoard[a][b] == OpponentColor) { //counts no.of opponent's coins in between.
                                    counter++
                                    tempBoard[a][b] = UserColor
                                }
                            } else if (i > row && j > column) { //Determining mode of iteration
                                a--
                                b--
                                if (myBoard[a][b] == OpponentColor) { //counts no.of opponent's coins in between.
                                    counter++
                                    tempBoard[a][b] = UserColor
                                }
                            } else if (i > row && column > j) { //Determining mode of iteration
                                a--
                                b++
                                if (myBoard[a][b] == OpponentColor) { //counts no.of opponent's coins in between.
                                    counter++
                                    tempBoard[a][b] = UserColor
                                }
                            } else if (row > i && j > column) { //Determining mode of iteration
                                a++
                                b--
                                if (myBoard[a][b] == OpponentColor) { //counts no.of opponent's coins in between.
                                    counter++
                                    tempBoard[a][b] = UserColor
                                }
                            }
                        }
                        if (counter != Math.abs(row - i) - 1) {
                            validFlip = false
                            revertTempBoard()
                        }
                    }
                    if (validFlip == true) {
                        for (e in 0 until BSIZE) {
                            for (f in 0 until BSIZE) {
                                myBoard[e][f] = tempBoard[e][f]
                            }
                        }
                    }
                }
            }
        }
    }

    private fun revertTempBoard() {
        for (i in 0 until BSIZE) {
            for (j in 0 until BSIZE) {
                tempBoard[i][j] = myBoard[i][j]
            }
        }
    }

    fun getBoard() : Array<Array<Color?>>{
        return myBoard
    }

    fun getWinner(): Int {
        var numBlacks = 0
        var numWhites = 0

        for (i in 0 until BSIZE) {
            for (j in 0 until BSIZE) {
                if (myBoard[i][j] == Color.WHITE) {
                    numWhites++
                }
                if(myBoard[i][j] == Color.BLACK)
                    numBlacks++
                }
            }

        Log.w("OthelloActivity", numBlacks.toString())
        Log.w("OthelloActivity", numWhites.toString())
        return when {
            numBlacks > numWhites -> BlackPlayer
            numWhites > numBlacks -> WhitePlayer
            else -> -1
        }

    }

}