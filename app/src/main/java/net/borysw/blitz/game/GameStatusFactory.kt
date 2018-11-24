package net.borysw.blitz.game

import net.borysw.blitz.game.ClockStatus.ActiveTimer.TIMER_A
import net.borysw.blitz.game.GameStatus.Status.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStatusFactory @Inject constructor(private val timeFormatter: TimeFormatter) {
  fun getGameStatus(clockStatus: ClockStatus, initialTime: Long): GameStatus {
    val status = when {
      (clockStatus.timeA == initialTime && clockStatus.timeB == initialTime) || (!clockStatus.isRunning &&
          (clockStatus.timeA != 0L && clockStatus.timeB != 0L)) -> INITIAL
      clockStatus.timeA == 0L -> FINISHED_PLAYER_A
      clockStatus.timeB == 0L -> FINISHED_PLAYER_B
      clockStatus.activeTimer == TIMER_A -> IN_PROGRESS_PLAYER_A
      else -> IN_PROGRESS_PLAYER_B
    }
    val timeA = timeFormatter.format(clockStatus.timeA)
    val timeB = timeFormatter.format(clockStatus.timeB)

    return GameStatus(status, timeA, timeB)
  }
}