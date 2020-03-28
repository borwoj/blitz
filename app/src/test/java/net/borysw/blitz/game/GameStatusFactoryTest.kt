package net.borysw.blitz.game

import net.borysw.blitz.game.GameStatus.Status
import net.borysw.blitz.game.GameStatus.Status.FINISHED_PLAYER_A
import net.borysw.blitz.game.GameStatus.Status.FINISHED_PLAYER_B
import net.borysw.blitz.game.GameStatus.Status.INITIAL
import net.borysw.blitz.game.GameStatus.Status.IN_PROGRESS_PLAYER_A
import net.borysw.blitz.game.GameStatus.Status.IN_PROGRESS_PLAYER_B
import net.borysw.blitz.game.GameStatus.Status.PAUSED
import net.borysw.blitz.game.presentation.ChessClock
import net.borysw.blitz.game.presentation.ChessClock.Player.FIRST
import net.borysw.blitz.game.presentation.ChessClock.Player.SECOND
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
                of(10, 0, 5, FIRST, FINISHED_PLAYER_A),
                of(10, 5, 0, SECOND, FINISHED_PLAYER_B),
                of(10, 1, 5, FIRST, IN_PROGRESS_PLAYER_A),
                of(10, 2, 5, SECOND, IN_PROGRESS_PLAYER_B)
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
        val testedObj = GameStatusFactory(TimeFormatter())
        val gameStatus = testedObj.getStatus(initialTime, timeLeftA, timeLeftB, current).status
        assertEquals(expectedGameStatus, gameStatus)
    }
}