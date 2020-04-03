package net.borysw.blitz.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.Schedulers.IO
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class SettingsImpl @Inject constructor(
    @Named(IO)
    ioScheduler: Scheduler,
    rxSharedPreferences: RxSharedPreferences
) : Settings {

    companion object {
        const val KEY_DURATION = "game_duration"
        const val KEY_TYPE = "game_type"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_TIME_UNIT = "time_unit"
    }

    private val duration: Observable<Long> =
        combineLatest(rxSharedPreferences
            .getString(KEY_DURATION)
            .asObservable()
            .map { it.toLong() },
            rxSharedPreferences
                .getString(KEY_TIME_UNIT)
                .asObservable()
                .map { timeUnit ->
                    when (timeUnit) {
                        "Seconds" -> TimeUnit.SECONDS
                        "Minutes" -> TimeUnit.MINUTES
                        "Hours" -> TimeUnit.HOURS
                        else -> throw IllegalArgumentException("Unsupported time unit: $timeUnit")
                    }
                },
            BiFunction<Long, TimeUnit, Long> { duration, timeUnit ->
                timeUnit.toMillis(duration)
            })

    private val soundEnabled: Observable<Boolean> =
        rxSharedPreferences
            .getBoolean(KEY_SOUND_ENABLED)
            .asObservable()

    private val type: Observable<GameType> =
        rxSharedPreferences.getString(KEY_TYPE).asObservable().map {
            when (it) {
                "Standard" -> GameType.Standard
                else -> throw IllegalArgumentException("Unsupported game type: $it.")
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
        soundEnabled
            .map { Settings.AppSettings(it) }
}