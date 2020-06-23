package net.borysw.blitz.game

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import net.borysw.blitz.game.status.GameInfo
import net.borysw.blitz.game.status.GameInfo.Status
import net.borysw.blitz.game.status.GameInfo.Status.Finished
import net.borysw.blitz.game.status.GameInfo.Status.InProgress
import net.borysw.blitz.game.status.GameInfo.Status.Paused
import net.borysw.blitz.game.status.GameInfo.Status.Unstarted
import net.borysw.blitz.game.status.GameInfoCreatorImpl
import net.borysw.blitz.game.status.TimeFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

internal class GameInfoCreatorImplTest {
    companion object {
        @JvmStatic
        fun parameters(): List<Arguments> = listOf(
            of(ClockStatus(10, 10, 10, null), Unstarted),
            of(ClockStatus(10, 9, 10, null), Paused),
            of(ClockStatus(10, 0, 5, Player1), Finished.Player1Won),
            of(ClockStatus(10, 5, 0, Player2), Finished.Player2Won),
            of(ClockStatus(10, 1, 5, Player1), InProgress.Player1Turn),
            of(ClockStatus(10, 2, 5, Player2), InProgress.Player2Turn)
        )
    }

    @ParameterizedTest(name = "when clock status: {0}, then game info should be: {1}")
    @MethodSource("parameters")
    @DisplayName("create game status")
    fun getInfo(clockStatus: ClockStatus, expectedGameStatus: Status) {
        val timeFormatter = mock<TimeFormatter> {
            on(it.format(any())).thenReturn("0")
        }
        val testedObj = GameInfoCreatorImpl(timeFormatter)

        assertEquals(
            GameInfo(
                remainingTimePlayer1 = "0",
                remainingTimePlayer2 = "0",
                status = expectedGameStatus
            ), testedObj.get(clockStatus)
        )
    }
}