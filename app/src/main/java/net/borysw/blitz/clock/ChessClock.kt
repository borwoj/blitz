package net.borysw.blitz.clock

interface ChessClock {
    sealed class Player {
        object Player1 : Player()
        object Player2 : Player()
    }

    var initialTime: Long
    var currentPlayer: Player?
    val remainingTimePlayer1: Long
    val remainingTimePlayer2: Long
    val isTimeOver: Boolean

    fun changeTurn(nextPlayer: Player)
    fun advanceTime()
    fun pause()
    fun reset()
}