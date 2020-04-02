package net.borysw.blitz.game.audio

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.R
import net.borysw.blitz.Schedulers.IO
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.settings.Settings
import javax.inject.Inject
import javax.inject.Named

class SoundEngineImpl @Inject constructor(
    private val soundPlayer: SoundPlayer,
    private val settings: Settings,
    @Named(IO) private val ioScheduler: Scheduler
) :
    SoundEngine, ObservableTransformer<UserAction, UserAction> {

    override fun apply(userActions: Observable<UserAction>): ObservableSource<UserAction> =
        Observable.combineLatest<UserAction, Settings.AppSettings, UserAction>(
            userActions.observeOn(ioScheduler),
            settings.appSettings.observeOn(ioScheduler),
            BiFunction { userAction, appSettings ->
                if (appSettings.soundEnabled) {
                    when (userAction) {
                        ClockClickedPlayer1, ClockClickedPlayer2 -> soundPlayer.playSound(R.raw.clock_press_1)
                    }
                }
                userAction
            })
}