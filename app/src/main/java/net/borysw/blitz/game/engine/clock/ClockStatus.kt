package net.borysw.blitz.game.engine.clock

import net.borysw.blitz.game.clock.type.ChessClock

data class ClockStatus(
    val initialTime: Long,
    val remainingTimePlayer1: Long,
    val remainingTimePlayer2: Long,
    val currentPlayer: ChessClock.Player?
)