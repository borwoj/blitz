package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.clock.ChessClockProvider
import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.UserActions
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

    override val clockStatus: Observable<ClockStatus> =
        chessClockProvider
            .chessClock
            .observeOn(computationScheduler)
            .switchMap { chessClock ->
                combineLatest(
                    timeEngine
                        .time
                        .observeOn(computationScheduler)
                        .doOnNext { if (!chessClock.isTimeOver && !chessClock.isPaused) chessClock.advanceTime() },
                    userActions
                        .userActions
                        .observeOn(computationScheduler)
                        .doOnNext { handleUserAction(it, chessClock) },
                    BiFunction<Long, UserAction, Unit> { _, _ -> }
                ).map {
                    ClockStatus(
                        chessClock.initialTime,
                        chessClock.remainingTimePlayer1,
                        chessClock.remainingTimePlayer2,
                        chessClock.currentPlayer
                    )
                }
            }

    private fun handleUserAction(action: UserAction, chessClock: ChessClock) {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player2)
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (chessClock.isPaused) chessClock.reset() else chessClock.pause()
        }
    }
}