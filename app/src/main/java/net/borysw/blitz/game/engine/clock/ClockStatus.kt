package net.borysw.blitz.game.engine.clock

data class ClockStatus(
    val initialTime: Long,
    val remainingTimePlayer1: Long,
    val remainingTimePlayer2: Long,
    val currentPlayer: ChessClock.Player?
)