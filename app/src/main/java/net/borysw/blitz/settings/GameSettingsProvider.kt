package net.borysw.blitz.settings

import io.reactivex.Observable

interface GameSettingsProvider {
    val gameSettings: Observable<GameSettings>
}