package net.borysw.blitz.game

import net.borysw.blitz.game.GameStatus.Status.FINISHED_PLAYER_A
import net.borysw.blitz.game.GameStatus.Status.FINISHED_PLAYER_B
import net.borysw.blitz.game.GameStatus.Status.INITIAL
import net.borysw.blitz.game.GameStatus.Status.IN_PROGRESS_PLAYER_A
import net.borysw.blitz.game.GameStatus.Status.IN_PROGRESS_PLAYER_B
import net.borysw.blitz.game.GameStatus.Status.PAUSED
import net.borysw.blitz.game.presentation.ChessClock

class GameStatusFactory(private val timeFormatter: TimeFormatter) {

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