package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Observable.interval
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import net.borysw.blitz.clock.ChessClock
import net.borysw.blitz.clock.ChessClock.Player.Player1
import net.borysw.blitz.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatusFactory
import net.borysw.blitz.settings.GameSettings
import net.borysw.blitz.settings.GameSettingsProvider
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class GameControllerImpl @Inject constructor(
    scheduler: Scheduler,
    gameSettingsProvider: GameSettingsProvider,
    private val chessClock: ChessClock,
    private val gameStatusFactory: GameStatusFactory
) : GameController {

    override val userActions: Subject<UserAction> = BehaviorSubject.create()

    private val gameSettingsObservable = gameSettingsProvider
        .gameSettings
        .subscribeOn(scheduler)
        .doOnNext(::setupGame)

    private val timeEngineObservable = interval(1, MILLISECONDS, scheduler)
        .doOnNext { advanceTime() }

    private val userActionsObservable = userActions
        .observeOn(scheduler)
        .doOnNext(::handleUserAction)

    private val gameStatusObservable = combineLatest<Long, UserAction, Unit>(
        timeEngineObservable,
        userActionsObservable,
        BiFunction { _, _ -> })
        .map { gameStatusFactory.getStatus(chessClock) }
        .distinctUntilChanged()

    override val gameStatus: Observable<GameStatus> =
        gameSettingsObservable
            .flatMap { gameStatusObservable }

    private fun advanceTime() {
        if (!chessClock.isTimeOver && chessClock.currentPlayer != null) chessClock.advanceTime()
    }

    private fun setupGame(gameSettings: GameSettings) {
        chessClock.initialTime = gameSettings.duration
    }

    private fun handleUserAction(action: UserAction) {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player2)
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (chessClock.currentPlayer == null) chessClock.reset() else chessClock.pause()
        }
    }
}