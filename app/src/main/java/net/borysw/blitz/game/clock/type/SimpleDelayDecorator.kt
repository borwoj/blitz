package net.borysw.blitz.game.clock.type

import net.borysw.blitz.game.clock.ClockStatus
import net.borysw.blitz.game.clock.timer.Timer
import net.borysw.blitz.game.clock.type.ChessClock.Player
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import javax.inject.Inject

class SimpleDelayDecorator @Inject constructor(
    private val chessClock: ChessClock,
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

    override val currentPlayer: Player?
        get() = chessClock.currentPlayer

    override val remainingTimePlayer1: Long
        get() = chessClock.remainingTimePlayer1

    override val remainingTimePlayer2: Long
        get() = chessClock.remainingTimePlayer2

    override val isTimeOver: Boolean
        get() = chessClock.isTimeOver

    override val isPaused: Boolean
        get() = chessClock.isPaused

    override val status: ClockStatus
        get() = chessClock.status

    override fun changeTurn(nextPlayer: Player) {
        if (currentPlayer != nextPlayer) {
            chessClock.changeTurn(nextPlayer)
            when (requireNotNull(currentPlayer)) {
                Player1 -> delayTimer1.reset()
                Player2 -> delayTimer2.reset()
            }
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