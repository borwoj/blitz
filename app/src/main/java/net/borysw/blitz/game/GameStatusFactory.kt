package net.borysw.blitz.game

import net.borysw.blitz.game.ChessClock.ActiveClock
import net.borysw.blitz.game.ChessClock.ActiveClock.CLOCK_A
import net.borysw.blitz.game.ChessClock.ActiveClock.CLOCK_B
import net.borysw.blitz.game.ChessClock.ActiveClock.NONE
import net.borysw.blitz.game.GameStatus.Status.FINISHED_PLAYER_A
import net.borysw.blitz.game.GameStatus.Status.FINISHED_PLAYER_B
import net.borysw.blitz.game.GameStatus.Status.INITIAL
import net.borysw.blitz.game.GameStatus.Status.IN_PROGRESS_PLAYER_A
import net.borysw.blitz.game.GameStatus.Status.IN_PROGRESS_PLAYER_B
import net.borysw.blitz.game.GameStatus.Status.PAUSED

class GameStatusFactory(private val timeFormatter: TimeFormatter) {

    fun getStatus(initialTime: Long, timeLeftA: Long, timeLeftB: Long, activeClock: ActiveClock): GameStatus {
        val status = when {
            (initialTime == timeLeftA && initialTime == timeLeftB) -> INITIAL
            activeClock == NONE -> PAUSED
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