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
import net.borysw.blitz.game.clock.ChessClockProvider
import net.borysw.blitz.game.clock.ChessClockProviderImpl
import net.borysw.blitz.game.clock.timer.Timer
import net.borysw.blitz.game.clock.timer.TimerImpl
import net.borysw.blitz.game.clock.type.BasicChessClockImpl
import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.engine.UserActions
import net.borysw.blitz.game.engine.UserActionsImpl
import net.borysw.blitz.game.engine.audio.SoundEngine
import net.borysw.blitz.game.engine.audio.SoundEngineImpl
import net.borysw.blitz.game.engine.audio.SoundPlayer
import net.borysw.blitz.game.engine.audio.SoundPlayerImpl
import net.borysw.blitz.game.engine.clock.ChessClockEngine
import net.borysw.blitz.game.engine.clock.ChessClockEngineImpl
import net.borysw.blitz.game.engine.game.GameEngine
import net.borysw.blitz.game.engine.game.GameEngineImpl
import net.borysw.blitz.game.engine.time.TimeEngine
import net.borysw.blitz.game.engine.time.TimeEngineImpl
import net.borysw.blitz.game.status.GameInfoCreator
import net.borysw.blitz.game.status.GameInfoCreatorImpl
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
        fun bindChessClock(implementation: BasicChessClockImpl): ChessClock

        @Binds
        fun bindTimer(implementation: TimerImpl): Timer

        @Binds
        fun bindTimeFormatter(implementation: SecondsTimeFormatterImpl): TimeFormatter

        @Binds
        @Singleton
        fun bindGameEngine(implementation: GameEngineImpl): GameEngine

        @Binds
        fun bindGameStatusFactory(implementation: GameInfoCreatorImpl): GameInfoCreator

        @Binds
        fun bindGameSettingsProvider(implementation: SettingsImpl): Settings

        @Binds
        @Singleton
        fun bindTimeEngine(implementation: TimeEngineImpl): TimeEngine

        @Binds
        fun bindSoundPlayer(implementation: SoundPlayerImpl): SoundPlayer

        @Binds
        @Singleton
        fun bindSoundEngine(implementation: SoundEngineImpl): SoundEngine

        @Binds
        @Singleton
        fun bindChessClockEngine(implementation: ChessClockEngineImpl): ChessClockEngine

        @Binds
        fun bindChessClockProvider(implementation: ChessClockProviderImpl): ChessClockProvider

        @Binds
        @Singleton
        fun bindUserActions(implementation: UserActionsImpl): UserActions
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