package net.borysw.blitz.game.clock.type

import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.clock.timer.Timer
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import javax.inject.Inject

class BasicChessClockImpl @Inject constructor(
    private val timer1: Timer,
    private val timer2: Timer
) : ChessClock {
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

    override var currentPlayer: ChessClock.Player? = null
        private set

    override val isTimeOver: Boolean
        get() = timer1.isTimeOver || timer2.isTimeOver

    override val isPaused: Boolean
        get() = currentPlayer == null

    override val status: ClockStatus
        get() = ClockStatus(initialTime, remainingTimePlayer1, remainingTimePlayer2, currentPlayer)

    override fun advanceTime() {
        when (currentPlayer) {
            Player1 -> timer1.advanceTime()
            Player2 -> timer2.advanceTime()
            null -> throw IllegalStateException("Can't advance time when player turn is undefined.")
        }
        if (isTimeOver) pause()
    }

    override fun addTimePlayer1(time: Long) {
        timer1.addTime(time)
    }

    override fun addTimePlayer2(time: Long) {
        timer2.addTime(time)
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