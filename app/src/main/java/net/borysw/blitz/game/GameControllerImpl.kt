package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.Observable.interval
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.app.clock.ChessClock.Player.Player1
import net.borysw.blitz.app.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.settings.GameSettings
import net.borysw.blitz.game.settings.GameSettingsProvider
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatusFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class GameControllerImpl @Inject constructor(
    scheduler: Scheduler,
    gameSettingsProvider: GameSettingsProvider,
    private val chessClock: ChessClock,
    private val gameStatusFactory: GameStatusFactory
) : GameController {

    override val userActions: Subject<UserAction> = BehaviorSubject.create()

    override val gameStatus: Observable<GameStatus> =
        gameSettingsProvider
            .gameSettings
            .subscribeOn(scheduler)
            .doOnNext(::setupGame)
            .flatMap {
                Observable.combineLatest(
                        interval(1, MILLISECONDS, scheduler),
                        userActions
                            .observeOn(scheduler)
                            .doOnNext(::handleUserAction),
                        BiFunction<Long, UserAction, Unit> { _, _ -> /* do nothing */ })
                    .doOnNext { advanceTime() }
                    .map { gameStatusFactory.getStatus(chessClock) }
                    .distinctUntilChanged()
            }

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