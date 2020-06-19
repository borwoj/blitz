package net.borysw.blitz.game.clock.type

import net.borysw.blitz.game.clock.timer.Timer
import net.borysw.blitz.game.clock.type.ChessClock.Player
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import javax.inject.Inject

class BronsteinDelayDecorator @Inject constructor(
    private val chessClock: ChessClock,
    private val delayTimer1: Timer,
    private val delayTimer2: Timer
) : ChessClock {

    var delay: Long = -1
        set(value) {
            delayTimer1.initialTime = value
            delayTimer2.initialTime = value
            field = value
        }

    override var initialTime: Long
        get() = chessClock.initialTime
        set(value) {
            if (delay == -1L) throw IllegalStateException("Delay value needs to be set first")
            chessClock.initialTime = value + delay
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

    override fun changeTurn(nextPlayer: Player) {
        if (currentPlayer != nextPlayer) {
            val isFirstMove =
                currentPlayer == null
                    && chessClock.remainingTimePlayer1 == chessClock.initialTime
                    && chessClock.remainingTimePlayer2 == chessClock.initialTime

            chessClock.changeTurn(nextPlayer)

            if (!isFirstMove) when (requireNotNull(currentPlayer)) {
                Player1 -> {
                    chessClock.addTimePlayer2(delay - delayTimer2.remainingTime)
                    delayTimer2.reset()
                }
                Player2 -> {
                    chessClock.addTimePlayer1(delay - delayTimer1.remainingTime)
                    delayTimer1.reset()
                }
            }
        }
    }

    override fun advanceTime() {
        when (currentPlayer) {
            Player1 -> {
                if (!delayTimer1.isTimeOver) delayTimer1.advanceTime()
                chessClock.advanceTime()
            }
            Player2 -> {
                if (!delayTimer2.isTimeOver) delayTimer2.advanceTime()
                chessClock.advanceTime()
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