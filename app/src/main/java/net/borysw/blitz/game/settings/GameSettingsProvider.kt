package net.borysw.blitz.game.settings

import io.reactivex.Observable

interface GameSettingsProvider {
    val gameSettings: Observable<GameSettings>
}