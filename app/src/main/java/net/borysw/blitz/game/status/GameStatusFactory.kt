package net.borysw.blitz.game.status

import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.game.status.GameStatus.Status.FINISHED_PLAYER_A
import net.borysw.blitz.game.status.GameStatus.Status.FINISHED_PLAYER_B
import net.borysw.blitz.game.status.GameStatus.Status.INITIAL
import net.borysw.blitz.game.status.GameStatus.Status.IN_PROGRESS_PLAYER_A
import net.borysw.blitz.game.status.GameStatus.Status.IN_PROGRESS_PLAYER_B
import net.borysw.blitz.game.status.GameStatus.Status.PAUSED
import javax.inject.Inject

class GameStatusFactory @Inject constructor(private val timeFormatter: TimeFormatter) {

    fun getStatus(
        initialTime: Long,
        timeLeftA: Long,
        timeLeftB: Long,
        current: ChessClock.Player?
    ): GameStatus {
        val status = when {
            (initialTime == timeLeftA && initialTime == timeLeftB) -> INITIAL
            current == null -> PAUSED
            timeLeftA == 0L -> FINISHED_PLAYER_A
            timeLeftB == 0L -> FINISHED_PLAYER_B
            current == ChessClock.Player.FIRST -> IN_PROGRESS_PLAYER_A
            current == ChessClock.Player.SECOND -> IN_PROGRESS_PLAYER_B
            else -> throw IllegalStateException("Failed to create status")
        }
        val timeA = timeFormatter.format(timeLeftA)
        val timeB = timeFormatter.format(timeLeftB)

        return GameStatus(status, timeA, timeB)
    }
}