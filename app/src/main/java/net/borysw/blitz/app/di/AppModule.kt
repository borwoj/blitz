package net.borysw.blitz.app.di

import dagger.Binds
import dagger.Module
import net.borysw.blitz.game.presentation.ChessClock
import net.borysw.blitz.game.presentation.ChessClockImpl
import javax.inject.Singleton

@Module(includes = [AppModule.Binding::class])
class AppModule {

    @Module
    interface Binding {
        @Binds
        @Singleton
        fun bindChessClock(implementation: ChessClockImpl): ChessClock
    }
}