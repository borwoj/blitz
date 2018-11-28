package net.borysw.blitz.game

class Timer(private var initialTime: Long) {
  var timeLeft = initialTime
    private set

  fun decrementTimeLeft() {
    timeLeft--
  }

  fun addTime(time: Long) {
    timeLeft += time
  }

  fun reset() {
    timeLeft = initialTime
  }

  fun isTimeOver() = timeLeft == 0L
}