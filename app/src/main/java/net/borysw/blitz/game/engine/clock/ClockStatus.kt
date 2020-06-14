package net.borysw.blitz.game.engine.clock

import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.engine.dialog.Dialog

data class ClockStatus(
    val initialTime: Long,
    val remainingTimePlayer1: Long,
    val remainingTimePlayer2: Long,
    val currentPlayer: ChessClock.Player?,
    val dialog: Dialog
)