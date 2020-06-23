package net.borysw.blitz.game.status

import net.borysw.blitz.game.clock.ClockStatus

interface GameInfoCreator {
    fun get(clockStatus: ClockStatus): GameInfo
}