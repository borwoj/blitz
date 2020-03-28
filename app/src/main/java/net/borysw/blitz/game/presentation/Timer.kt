package net.borysw.blitz.game.presentation

interface Timer {
    var initialTime: Long
    var remainingTime: Long
    val isTimeOver: Boolean
    fun advanceTime()
    fun reset()
}