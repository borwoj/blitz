package net.borysw.blitz.game.presentation

import javax.inject.Inject

class TimerImpl @Inject constructor() : Timer {
    override var initialTime: Long = 0
        set(value) {
            remainingTime = value
            field = value
        }
    override var remainingTime: Long = 0
    override val isTimeOver: Boolean
        get() = remainingTime == 0L

    override fun advanceTime() {
        remainingTime--
    }

    override fun reset() {
        remainingTime = initialTime
    }
}