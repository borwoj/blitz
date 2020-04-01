package net.borysw.blitz.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.Schedulers.IO
import javax.inject.Inject
import javax.inject.Named

class GameSettingsImpl @Inject constructor(
    @Named(IO)
    ioScheduler: Scheduler,
    rxSharedPreferences: RxSharedPreferences
) : GameSettings {

    companion object {
        private const val KEY_DURATION = "game_duration"
        private const val KEY_TYPE = "game_type"
    }

    private val duration =
        rxSharedPreferences.getLong(KEY_DURATION, DefaultGameSettings.duration).asObservable()

    private val type =
        rxSharedPreferences.getString(KEY_TYPE).asObservable().map {
            when (it) {
                "" -> DefaultGameSettings.type
                else -> throw IllegalArgumentException("Unknown game type: $it.")
            }
        }

    override val settings: Observable<GameSettings.Settings> =
        combineLatest(
            duration,
            type,
            BiFunction<Long, GameType, GameSettings.Settings> { duration, type ->
                GameSettings.Settings(duration, type)
            }).subscribeOn(ioScheduler)
}