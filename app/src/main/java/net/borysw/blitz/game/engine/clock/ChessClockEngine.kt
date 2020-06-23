package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import net.borysw.blitz.game.clock.ClockStatus

interface ChessClockEngine {
    val clockStatus: Observable<ClockStatus>
}