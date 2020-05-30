package net.borysw.blitz.game.status

import net.borysw.blitz.game.engine.clock.ChessClock

interface GameInfoCreator {
    fun get(
        initialTime: Long,
        remainingTimePlayer1: Long,
        remainingTimePlayer2: Long,
        currentPlayer: ChessClock.Player?
    ): GameInfo
}