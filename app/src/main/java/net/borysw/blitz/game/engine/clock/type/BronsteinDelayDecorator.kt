package net.borysw.blitz.game.engine.clock.type

import net.borysw.blitz.game.engine.clock.ChessClock
import net.borysw.blitz.game.engine.clock.ChessClock.Player
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.clock.timer.Timer
import javax.inject.Inject

class BronsteinDelayDecorator @Inject constructor(
    private val chessClock: ChessClockImpl,
    private val delayTimer1: Timer,
    private val delayTimer2: Timer
) : ChessClock {
    var delay: Long = 0
        set(value) {
            delayTimer1.initialTime = value
            delayTimer2.initialTime = value
            field = value
        }

    override var initialTime: Long
        get() = chessClock.initialTime
        set(value) {
            chessClock.initialTime = value
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

    override val remainingDelayTimePlayer1: Long
        get() = delayTimer1.remainingTime

    override val remainingDelayTimePlayer2: Long
        get() = delayTimer2.remainingTime

    override val isTimeOver: Boolean
        get() = chessClock.isTimeOver

    override val isPaused: Boolean
        get() = chessClock.isPaused

    override fun changeTurn(nextPlayer: Player) {
        chessClock.changeTurn(nextPlayer)
        when (requireNotNull(currentPlayer)) {
            Player1 -> delayTimer1.reset()
            Player2 -> delayTimer2.reset()
        }
    }

    override fun advanceTime() {
        when (currentPlayer) {
            Player1 -> {
                if (!delayTimer1.isTimeOver) delayTimer1.advanceTime()
                else chessClock.advanceTime()
            }
            Player2 -> {
                if (!delayTimer2.isTimeOver) delayTimer2.advanceTime()
                else chessClock.advanceTime()
            }
        }
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