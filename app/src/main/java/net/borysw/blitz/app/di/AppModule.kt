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
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.app.clock.ChessClockImpl
import net.borysw.blitz.app.clock.timer.Timer
import net.borysw.blitz.app.clock.timer.TimerImpl
import net.borysw.blitz.game.GameController
import net.borysw.blitz.game.GameControllerImpl
import net.borysw.blitz.game.settings.GameSettingsProvider
import net.borysw.blitz.game.settings.GameSettingsProviderImpl
import net.borysw.blitz.game.status.GameStatusFactory
import net.borysw.blitz.game.status.GameStatusFactoryImpl
import net.borysw.blitz.game.status.SecondsTimeFormatterImpl
import net.borysw.blitz.game.status.TimeFormatter
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
        fun bindGameController(implementation: GameControllerImpl): GameController

        @Binds
        fun bindGameStatusFactory(implementation: GameStatusFactoryImpl): GameStatusFactory

        @Binds
        fun bindGameSettingsProvider(implementation: GameSettingsProviderImpl): GameSettingsProvider
    }

    @Provides
    fun provideScheduler(): Scheduler = Schedulers.computation()

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    @Provides
    fun provideRxPreferences(sharedPreferences: SharedPreferences): RxSharedPreferences =
        RxSharedPreferences.create(sharedPreferences)
}