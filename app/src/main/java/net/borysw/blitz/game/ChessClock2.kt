package net.borysw.blitz.game

class ChessClock2(private val timerA: Timer, private val timerB: Timer) {
    private var currentTimer: Timer? = null

    var initialTime: Long = 0L
        set(value) {
            field = value
            timerA.initialTime = field
            timerB.initialTime = field
        }

    val remainingTimeA: Long
        get() = timerA.remainingTime

    val remainingTimeB: Long
        get() = timerB.remainingTime

    fun advanceTime() {
        currentTimer!!.advance()
        if (isTimeOver()) {
            currentTimer = null
        }
    }

    fun onPressedA() {
        currentTimer = timerB
    }

    fun onPressedB() {
        currentTimer = timerA
    }

    fun reset() {
        timerA.reset()
        timerB.reset()
        currentTimer = null
    }

    // as property here and in timer?
    fun isTimeOver() = timerA.isTimeOver() || timerB.isTimeOver()

    fun getCurrent(): Current =
        if (currentTimer == timerA) Current.A else if (currentTimer == timerB) Current.B else Current.NONE

    enum class Current {
        A, B, NONE
    }
}