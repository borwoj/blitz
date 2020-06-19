package net.borysw.blitz.game.engine.game

import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Observable.combineLatest
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import net.borysw.blitz.Schedulers.COMPUTATION
import net.borysw.blitz.analytics.Analytics
import net.borysw.blitz.analytics.AnalyticsConstants.EVENT_GAME_FINISHED
import net.borysw.blitz.analytics.AnalyticsConstants.PARAM_GAME_DURATION
import net.borysw.blitz.game.engine.audio.SoundEngine
import net.borysw.blitz.game.engine.clock.ChessClockEngine
import net.borysw.blitz.game.engine.clock.ClockStatus
import net.borysw.blitz.game.engine.userActions.UserAction
import net.borysw.blitz.game.engine.userActions.UserActions
import net.borysw.blitz.game.status.GameInfo
import net.borysw.blitz.game.status.GameInfoCreator
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Named

class GameEngineImpl @Inject constructor(
    chessClockEngine: ChessClockEngine,
    soundEngine: SoundEngine,
    userActions: UserActions,
    private val gameInfoCreator: GameInfoCreator,
    private val analytics: Analytics,
    @Named(COMPUTATION)
    computationScheduler: Scheduler
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
                gameInfoCreator.get(clockStatus).apply {
                    logEvent(this, clockStatus)
                }
            })
            .distinctUntilChanged()

    private fun logEvent(gameInfo: GameInfo, clockStatus: ClockStatus) {
        if (gameInfo.status is GameInfo.Status.Finished)
            analytics.logEvent(
                EVENT_GAME_FINISHED,
                Bundle().apply {
                    putLong(PARAM_GAME_DURATION, MILLISECONDS.toSeconds(clockStatus.initialTime))
                })
    }
}