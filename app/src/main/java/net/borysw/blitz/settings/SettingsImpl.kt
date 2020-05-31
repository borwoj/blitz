package net.borysw.blitz.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
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
        const val KEY_DELAY = "delay"
        const val KEY_INCREMENT_BY = "increment_by"
    }

    private val duration: Observable<Long> =
        combineLatest(rxSharedPreferences
            .getString(KEY_DURATION)
            .asObservable()
            .map { it.toLong() },
            rxSharedPreferences
                .getString(KEY_TIME_UNIT)
                .asObservable()
                .map { it.toInt() }
                .map { timeUnit ->
                    when (timeUnit) {
                        0 -> TimeUnit.SECONDS
                        1 -> TimeUnit.MINUTES
                        2 -> TimeUnit.HOURS
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
        combineLatest(
            rxSharedPreferences
                .getString(KEY_TYPE)
                .asObservable()
                .map { it.toInt() },
            rxSharedPreferences
                .getString(KEY_DELAY)
                .asObservable()
                .map { it.toLong() }
                .map { TimeUnit.SECONDS.toMillis(it) },
            rxSharedPreferences
                .getString(KEY_INCREMENT_BY)
                .asObservable()
                .map { it.toLong() }
                .map { TimeUnit.SECONDS.toMillis(it) },
            Function3<Int, Long, Long, GameType> { type, delay, incrementBy ->
                when (type) {
                    0 -> GameType.Standard
                    1 -> GameType.SimpleDelay(delay)
                    2 -> GameType.BronsteinDelay(delay)
                    3 -> GameType.Increment(incrementBy)
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