package net.borysw.blitz.game.presentation

import net.borysw.blitz.game.ActiveTimer
import net.borysw.blitz.game.ClockStatus
import net.borysw.blitz.game.presentation.GameStatus.*
import net.borysw.blitz.game.presentation.GameStatus.Status.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStatusFactory @Inject constructor(private val timeFormatter: TimeFormatter) {
  fun getGameStatus(clockStatus: ClockStatus, initialTime: Long): GameStatus {
    val status = when {
      clockStatus.timeA == clockStatus.timeB && clockStatus.timeA == initialTime -> INITIAL
      clockStatus.timeA == 0L || clockStatus.timeB == 0L -> FINISHED
      clockStatus.activeTimer == ActiveTimer.A -> PLAYER_A
      else -> PLAYER_B
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
