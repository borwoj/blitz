package net.borysw.blitz.game.status

import net.borysw.blitz.clock.ChessClock

interface GameStatusFactory {
    fun getStatus(chessClock: ChessClock): GameStatus
}