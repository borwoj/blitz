package net.borysw.blitz.game.presentation

data class GameStatus(val isFinished: Boolean, val timeA: String, val timeB: String, val playerMove: PlayerMove)

enum class PlayerMove {
  PLAYER_A, PLAYER_B
}