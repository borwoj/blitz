package net.borysw.blitz.game.engine

import io.reactivex.Observable

interface TimeEngine {
    val time: Observable<Long>
}