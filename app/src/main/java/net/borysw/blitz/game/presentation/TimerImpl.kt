package net.borysw.blitz.game.presentation

import javax.inject.Inject

class TimerImpl @Inject constructor() : Timer {
    override var initialTime: Long = 0
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