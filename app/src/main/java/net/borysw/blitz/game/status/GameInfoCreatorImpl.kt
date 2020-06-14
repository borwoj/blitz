package net.borysw.blitz.game.status

import net.borysw.blitz.game.clock.type.ChessClock.Player
import net.borysw.blitz.game.engine.clock.ClockStatus
import net.borysw.blitz.game.status.GameInfo.Status.Finished.Player1Won
import net.borysw.blitz.game.status.GameInfo.Status.Finished.Player2Won
import net.borysw.blitz.game.status.GameInfo.Status.InProgress.Player1Turn
import net.borysw.blitz.game.status.GameInfo.Status.InProgress.Player2Turn
import net.borysw.blitz.game.status.GameInfo.Status.Paused
import net.borysw.blitz.game.status.GameInfo.Status.Unstarted
import javax.inject.Inject

class GameInfoCreatorImpl @Inject constructor(private val timeFormatter: TimeFormatter) :
    GameInfoCreator {
    override fun get(clockStatus: ClockStatus): GameInfo = with(clockStatus) {
        GameInfo(
            timeFormatter.format(remainingTimePlayer1),
            timeFormatter.format(remainingTimePlayer2),
            getStatus(
                initialTime,
                remainingTimePlayer1,
                remainingTimePlayer2,
                currentPlayer
            ),
            dialog
        )
    }

    private fun getStatus(
        initialTime: Long,
        remainingTimePlayer1: Long,
        remainingTimePlayer2: Long,
        currentPlayer: Player?
    ): GameInfo.Status = when {
        (currentPlayer == null && initialTime == remainingTimePlayer1 && initialTime == remainingTimePlayer2) -> Unstarted
        remainingTimePlayer1 == 0L -> Player1Won
        remainingTimePlayer2 == 0L -> Player2Won
        currentPlayer == null -> Paused
        currentPlayer == Player.Player1 -> Player1Turn
        currentPlayer == Player.Player2 -> Player2Turn
        else -> throw IllegalStateException("Unknown game status.")
    }
}