package net.borysw.blitz.game.status

import net.borysw.blitz.app.clock.ChessClock

interface GameStatusFactory {
    fun getStatus(chessClock: ChessClock): GameStatus
}