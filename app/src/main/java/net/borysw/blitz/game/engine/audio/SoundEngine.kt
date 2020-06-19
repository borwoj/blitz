package net.borysw.blitz.game.engine.audio

import io.reactivex.Observable

interface SoundEngine {
    val sound: Observable<Unit>
}