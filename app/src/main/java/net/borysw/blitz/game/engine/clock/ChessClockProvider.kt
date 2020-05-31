package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable

interface ChessClockProvider {
    val chessClock: Observable<ChessClock>
}