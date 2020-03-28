package net.borysw.blitz.game.presentation

interface ChessClock {
    enum class Player { FIRST, SECOND }

    var initialTime: Long
    val remainingTimePlayer1: Long
    val remainingTimePlayer2: Long
    var currentPlayer: Player?
    val isTimeOver: Boolean
    fun advanceTime()
    fun onPaused()
    fun changeTurn(nextPlayer: Player)
    fun reset()
}