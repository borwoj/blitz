package net.borysw.blitz.game.engine.clock

interface ChessClock {
    sealed class Player {
        object Player1 : Player()
        object Player2 : Player()
    }

    var initialTime: Long
    var currentPlayer: Player?
    val remainingTimePlayer1: Long
    val remainingTimePlayer2: Long
    val remainingDelayTimePlayer1: Long //TODO remove these 2 from the interface
    val remainingDelayTimePlayer2: Long
    val isTimeOver: Boolean
    val isPaused: Boolean

    fun changeTurn(nextPlayer: Player)
    fun advanceTime()
    fun addTimePlayer1(time: Long)
    fun addTimePlayer2(time: Long)
    fun pause()
    fun reset()
}