package net.borysw.blitz.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.Schedulers.IO
import javax.inject.Inject
import javax.inject.Named

class SettingsImpl @Inject constructor(
    @Named(IO)
    ioScheduler: Scheduler,
    rxSharedPreferences: RxSharedPreferences
) : Settings {

    companion object {
        private const val KEY_DURATION = "game_duration"
        private const val KEY_TYPE = "game_type"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
    }

    private val duration =
        rxSharedPreferences.getLong(KEY_DURATION, DefaultSettings.duration).asObservable()

    private val soundEnabled =
        rxSharedPreferences.getBoolean(KEY_SOUND_ENABLED, DefaultSettings.soundEnabled)
            .asObservable()

    private val type =
        rxSharedPreferences.getString(KEY_TYPE).asObservable().map {
            when (it) {
                "" -> DefaultSettings.type
                else -> throw IllegalArgumentException("Unknown game type: $it.")
            }
        }

    override val gameSettings: Observable<Settings.GameSettings> =
        combineLatest(
            duration,
            type,
            BiFunction<Long, GameType, Settings.GameSettings> { duration, type ->
                Settings.GameSettings(duration, type)
            }).subscribeOn(ioScheduler)

    override val appSettings: Observable<Settings.AppSettings> =
        soundEnabled.map { Settings.AppSettings(it) }
}