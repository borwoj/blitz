package net.borysw.blitz.game.engine.clock

import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.clock.timer.Timer
import net.borysw.blitz.settings.GameType
import net.borysw.blitz.settings.GameType.Increment
import net.borysw.blitz.settings.GameType.Standard
import javax.inject.Inject

class ChessClockImpl @Inject constructor(private val timer1: Timer, private val timer2: Timer) :
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

    override var currentPlayer: ChessClock.Player? = null

    override val isTimeOver: Boolean
        get() = timer1.isTimeOver || timer2.isTimeOver

    override val isPaused: Boolean
        get() = currentPlayer == null

    override fun advanceTime() {
        when (currentPlayer) {
            Player1 -> timer1.advanceTime()
            Player2 -> timer2.advanceTime()
            null -> throw IllegalStateException("Can't advance time when player turn is undefined.")
        }
        if (isTimeOver) pause()
    }

    override var gameType: GameType = Standard

    override fun pause() {
        currentPlayer = null
    }

    override fun changeTurn(nextPlayer: ChessClock.Player) {
        if (currentPlayer != nextPlayer) {
            currentPlayer = nextPlayer
            when (val type = gameType) {
                is Increment -> when (currentPlayer) {
                    Player1 -> timer2.addTime(type.incrementBy)
                    Player2 -> timer1.addTime(type.incrementBy)
                }
            }
        }
    }

    override fun reset() {
        pause()
        timer1.reset()
        timer2.reset()
    }
}