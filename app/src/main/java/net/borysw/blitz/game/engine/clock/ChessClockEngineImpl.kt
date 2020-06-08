package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.UserActions
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.time.TimeEngine
import javax.inject.Inject
import javax.inject.Named

class ChessClockEngineImpl @Inject constructor(
    userActions: UserActions,
    timeEngine: TimeEngine,
    chessClockProvider: ChessClockProvider,
    @Named(COMPUTATION)
    computationScheduler: Scheduler
) : ChessClockEngine {

    private lateinit var chessClock: ChessClock

    override val clockStatus: Observable<ClockStatus> =

        chessClockProvider
            .chessClock
            .observeOn(computationScheduler)
            .doOnNext { chessClock = it }
            .switchMap {
                Observable.combineLatest(
                    timeEngine
                        .time
                        .observeOn(computationScheduler)
                        .doOnNext { if (!chessClock.isTimeOver && !chessClock.isPaused) chessClock.advanceTime() },
                    userActions
                        .userActions
                        .observeOn(computationScheduler)
                        .doOnNext(::handleUserAction),
                    BiFunction<Long, UserAction, Unit> { _, _ -> }
                ).map {
                    ClockStatus(
                        chessClock.initialTime,
                        chessClock.remainingTimePlayer1,
                        chessClock.remainingTimePlayer2,
                        chessClock.remainingDelayTimePlayer1,
                        chessClock.remainingDelayTimePlayer2,
                        chessClock.currentPlayer
                    )
                }
            }

    private fun handleUserAction(action: UserAction) {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player2)
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (chessClock.isPaused) chessClock.reset() else chessClock.pause()
        }
    }
}