package net.borysw.blitz.game

import net.borysw.blitz.game.ChessClock.ActiveClock
import net.borysw.blitz.game.ChessClock.ActiveClock.*
import net.borysw.blitz.game.GameStatus.Status
import net.borysw.blitz.game.GameStatus.Status.*
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
        of(10, 10, 10, NONE, INITIAL),
        of(10, 9, 10, NONE, PAUSED),
        of(10, 0, 5, CLOCK_A, FINISHED_PLAYER_A),
        of(10, 5, 0, CLOCK_B, FINISHED_PLAYER_B),
        of(10, 1, 5, CLOCK_A, IN_PROGRESS_PLAYER_A),
        of(10, 2, 5, CLOCK_B, IN_PROGRESS_PLAYER_B)
      )
    }
  }

  @ParameterizedTest(name = "when initialTime: {0}, timeLeftA: {1}, timeLeftB: {2}, activeClock: {3}, then return {4}")
  @MethodSource("getArguments")
  @DisplayName("create game status")
  fun getStatus(
    initialTime: Long, timeLeftA: Long, timeLeftB: Long, activeClock: ActiveClock, expectedGameStatus: Status
  ) {
    val timeFormatter = TimeFormatter()
    val testedObj = GameStatusFactory(timeFormatter)

    val gameStatus = testedObj.getStatus(initialTime, timeLeftA, timeLeftB, activeClock).status

    assertEquals(expectedGameStatus, gameStatus)
  }
}