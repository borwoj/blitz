package net.borysw.blitz.game

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.interval
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.app.clock.ChessClock.Player.Player1
import net.borysw.blitz.app.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatusFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class GameControllerImpl @Inject constructor(
    scheduler: Scheduler,
    private val chessClock: ChessClock,
    private val gameStatusFactory: GameStatusFactory
) : GameController {

    override var userActions: Subject<UserAction> = PublishSubject.create()

    override var game: Game = Game(0)
        set(value) {
            chessClock.initialTime = value.initialTime
            field = value
        }

    override val gameStatus: Observable<GameStatus> =
        Observable.combineLatest(
                interval(1, MILLISECONDS, scheduler),
                userActions
                    .observeOn(scheduler)
                    .doOnNext(::handleUserAction),
                BiFunction<Long, UserAction, Unit> { _, _ -> /* do nothing */ })
            .doOnNext { if (!chessClock.isTimeOver && chessClock.currentPlayer != null) chessClock.advanceTime() }
            .map { gameStatusFactory.getStatus(chessClock) }
            .distinctUntilChanged()

    private fun handleUserAction(action: UserAction) {
        when (action) {
            ClockClickedPlayer1 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player2)
            ClockClickedPlayer2 -> if (!chessClock.isTimeOver) chessClock.changeTurn(Player1)
            ActionButtonClicked -> if (chessClock.currentPlayer == null) chessClock.reset() else chessClock.pause()
        }
    }
}