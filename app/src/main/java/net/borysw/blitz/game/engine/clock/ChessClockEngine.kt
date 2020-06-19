package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable

interface ChessClockEngine {
    val clockStatus: Observable<ClockStatus>
}