package net.borysw.blitz.settings

import io.reactivex.Observable

interface GameSettings {
    data class Settings(val duration: Long, val type: GameType)

    val gameSettings: Observable<Settings>
}

