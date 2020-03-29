package net.borysw.blitz.clock.timer

interface Timer {
    var initialTime: Long
    var remainingTime: Long
    val isTimeOver: Boolean

    fun advanceTime()
    fun reset()
}