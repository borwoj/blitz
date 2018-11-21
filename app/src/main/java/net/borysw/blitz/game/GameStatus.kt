package net.borysw.blitz.game

data class GameStatus(val status: Status, val timeA: String, val timeB: String) {
  enum class Status {
    INITIAL, FINISHED, PLAYER_A, PLAYER_B
  }
}
