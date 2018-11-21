package net.borysw.blitz.game.presentation

import net.borysw.blitz.game.ActiveTimer
import net.borysw.blitz.game.ClockStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStatusFactory @Inject constructor(private val timeFormatter: TimeFormatter) {

  fun getGameStatus(clockStatus: ClockStatus, initialTime: Long): GameStatus {
    val status = when {
      clockStatus.timeA == clockStatus.timeB && clockStatus.timeA == initialTime -> GameStatus.Status.INITIAL
      clockStatus.timeA == 0L || clockStatus.timeB == 0L -> GameStatus.Status.FINISHED
      clockStatus.activeTimer == ActiveTimer.A -> GameStatus.Status.PLAYER_A
      else -> GameStatus.Status.PLAYER_B
    }
    val timeA = timeFormatter.format(clockStatus.timeA)
    val timeB = timeFormatter.format(clockStatus.timeB)

    return GameStatus(status, timeA, timeB)
  }
}

data class GameStatus(val status: Status, val timeA: String, val timeB: String) {
  enum class Status {
    INITIAL, FINISHED, PLAYER_A, PLAYER_B
  }
}
