package net.borysw.blitz.app.clock

interface ChessClock {
    enum class Player { PLAYER_1, PLAYER_2 }

    var initialTime: Long
    var currentPlayer: Player?
    val remainingTimePlayer1: Long
    val remainingTimePlayer2: Long
    val isTimeOver: Boolean

    fun advanceTime()
    fun onPaused()
    fun changeTurn(nextPlayer: Player)
    fun reset()
}