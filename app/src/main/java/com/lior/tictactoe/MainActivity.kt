package com.lior.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var btnPlayAgain: Button
    private lateinit var cells: Array<Array<Button>>

    private var board = Array(3) { CharArray(3) { ' ' } }
    private var currentPlayer: Char = 'X'
    private var gameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)

        cells = arrayOf(
            arrayOf(findViewById(R.id.b00), findViewById(R.id.b01), findViewById(R.id.b02)),
            arrayOf(findViewById(R.id.b10), findViewById(R.id.b11), findViewById(R.id.b12)),
            arrayOf(findViewById(R.id.b20), findViewById(R.id.b21), findViewById(R.id.b22))
        )

        // listeners for 9 buttons
        for (r in 0..2) {
            for (c in 0..2) {
                cells[r][c].setOnClickListener { onCellClicked(r, c) }
            }
        }

        btnPlayAgain.setOnClickListener { resetGame() }

        resetGame()
    }

    private fun onCellClicked(r: Int, c: Int) {
        if (gameOver) return
        if (board[r][c] != ' ') return  // already filled

        board[r][c] = currentPlayer
        cells[r][c].text = currentPlayer.toString()

        val winner = checkWinner()
        if (winner != null) {
            endGame("Player $winner wins!")
            return
        }

        if (isBoardFull()) {
            endGame("It's a draw!")
            return
        }

        // switch turns
        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
        tvStatus.text = "Player $currentPlayer turn"
    }

    private fun endGame(message: String) {
        gameOver = true
        tvStatus.text = message
        setBoardEnabled(false)
        btnPlayAgain.isEnabled = true

        // optional: show dialog
        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun resetGame() {
        board = Array(3) { CharArray(3) { ' ' } }
        currentPlayer = 'X'
        gameOver = false

        for (r in 0..2) for (c in 0..2) cells[r][c].text = ""
        setBoardEnabled(true)

        tvStatus.text = "Player X turn"
        btnPlayAgain.isEnabled = false
    }

    private fun setBoardEnabled(enabled: Boolean) {
        for (r in 0..2) for (c in 0..2) cells[r][c].isEnabled = enabled
    }

    private fun isBoardFull(): Boolean {
        for (r in 0..2) for (c in 0..2) {
            if (board[r][c] == ' ') return false
        }
        return true
    }

    private fun checkWinner(): Char? {
        // rows
        for (r in 0..2) {
            if (board[r][0] != ' ' && board[r][0] == board[r][1] && board[r][1] == board[r][2]) {
                return board[r][0]
            }
        }
        // cols
        for (c in 0..2) {
            if (board[0][c] != ' ' && board[0][c] == board[1][c] && board[1][c] == board[2][c]) {
                return board[0][c]
            }
        }
        // diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }

        return null
    }
}
