package net.borysw.blitz.game.clock

import io.reactivex.Observable
import net.borysw.blitz.game.clock.type.ChessClock

interface ChessClockProvider {
    val chessClock: Observable<ChessClock>
}