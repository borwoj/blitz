package net.borysw.blitz.game

import net.borysw.blitz.game.engine.clock.ChessClock
import net.borysw.blitz.game.status.GameInfo.Status
import net.borysw.blitz.game.status.GameInfo.Status.Finished
import net.borysw.blitz.game.status.GameInfo.Status.InProgress
import net.borysw.blitz.game.status.GameInfo.Status.Paused
import net.borysw.blitz.game.status.GameInfo.Status.Unstarted
import net.borysw.blitz.game.status.GameInfoCreatorImpl
import net.borysw.blitz.game.status.SecondsTimeFormatterImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

internal class GameInfoCreatorImplTest {
    companion object {
        @JvmStatic
        fun getArguments(): List<Arguments> {
            return listOf(
                of(10, 10, 10, null, Unstarted),
                of(10, 9, 10, null, Paused),
                of(10, 0, 5, ChessClock.Player.Player1, Finished.Player1Won),
                of(10, 5, 0, ChessClock.Player.Player2, Finished.Player2Won),
                of(10, 1, 5, ChessClock.Player.Player1, InProgress.Player1Turn),
                of(10, 2, 5, ChessClock.Player.Player2, InProgress.Player2Turn)
            )
        }
    }

    @ParameterizedTest(name = "when initialTime: {0}, timeLeftA: {1}, timeLeftB: {2}, current: {3}, then return {4}")
    @MethodSource("getArguments")
    @DisplayName("create game status")
    fun getStatus(
        initialTime: Long,
        timeLeftA: Long,
        timeLeftB: Long,
        current: ChessClock.Player?,
        expectedGameStatus: Status
    ) {
        val testedObj = GameInfoCreatorImpl(SecondsTimeFormatterImpl())
        val gameStatus = testedObj.get(initialTime, timeLeftA, timeLeftB, current).status
        assertEquals(expectedGameStatus, gameStatus)
    }
}