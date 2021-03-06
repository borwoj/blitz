package net.borysw.blitz.game.engine.clock

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.analytics.Analytics
import net.borysw.blitz.analytics.AnalyticsConstants.EVENT_TIME_OVER
import net.borysw.blitz.analytics.AnalyticsConstants.PARAM_GAME_DURATION
import net.borysw.blitz.game.clock.ChessClockProvider
import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.dialog.Dialog
import net.borysw.blitz.game.engine.dialog.Dialogs
import net.borysw.blitz.game.engine.time.TimeEngine
import net.borysw.blitz.game.engine.userActions.UserAction
import net.borysw.blitz.game.engine.userActions.UserAction.ActionButtonClicked
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.userActions.UserAction.ResetConfirmed
import net.borysw.blitz.game.engine.userActions.UserActions
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Named

class ChessClockEngineImpl @Inject constructor(
    userActions: UserActions,
    timeEngine: TimeEngine,
    chessClockProvider: ChessClockProvider,
    @Named(COMPUTATION)
    computationScheduler: Scheduler,
    private val dialogs: Dialogs,
    private val analytics: Analytics
) : ChessClockEngine {
    private val timeEngineToggle = PublishSubject.create<Boolean>()

    override val clockStatus: Observable<ClockStatus> =
        chessClockProvider
            .chessClock
            .observeOn(computationScheduler)
            .switchMap { chessClock ->
                combineLatest(
                    timeEngineToggle
                        .observeOn(computationScheduler)
                        .distinctUntilChanged()
                        .startWith(false)
                        .switchMap { shouldRunTimeEngine ->
                            if (shouldRunTimeEngine)
                                timeEngine
                                    .time
                                    .observeOn(computationScheduler)
                                    .filter { !chessClock.isTimeOver && !chessClock.isPaused }
                                    .doOnNext { chessClock.advanceTime() }
                                    .doOnNext {
                                        if (chessClock.isTimeOver) logGameFinishedEvent(
                                            chessClock.initialTime
                                        )
                                    }
                                    .map { Unit }
                            else Observable.just(Unit)
                        },
                    userActions
                        .userActions
                        .observeOn(computationScheduler)
                        .doOnNext { handleUserAction(it, chessClock) }
                        .doOnNext { timeEngineToggle.onNext(!chessClock.isTimeOver && !chessClock.isPaused) },
                    BiFunction<Unit, UserAction, ClockStatus> { _, _ ->
                        chessClock.status
                    }
                )
            }

    private fun handleUserAction(action: UserAction, chessClock: ChessClock) {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player2)
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (!chessClock.isPaused) chessClock.pause() else if (!chessClock.isTimeOver)
                dialogs.showDialog(Dialog.ResetConfirmation()) else chessClock.reset()
            ResetConfirmed -> chessClock.reset()
        }
    }

    private fun logGameFinishedEvent(initialTime: Long) {
        analytics.logEvent(EVENT_TIME_OVER, Bundle().apply {
            putLong(
                PARAM_GAME_DURATION,
                MILLISECONDS.toSeconds(initialTime)
            )
        })
    }
}