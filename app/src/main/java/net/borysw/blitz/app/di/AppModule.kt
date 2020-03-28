package net.borysw.blitz.app.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import net.borysw.blitz.game.presentation.ChessClock
import net.borysw.blitz.game.presentation.ChessClockImpl
import net.borysw.blitz.game.presentation.Timer
import net.borysw.blitz.game.presentation.TimerImpl
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
    }

    @Provides
    fun provideScheduler(): Scheduler = Schedulers.computation()
}