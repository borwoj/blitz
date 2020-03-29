package net.borysw.blitz.game.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GameSettingsProviderImpl @Inject constructor(rxSharedPreferences: RxSharedPreferences) :
    GameSettingsProvider {
    companion object {
        private const val KEY_DURATION = "game_duration"
        private const val KEY_TYPE = "game_type"
    }

    private val duration =
        rxSharedPreferences.getLong(KEY_DURATION, DefaultSettings.gameDuration).asObservable()
    private val type =
        rxSharedPreferences.getString(KEY_TYPE).asObservable().map { GameType.Standard }

    override val gameSettings: Observable<GameSettings> =
        Observable.zip(duration, type, BiFunction<Long, GameType, GameSettings> { duration, type ->
            GameSettings(duration, type)
        })
}