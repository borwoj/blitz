package net.borysw.blitz.game.status

import net.borysw.blitz.game.engine.clock.ClockStatus

interface GameInfoCreator {
    fun get(clockStatus: ClockStatus): GameInfo
}