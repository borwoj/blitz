package net.borysw.blitz.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import net.borysw.blitz.Schedulers.IO
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import javax.inject.Named

class SettingsImpl @Inject constructor(
    @Named(IO)
    ioScheduler: Scheduler,
    preferences: RxSharedPreferences
) : Settings {

    private companion object {
        const val KEY_DURATION = "game_duration"
        const val KEY_TYPE = "game_type"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_TIME_UNIT = "time_unit"
        const val KEY_DELAY = "delay"
        const val KEY_INCREMENT_BY = "increment_by"
    }

    private val duration: Observable<Long> =
        combineLatest(
            preferences
                .getString(KEY_DURATION)
                .asObservable()
                .map { it.toLong() },
            preferences
                .getString(KEY_TIME_UNIT)
                .asObservable()
                .map { it.toInt() }
                .map { timeUnit ->
                    when (timeUnit) {
                        0 -> SECONDS
                        1 -> MINUTES
                        2 -> HOURS
                        else -> throw IllegalArgumentException("Unsupported time unit: $timeUnit")
                    }
                },
            BiFunction<Long, TimeUnit, Long> { duration, timeUnit ->
                timeUnit.toMillis(duration)
            })

    private val soundEnabled: Observable<Boolean> =
        preferences
            .getBoolean(KEY_SOUND_ENABLED)
            .asObservable()

    private val type: Observable<GameType> =
        combineLatest(
            preferences
                .getString(KEY_TYPE)
                .asObservable()
                .map { it.toInt() },
            preferences
                .getString(KEY_DELAY)
                .asObservable()
                .map { it.toLong() }
                .map { SECONDS.toMillis(it) },
            preferences
                .getString(KEY_INCREMENT_BY)
                .asObservable()
                .map { it.toLong() }
                .map { SECONDS.toMillis(it) },
            Function3<Int, Long, Long, GameType> { type, delay, incrementBy ->
                when (type) {
                    0 -> GameType.Standard
                    1 -> GameType.SimpleDelay(delay)
                    2 -> GameType.BronsteinDelay(delay)
                    3 -> GameType.Fischer(incrementBy)
                    else -> throw IllegalArgumentException("Unsupported game type: $type.")
                }
            }
        )

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