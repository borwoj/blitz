package net.borysw.blitz.game.status

import net.borysw.blitz.clock.ChessClock
import net.borysw.blitz.clock.ChessClock.Player
import net.borysw.blitz.game.status.GameStatus.Status.Finished.Player1Won
import net.borysw.blitz.game.status.GameStatus.Status.Finished.Player2Won
import net.borysw.blitz.game.status.GameStatus.Status.InProgress.Player1
import net.borysw.blitz.game.status.GameStatus.Status.InProgress.Player2
import net.borysw.blitz.game.status.GameStatus.Status.Paused
import net.borysw.blitz.game.status.GameStatus.Status.Unstarted
import javax.inject.Inject

class GameStatusFactoryImpl @Inject constructor(private val timeFormatter: TimeFormatter) :
    GameStatusFactory {
    override fun getStatus(chessClock: ChessClock): GameStatus = with(chessClock) {
        GameStatus(
            timeFormatter.format(initialTime),
            timeFormatter.format(remainingTimePlayer1),
            timeFormatter.format(remainingTimePlayer2),
            getStatus2(
                initialTime,
                remainingTimePlayer1,
                remainingTimePlayer2,
                currentPlayer
            )
        )
    }

    private fun getStatus2(
        initialTime: Long,
        remainingTimePlayer1: Long,
        remainingTimePlayer2: Long,
        current: Player?
    ): GameStatus.Status = when {
        (initialTime == remainingTimePlayer1 && initialTime == remainingTimePlayer2) -> Unstarted
        remainingTimePlayer1 == 0L -> Player1Won
        remainingTimePlayer2 == 0L -> Player2Won
        current == null -> Paused
        current == Player.Player1 -> Player1
        current == Player.Player2 -> Player2
        else -> throw IllegalStateException("Unknown game status.")
    }
}