package net.borysw.blitz.app.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.Schedulers.IO
import net.borysw.blitz.clock.ChessClock
import net.borysw.blitz.clock.ChessClockImpl
import net.borysw.blitz.clock.timer.Timer
import net.borysw.blitz.clock.timer.TimerImpl
import net.borysw.blitz.game.audio.SoundEngine
import net.borysw.blitz.game.audio.SoundEngineImpl
import net.borysw.blitz.game.audio.SoundPlayer
import net.borysw.blitz.game.audio.SoundPlayerImpl
import net.borysw.blitz.game.engine.GameEngine
import net.borysw.blitz.game.engine.GameEngineImpl
import net.borysw.blitz.game.engine.TimeEngine
import net.borysw.blitz.game.engine.TimeEngineImpl
import net.borysw.blitz.game.status.GameStatusFactory
import net.borysw.blitz.game.status.GameStatusFactoryImpl
import net.borysw.blitz.game.status.SecondsTimeFormatterImpl
import net.borysw.blitz.game.status.TimeFormatter
import net.borysw.blitz.settings.Settings
import net.borysw.blitz.settings.SettingsImpl
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule.Binding::class])
class AppModule {

    @Module
    interface Binding {
        @Binds
        @Singleton
        fun bindChessClock(implementation: ChessClockImpl): ChessClock

        @Binds
        fun bindTimer(implementation: TimerImpl): Timer

        @Binds
        fun bindTimeFormatter(implementation: SecondsTimeFormatterImpl): TimeFormatter

        @Binds
        fun bindGameEngine(implementation: GameEngineImpl): GameEngine

        @Binds
        fun bindGameStatusFactory(implementation: GameStatusFactoryImpl): GameStatusFactory

        @Binds
        fun bindGameSettingsProvider(implementation: SettingsImpl): Settings

        @Binds
        fun bindTimeEngine(implementation: TimeEngineImpl): TimeEngine

        @Binds
        fun bindSoundPlayer(implementation: SoundPlayerImpl): SoundPlayer

        @Binds
        fun bindSoundEngine(implementation: SoundEngineImpl): SoundEngine
    }

    @Provides
    @Named(COMPUTATION)
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @Provides
    @Named(IO)
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    fun provideRxPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences =
        RxSharedPreferences.create(sharedPreferences)
}