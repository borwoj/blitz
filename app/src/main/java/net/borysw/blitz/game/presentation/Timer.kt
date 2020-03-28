package net.borysw.blitz.game.presentation

interface Timer {
    var initialTime: Long
    var remainingTime: Long
    var isTimeOver: Boolean
    fun advanceTime()
    fun reset()
}