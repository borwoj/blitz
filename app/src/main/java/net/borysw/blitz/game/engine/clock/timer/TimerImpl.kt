package net.borysw.blitz.game.engine.clock.timer

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
        if (isTimeOver) throw IllegalStateException("Time was over already.")
        remainingTime--
    }

    override fun addTime(time: Long) {
        remainingTime += time
    }

    override fun reset() {
        remainingTime = initialTime
    }
}