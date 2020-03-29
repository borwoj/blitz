package net.borysw.blitz.game

import net.borysw.blitz.clock.ChessClock
import net.borysw.blitz.clock.ChessClock.Player.PLAYER_1
import net.borysw.blitz.clock.ChessClock.Player.PLAYER_2
import net.borysw.blitz.game.status.GameStatus.Status
import net.borysw.blitz.game.status.GameStatus.Status.FINISHED_PLAYER_A
import net.borysw.blitz.game.status.GameStatus.Status.FINISHED_PLAYER_B
import net.borysw.blitz.game.status.GameStatus.Status.INITIAL
import net.borysw.blitz.game.status.GameStatus.Status.IN_PROGRESS_PLAYER_A
import net.borysw.blitz.game.status.GameStatus.Status.IN_PROGRESS_PLAYER_B
import net.borysw.blitz.game.status.GameStatus.Status.PAUSED
import net.borysw.blitz.game.status.GameStatusFactory
import net.borysw.blitz.game.status.SecondsTimeFormatterImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource

internal class GameStatusFactoryTest {
    companion object {
        @JvmStatic
        fun getArguments(): List<Arguments> {
            return listOf(
                of(10, 10, 10, null, INITIAL),
                of(10, 9, 10, null, PAUSED),
                of(10, 0, 5, PLAYER_1, FINISHED_PLAYER_A),
                of(10, 5, 0, PLAYER_2, FINISHED_PLAYER_B),
                of(10, 1, 5, PLAYER_1, IN_PROGRESS_PLAYER_A),
                of(10, 2, 5, PLAYER_2, IN_PROGRESS_PLAYER_B)
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
        val testedObj = GameStatusFactory(
            SecondsTimeFormatterImpl()
        )
        val gameStatus = testedObj.getStatus(initialTime, timeLeftA, timeLeftB, current).status
        assertEquals(expectedGameStatus, gameStatus)
    }
}