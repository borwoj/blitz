package net.borysw.blitz.game

import net.borysw.blitz.game.ClockStatus.ActiveTimer.A
import net.borysw.blitz.game.GameStatus.Status.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStatusFactory @Inject constructor(private val timeFormatter: TimeFormatter) {
  fun getGameStatus(clockStatus: ClockStatus, initialTime: Long): GameStatus {
    val status = when {
      clockStatus.timeA == clockStatus.timeB && clockStatus.timeA == initialTime -> INITIAL
      clockStatus.timeA == 0L || clockStatus.timeB == 0L -> FINISHED
      clockStatus.activeTimer == A -> PLAYER_A
      else -> PLAYER_B
    }
    val timeA = timeFormatter.format(clockStatus.timeA)
    val timeB = timeFormatter.format(clockStatus.timeB)

    return GameStatus(status, timeA, timeB)
  }
}