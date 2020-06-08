package net.borysw.blitz.game.engine.clock.type

import net.borysw.blitz.game.engine.clock.ChessClock
import net.borysw.blitz.game.engine.clock.ChessClock.Player
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import javax.inject.Inject

class FischerDecorator @Inject constructor(private val chessClock: ChessClockImpl) : ChessClock {

    var incrementBy: Long = 0

    override var initialTime: Long
        get() = chessClock.initialTime
        set(value) {
            chessClock.initialTime = value + incrementBy
        }

    override var currentPlayer: Player?
        get() = chessClock.currentPlayer
        set(value) {
            chessClock.currentPlayer = value
        }

    override val remainingTimePlayer1: Long
        get() = chessClock.remainingTimePlayer1

    override val remainingTimePlayer2: Long
        get() = chessClock.remainingTimePlayer2

    override val remainingDelayTimePlayer1: Long = 0

    override val remainingDelayTimePlayer2: Long = 0

    override val isTimeOver: Boolean
        get() = chessClock.isTimeOver

    override val isPaused: Boolean
        get() = chessClock.isPaused

    override fun changeTurn(nextPlayer: Player) {
        val isFirstMove = isFirstMove()
        chessClock.changeTurn(nextPlayer)
        if (!isFirstMove) when (requireNotNull(currentPlayer)) {
            Player1 -> chessClock.addTimePlayer2(incrementBy)
            Player2 -> chessClock.addTimePlayer1(incrementBy)
        }
    }

    private fun isFirstMove() =
        currentPlayer == null && chessClock.remainingTimePlayer1 == chessClock.initialTime && chessClock.remainingTimePlayer2 == chessClock.initialTime

    override fun advanceTime() {
        chessClock.advanceTime()
    }

    override fun addTimePlayer1(time: Long) {
        chessClock.addTimePlayer1(time)
    }

    override fun addTimePlayer2(time: Long) {
        chessClock.addTimePlayer2(time)
    }

    override fun pause() {
        chessClock.pause()
    }

    override fun reset() {
        chessClock.reset()
    }
}