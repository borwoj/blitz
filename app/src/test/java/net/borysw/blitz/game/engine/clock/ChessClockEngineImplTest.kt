package net.borysw.blitz.game.engine.clock

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import net.borysw.blitz.game.clock.ChessClockProvider
import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.dialog.Dialog
import net.borysw.blitz.game.engine.dialog.Dialogs
import net.borysw.blitz.game.engine.time.TimeEngine
import net.borysw.blitz.game.engine.userActions.UserAction
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.userActions.UserActions
import org.junit.jupiter.api.Test

internal class ChessClockEngineImplTest {

    @Test
    fun getClockStatus() {
        val dialogs = mock<Dialogs>()
        val userActionsSubject = PublishSubject.create<UserAction>()
        val userActions = mock<UserActions> {
            on(it.userActions).thenReturn(userActionsSubject)
        }
        val timeSubject = PublishSubject.create<Long>()
        val timeEngine = mock<TimeEngine> {
            on(it.time).thenReturn(timeSubject)
        }
        val chessClock = mock<ChessClock> {
            on(it.initialTime).thenReturn(1)
            on(it.remainingTimePlayer1).thenReturn(1)
            on(it.remainingTimePlayer2).thenReturn(1)
            on(it.currentPlayer).thenReturn(Player2).thenReturn(Player1)
        }
        val chessClockSubject = PublishSubject.create<ChessClock>()
        val chessClockProvider = mock<ChessClockProvider> {
            on(it.chessClock).thenReturn(chessClockSubject)
        }
        val testedObj =
            ChessClockEngineImpl(userActions, timeEngine, chessClockProvider, dialogs, trampoline())

        val testObserver = testedObj.clockStatus.test()

        chessClockSubject.onNext(chessClock)

        whenever(chessClock.isTimeOver).thenReturn(false)
        whenever(chessClock.isPaused).thenReturn(false)

        userActionsSubject.onNext(ClockClickedPlayer1)
        verify(chessClock).changeTurn(Player2)

        timeSubject.onNext(1)
        verify(chessClock).advanceTime()


        userActionsSubject.onNext(ClockClickedPlayer2)
        verify(chessClock).changeTurn(Player1)

        userActionsSubject.onNext(UserAction.ActionButtonClicked)
        verify(chessClock).pause()

        whenever(chessClock.isPaused).thenReturn(true)

        userActionsSubject.onNext(UserAction.ActionButtonClicked)
        verify(dialogs).showDialog(any<Dialog.ResetConfirmation>())

        whenever(chessClock.isTimeOver).thenReturn(true)

        userActionsSubject.onNext(UserAction.ActionButtonClicked)
        verify(chessClock).reset()

        testObserver.assertValues(
            ClockStatus(1, 1, 1, Player2),
            ClockStatus(1, 1, 1, Player1),
            ClockStatus(1, 1, 1, Player1),
            ClockStatus(1, 1, 1, Player1),
            ClockStatus(1, 1, 1, Player1),
            ClockStatus(1, 1, 1, Player1),
            ClockStatus(1, 1, 1, Player1)
        )
    }
}