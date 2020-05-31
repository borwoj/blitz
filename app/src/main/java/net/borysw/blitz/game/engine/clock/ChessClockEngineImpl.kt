package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.UserActions
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.time.TimeEngine
import net.borysw.blitz.settings.Settings
import javax.inject.Inject
import javax.inject.Named

class ChessClockEngineImpl @Inject constructor(
    userActions: UserActions,
    timeEngine: TimeEngine,
    settings: Settings,
    @Named(COMPUTATION)
    computationScheduler: Scheduler,
    private val chessClock: ChessClock
) : ChessClockEngine {

    override val clockStatus: Observable<ClockStatus> = Observable.combineLatest(
        timeEngine
            .time
            .observeOn(computationScheduler)
            .doOnNext { if (!chessClock.isTimeOver && chessClock.currentPlayer != null) chessClock.advanceTime() },
        settings
            .gameSettings
            .observeOn(computationScheduler)
            .doOnNext { chessClock.reset() }
            .doOnNext { chessClock.gameType = it.type }
            .doOnNext { chessClock.initialTime = it.duration },
        userActions
            .userActions
            .observeOn(computationScheduler)
            .doOnNext(::handleUserAction),
        Function3<Long, Settings.GameSettings, UserAction, ClockStatus> { _, _, _ ->
            ClockStatus(
                chessClock.initialTime,
                chessClock.remainingTimePlayer1,
                chessClock.remainingTimePlayer2,
                chessClock.currentPlayer
            )
        }
    )

    private fun handleUserAction(action: UserAction) {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) {
                chessClock.changeTurn(Player2)
            }
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (chessClock.currentPlayer == null) chessClock.reset() else chessClock.pause()
        }
    }
}