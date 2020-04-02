package net.borysw.blitz.settings

import io.reactivex.Observable

interface Settings {
    data class GameSettings(val duration: Long, val type: GameType)
    data class AppSettings(val soundEnabled: Boolean)

    val gameSettings: Observable<GameSettings>
    val appSettings: Observable<AppSettings>
}

