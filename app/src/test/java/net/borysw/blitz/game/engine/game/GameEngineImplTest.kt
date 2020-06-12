package net.borysw.blitz.game.engine.game

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable.just
import io.reactivex.schedulers.Schedulers.trampoline
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.UserActions
import net.borysw.blitz.game.engine.audio.SoundEngine
import net.borysw.blitz.game.engine.clock.ChessClockEngine
import net.borysw.blitz.game.engine.clock.ClockStatus
import net.borysw.blitz.game.status.GameInfo
import net.borysw.blitz.game.status.GameInfo.Status.InProgress.Player2Turn
import net.borysw.blitz.game.status.GameInfoCreator
import net.borysw.blitz.settings.GameType
import net.borysw.blitz.settings.Settings
import org.junit.jupiter.api.Test

internal class GameEngineImplTest {

    @Test
    fun getGameInfo() {
        val settings = mock<Settings> {
            on(it.gameSettings).thenReturn(just(Settings.GameSettings(1, GameType.Standard)))
        }
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
            on(it.get(clockStatus)).thenReturn(GameInfo("foo", "bar", Player2Turn))
        }
        val testedObj = GameEngineImpl(
            settings,
            chessClockEngine,
            soundEngine,
            userActions,
            gameInfoCreator,
            trampoline()
        )

        testedObj.gameInfo.test().assertValue(GameInfo("foo", "bar", Player2Turn))
    }
}