package net.borysw.blitz.game.engine.time

import io.reactivex.Observable

interface TimeEngine {
    val time: Observable<Long>
}