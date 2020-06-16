package net.borysw.blitz.game.engine.game

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Observable.just
import io.reactivex.schedulers.Schedulers.trampoline
import net.borysw.blitz.analytics.Analytics
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.audio.SoundEngine
import net.borysw.blitz.game.engine.clock.ChessClockEngine
import net.borysw.blitz.game.engine.clock.ClockStatus
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserActions
import net.borysw.blitz.game.status.GameInfo
import net.borysw.blitz.game.status.GameInfo.Status.Finished.Player2Won
import net.borysw.blitz.game.status.GameInfoCreator
import org.junit.jupiter.api.Test

internal class GameEngineImplTest {

    @Test
    fun getGameInfo() {
        val clockStatus = ClockStatus(1, 1, 1, Player1)
        val chessClockEngine = mock<ChessClockEngine> {
            on(it.clockStatus).thenReturn(just(clockStatus))
        }
        val soundEngine = mock<SoundEngine> {
            on(it.sound).thenReturn(just(Unit))
        }
        val userActions = mock<UserActions> {
            on(it.userActions).thenReturn(just(ClockClickedPlayer1))
        }
        val gameInfoCreator = mock<GameInfoCreator> {
            on(it.get(clockStatus)).thenReturn(GameInfo("foo", "bar", Player2Won))
        }
        val analytics = mock<Analytics>()
        val testedObj = GameEngineImpl(
            chessClockEngine,
            soundEngine,
            userActions,
            gameInfoCreator,
            analytics,
            trampoline()
        )

        testedObj.gameInfo.test().assertValue(GameInfo("foo", "bar", Player2Won))
        verify(analytics).logEvent(any(), any())
    }
}