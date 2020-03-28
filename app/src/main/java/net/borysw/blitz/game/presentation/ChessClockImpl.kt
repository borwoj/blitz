package net.borysw.blitz.game.presentation

import net.borysw.blitz.game.presentation.ChessClock.Player.FIRST
import net.borysw.blitz.game.presentation.ChessClock.Player.SECOND
import javax.inject.Inject

class ChessClockImpl @Inject constructor(private val timer1: Timer, private val timer2: Timer) :
    ChessClock {
    override var initialTime: Long = 0
    override val remainingTimePlayer1: Long
        get() = timer1.remainingTime
    override val remainingTimePlayer2: Long
        get() = timer2.remainingTime
    override var currentPlayer: ChessClock.Player? = null
    override val isTimeOver: Boolean
        get() = timer1.isTimeOver || timer2.isTimeOver

    override fun advanceTime() {
        when (currentPlayer) {
            FIRST -> timer1.advanceTime()
            SECOND -> timer2.advanceTime()
            null -> throw IllegalStateException("Can't advance time after the clock was paused.")
        }
    }

    override fun onPaused() {
        currentPlayer = null
    }

    override fun changeTurn(nextPlayer: ChessClock.Player) {
        currentPlayer = nextPlayer
    }

    override fun reset() {
        currentPlayer = null
        timer1.reset()
        timer2.reset()
    }
}