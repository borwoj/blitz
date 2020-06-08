package net.borysw.blitz.game.engine.clock.type

import net.borysw.blitz.game.engine.clock.ChessClock
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.clock.timer.Timer
import javax.inject.Inject

class BronsteinChessClockImpl @Inject constructor(
    private val timer1: Timer,
    private val timer2: Timer,
    private val delayTimer1: Timer,
    private val delayTimer2: Timer
) :
    ChessClock {
    override var initialTime: Long = 0
        set(value) {
            timer1.initialTime = value
            timer2.initialTime = value
            field = value
        }

    override val remainingTimePlayer1: Long
        get() = timer1.remainingTime

    override val remainingTimePlayer2: Long
        get() = timer2.remainingTime

    override val remainingDelayTimePlayer1: Long
        get() = delayTimer1.remainingTime

    override val remainingDelayTimePlayer2: Long
        get() = delayTimer2.remainingTime

    override var currentPlayer: ChessClock.Player? = null

    override val isTimeOver: Boolean
        get() = timer1.isTimeOver || timer2.isTimeOver

    override val isPaused: Boolean
        get() = currentPlayer == null

    var delay: Long = 0
        set(value) {
            delayTimer1.initialTime = delay
            delayTimer2.initialTime = delay
            field = value
        }

    var incrementBy: Long = 0

    override fun advanceTime() {
        when (currentPlayer) {
            Player1 -> timer1.advanceTime()
            Player2 -> timer2.advanceTime()
            null -> throw IllegalStateException("Can't advance time when player turn is undefined.")
        }
        if (isTimeOver) pause()
    }

    override fun pause() {
        currentPlayer = null
    }

    override fun changeTurn(nextPlayer: ChessClock.Player) {
        if (currentPlayer != nextPlayer)
            currentPlayer = nextPlayer
    }

    override fun reset() {
        pause()
        timer1.reset()
        timer2.reset()
    }
}