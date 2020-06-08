package net.borysw.blitz.game.engine.clock.type

import net.borysw.blitz.game.engine.clock.ChessClock
import net.borysw.blitz.game.engine.clock.ChessClock.Player
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player1
import net.borysw.blitz.game.engine.clock.ChessClock.Player.Player2
import net.borysw.blitz.game.engine.clock.timer.Timer
import javax.inject.Inject

class BronsteinDelayDecorator @Inject constructor(
    private val chessClock: ChessClock,
    private val delayTimer1: Timer,
    private val delayTimer2: Timer
) : ChessClock {

    var delayAndIncrement: Long = -1
        set(value) {
            delayTimer1.initialTime = value
            delayTimer2.initialTime = value
            field = value
        }

    override var initialTime: Long
        get() = chessClock.initialTime
        set(value) {
            if (delayAndIncrement == -1L) throw IllegalStateException("Delay/increment value needs to be set first for compliance with FIDE and US Chess rules")
            chessClock.initialTime = value + delayAndIncrement
        }

    override val currentPlayer: Player?
        get() = chessClock.currentPlayer

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
        if (currentPlayer != nextPlayer) {
            val isFirstMove =
                currentPlayer == null
                    && chessClock.remainingTimePlayer1 == chessClock.initialTime
                    && chessClock.remainingTimePlayer2 == chessClock.initialTime

            chessClock.changeTurn(nextPlayer)

            if (!isFirstMove) when (requireNotNull(currentPlayer)) {
                Player1 -> {
                    chessClock.addTimePlayer2(delayAndIncrement - delayTimer2.remainingTime)
                    delayTimer2.reset()
                }
                Player2 -> {
                    chessClock.addTimePlayer1(delayAndIncrement - delayTimer1.remainingTime)
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