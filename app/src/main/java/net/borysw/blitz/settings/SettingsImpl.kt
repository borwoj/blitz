package net.borysw.blitz.settings

import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import net.borysw.blitz.Schedulers.IO
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_BRONSTEIN_DELAY
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_FISCHER
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_SIMPLE_DELAY
import net.borysw.blitz.settings.PreferencesConstants.GAME_TYPE_STANDARD
import net.borysw.blitz.settings.PreferencesConstants.KEY_DELAY
import net.borysw.blitz.settings.PreferencesConstants.KEY_GAME_DURATION
import net.borysw.blitz.settings.PreferencesConstants.KEY_GAME_TYPE
import net.borysw.blitz.settings.PreferencesConstants.KEY_INCREMENT_BY
import net.borysw.blitz.settings.PreferencesConstants.KEY_SOUND_ENABLED
import net.borysw.blitz.settings.PreferencesConstants.KEY_TIME_UNIT
import net.borysw.blitz.settings.PreferencesConstants.TIME_UNIT_HOURS
import net.borysw.blitz.settings.PreferencesConstants.TIME_UNIT_MINUTES
import net.borysw.blitz.settings.PreferencesConstants.TIME_UNIT_SECONDS
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

    private val duration: Observable<Long> =
        combineLatest(
            preferences
                .getString(KEY_GAME_DURATION)
                .asObservable()
                .map { it.toLong() },
            preferences
                .getString(KEY_TIME_UNIT)
                .asObservable()
                .map { timeUnit ->
                    when (timeUnit) {
                        TIME_UNIT_SECONDS -> SECONDS
                        TIME_UNIT_MINUTES -> MINUTES
                        TIME_UNIT_HOURS -> HOURS
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
                .getString(KEY_GAME_TYPE)
                .asObservable(),
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
            Function3<String, Long, Long, GameType> { type, delay, incrementBy ->
                when (type) {
                    GAME_TYPE_STANDARD -> GameType.Standard
                    GAME_TYPE_SIMPLE_DELAY -> GameType.SimpleDelay(delay)
                    GAME_TYPE_BRONSTEIN_DELAY -> GameType.BronsteinDelay(delay)
                    GAME_TYPE_FISCHER -> GameType.Fischer(incrementBy)
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