package net.borysw.blitz.settings

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
        rxSharedPreferences.getString(KEY_TYPE).asObservable().map {
            when (it) {
                "" -> DefaultSettings.type
                else -> throw IllegalArgumentException("Unknown game type: $it.")
            }
        }

    override val gameSettings: Observable<GameSettings> =
        Observable.combineLatest(
            duration,
            type,
            BiFunction<Long, GameType, GameSettings> { duration, type ->
                GameSettings(duration, type)
            })
}