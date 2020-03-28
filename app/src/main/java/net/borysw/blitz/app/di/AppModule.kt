package net.borysw.blitz.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.app.clock.ChessClockImpl
import net.borysw.blitz.app.clock.timer.Timer
import net.borysw.blitz.app.clock.timer.TimerImpl
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
    }

    @Provides
    fun provideScheduler(): Scheduler = Schedulers.computation()
}