package net.borysw.blitz.game

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observable.interval
import io.reactivex.rxjava3.core.Scheduler
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatusFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class GameControllerImpl @Inject constructor(
    scheduler: Scheduler,
    private val chessClock: ChessClock,
    private val gameStatusFactory: GameStatusFactory
) : GameController {

    @Volatile
    override var isGamePaused = false
        get() = chessClock.currentPlayer == null

    override var game: Game =
        Game(0)
        set(value) {
            chessClock.initialTime = value.time
            field = value
        }

    override val gameStatus: Observable<GameStatus> = interval(1, MILLISECONDS, scheduler)
        .takeUntil { chessClock.isTimeOver }
        .doOnNext { chessClock.advanceTime() }
        .map { gameStatusFactory.getStatus(chessClock) }
        .distinctUntilChanged()

    override fun onPlayer1Clicked() {
        if (!chessClock.isTimeOver) chessClock.changeTurn(ChessClock.Player.Player2)
    }

    override fun onPlayer2Clicked() {
        if (!chessClock.isTimeOver) chessClock.changeTurn(ChessClock.Player.Player1)
    }

    override fun onPauseClicked() = chessClock.pause()

    override fun onResetClicked() = chessClock.reset()
}