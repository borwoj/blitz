package net.borysw.blitz.game.status

import net.borysw.blitz.clock.ChessClock

interface GameStatusProvider {
    fun getStatus(
        initialTime: Long,
        remainingTimePlayer1: Long,
        remainingTimePlayer2: Long,
        currentPlayer: ChessClock.Player?
    ): GameStatus
}