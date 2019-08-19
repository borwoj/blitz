package net.borysw.blitz.app

import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences.create
import net.borysw.blitz.game.ChessClock
import net.borysw.blitz.game.GameStatusFactory
import net.borysw.blitz.game.TimeFormatter
import net.borysw.blitz.game.Timer
import net.borysw.blitz.game.presentation.GameViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single { create(getDefaultSharedPreferences(androidContext())) }

    scope(named("game")) {
        //TODO scope(named<MyActivity>()) {
        scoped { TimeFormatter() }
        scoped { GameStatusFactory(get()) }
        scoped(named("A")) { Timer() }
        scoped(named("B")) { Timer() }
        scoped { ChessClock(get(), get(named("A")), get(named("B"))) }
        viewModel { GameViewModel(get()) }
    }
}