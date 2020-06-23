package net.borysw.blitz.game.engine.game

import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.engine.audio.SoundEngine
import net.borysw.blitz.game.engine.clock.ChessClockEngine
import net.borysw.blitz.game.engine.userActions.UserAction
import net.borysw.blitz.game.engine.userActions.UserActions
import net.borysw.blitz.game.status.GameInfo
import net.borysw.blitz.game.status.GameInfoCreator
import javax.inject.Inject
import javax.inject.Named

class GameEngineImpl @Inject constructor(
    chessClockEngine: ChessClockEngine,
    soundEngine: SoundEngine,
    userActions: UserActions,
    @Named(COMPUTATION)
    computationScheduler: Scheduler,
    private val gameInfoCreator: GameInfoCreator
) : GameEngine {

    private val soundEngineObservable =
        soundEngine.sound
            .observeOn(computationScheduler)

    private val userActionsObservable =
        userActions.userActions
            .observeOn(computationScheduler)

    private val clockEngineObservable =
        chessClockEngine.clockStatus
            .observeOn(computationScheduler)

    override val gameInfo: Observable<GameInfo> =
        combineLatest(
            userActionsObservable,
            soundEngineObservable,
            clockEngineObservable,
            Function3<UserAction, Unit, ClockStatus, GameInfo> { _, _, clockStatus ->
                gameInfoCreator.get(clockStatus)
            })
            .distinctUntilChanged()
}