package net.borysw.blitz.game

import net.borysw.blitz.game.ChessClock.ActiveClock
import net.borysw.blitz.game.ChessClock.ActiveClock.*
import net.borysw.blitz.game.GameStatus.Status.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameStatusFactory @Inject constructor(private val timeFormatter: TimeFormatter) {
  fun getStatus(initialTime: Long, timeLeftA: Long, timeLeftB: Long, activeClock: ActiveClock): GameStatus {
    val status = when {
      (initialTime == timeLeftA && initialTime == timeLeftB) || (activeClock == NONE) -> PAUSED
      timeLeftA == 0L -> FINISHED_PLAYER_A
      timeLeftB == 0L -> FINISHED_PLAYER_B
      activeClock == CLOCK_A -> IN_PROGRESS_PLAYER_A
      activeClock == CLOCK_B -> IN_PROGRESS_PLAYER_B
      else -> throw IllegalStateException("Failed to create status")
    }
    val timeA = timeFormatter.format(timeLeftA)
    val timeB = timeFormatter.format(timeLeftB)

    return GameStatus(status, timeA, timeB)
  }
}