package net.borysw.blitz.game.clock.type

import net.borysw.blitz.game.clock.type.ChessClock.Player
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player1
import net.borysw.blitz.game.clock.type.ChessClock.Player.Player2
import javax.inject.Inject

class FischerDecorator @Inject constructor(private val chessClock: ChessClock) :
    ChessClock {

    var incrementBy: Long = -1

    override var initialTime: Long
        get() = chessClock.initialTime
        set(value) {
            if (incrementBy == -1L) throw IllegalStateException("Increment value needs to be set first")
            chessClock.initialTime = value + incrementBy
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
                Player1 -> chessClock.addTimePlayer2(incrementBy)
                Player2 -> chessClock.addTimePlayer1(incrementBy)
            }
        }
    }

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