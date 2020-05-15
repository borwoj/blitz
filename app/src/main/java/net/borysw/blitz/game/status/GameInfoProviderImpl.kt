package net.borysw.blitz.game.status

import net.borysw.blitz.clock.ChessClock.Player
import net.borysw.blitz.game.status.GameInfo.Status.Finished.Player1Won
import net.borysw.blitz.game.status.GameInfo.Status.Finished.Player2Won
import net.borysw.blitz.game.status.GameInfo.Status.InProgress.Player1
import net.borysw.blitz.game.status.GameInfo.Status.InProgress.Player2
import net.borysw.blitz.game.status.GameInfo.Status.Paused
import net.borysw.blitz.game.status.GameInfo.Status.Unstarted
import javax.inject.Inject

class GameInfoProviderImpl @Inject constructor(private val timeFormatter: TimeFormatter) :
    GameInfoProvider {
    override fun get(
        initialTime: Long,
        remainingTimePlayer1: Long,
        remainingTimePlayer2: Long,
        currentPlayer: Player?
    ): GameInfo =
        GameInfo(
            timeFormatter.format(remainingTimePlayer1),
            timeFormatter.format(remainingTimePlayer2),
            getStatus(
                initialTime,
                remainingTimePlayer1,
                remainingTimePlayer2,
                currentPlayer
            )
        )

    private fun getStatus(
        initialTime: Long,
        remainingTimePlayer1: Long,
        remainingTimePlayer2: Long,
        current: Player?
    ): GameInfo.Status = when {
        (initialTime == remainingTimePlayer1 && initialTime == remainingTimePlayer2) -> Unstarted
        remainingTimePlayer1 == 0L -> Player1Won
        remainingTimePlayer2 == 0L -> Player2Won
        current == null -> Paused
        current == Player.Player1 -> Player1
        current == Player.Player2 -> Player2
        else -> throw IllegalStateException("Unknown game status.")
    }
}