package net.borysw.blitz.game.engine.clock

import net.borysw.blitz.settings.GameType

interface ChessClock {
    sealed class Player {
        object Player1 : Player()
        object Player2 : Player()
    }

    var gameType: GameType
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