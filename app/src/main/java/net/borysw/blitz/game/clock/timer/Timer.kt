package net.borysw.blitz.game.clock.timer

interface Timer {
    var initialTime: Long
    var remainingTime: Long
    val isTimeOver: Boolean

    fun advanceTime()
    fun addTime(time: Long)
    fun reset()
}