package net.borysw.blitz.game.engine

import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.clock.ChessClock
import net.borysw.blitz.clock.ChessClock.Player.Player1
import net.borysw.blitz.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.audio.SoundEngine
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatusFactory
import net.borysw.blitz.settings.Settings
import javax.inject.Inject
import javax.inject.Named

class GameEngineImpl @Inject constructor(
    @Named(COMPUTATION)
    computationScheduler: Scheduler,
    settings: Settings,
    timeEngine: TimeEngine,
    soundEngine: SoundEngine,
    private val chessClock: ChessClock,
    private val gameStatusFactory: GameStatusFactory
) : GameEngine {

    override val userActions: Subject<UserAction> = BehaviorSubject.create()

    private val gameSettingsObservable =
        settings.gameSettings
            .observeOn(computationScheduler)
            .doOnNext(::setupGame)

    private val timeEngineObservable =
        timeEngine.time
            .observeOn(computationScheduler)
            .doOnNext { if (!chessClock.isTimeOver && chessClock.currentPlayer != null) chessClock.advanceTime() }

    private val userActionsObservable =
        userActions
            .compose(soundEngine)
            .observeOn(computationScheduler)
            .doOnNext(::handleUserAction)

    private val gameStatusObservable = combineLatest(
        timeEngineObservable,
        userActionsObservable,
        BiFunction<Long, UserAction, Unit> { _, _ -> })
        .map { gameStatusFactory.getStatus(chessClock) }
        .distinctUntilChanged()

    override val gameStatus: Observable<GameStatus> =
        gameSettingsObservable
            .flatMap { gameStatusObservable }

    private fun setupGame(gameSettings: Settings.GameSettings) {
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