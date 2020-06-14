package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.game.clock.ChessClockProvider
import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.dialog.Dialog
import net.borysw.blitz.game.engine.time.TimeEngine
import net.borysw.blitz.game.engine.userActions.UserAction
import net.borysw.blitz.game.engine.userActions.UserAction.ActionButtonClicked
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.userActions.UserAction.ResetConfirmed
import net.borysw.blitz.game.engine.userActions.UserActions
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
                        .map { handleUserAction(it, chessClock) },
                    BiFunction<Long, Dialog, ClockStatus> { _, dialog ->
                        ClockStatus(
                            chessClock.initialTime,
                            chessClock.remainingTimePlayer1,
                            chessClock.remainingTimePlayer2,
                            chessClock.currentPlayer,
                            dialog
                        )
                    }
                )
            }

    private fun handleUserAction(action: UserAction, chessClock: ChessClock): Dialog {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player2)
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (!chessClock.isPaused) chessClock.pause() else return Dialog.ResetConfirmation()
            ResetConfirmed -> chessClock.reset()
        }
        return Dialog.None
    }
}