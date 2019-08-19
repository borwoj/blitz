package net.borysw.blitz.game

class Timer(val initialTime: Long) {
    var remainingTime = initialTime
        private set

    var elapsedTime = 0L
        private set

    var progress = 0f
        private set
        get() = elapsedTime.toFloat() / (remainingTime + elapsedTime)

    fun advance() {
        if (isTimeOver()) throw IllegalStateException("Time was over already.")
        remainingTime--
        elapsedTime++
    }

    fun advanceBy(time: Long) {
        if (time > remainingTime) throw IllegalStateException("Time exceeds time left.")
        remainingTime -= time
        elapsedTime += time
    }

    fun addTime(time: Long) {
        remainingTime += time
    }

    fun reset() {
        remainingTime = initialTime
        elapsedTime = 0L
    }

    fun isTimeOver() = remainingTime == 0L
}