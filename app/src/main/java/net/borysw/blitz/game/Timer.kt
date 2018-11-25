package net.borysw.blitz.game

class Timer(private var initialTime: Long) {
  private var timeLeft = initialTime

  fun decrementTimeLeft() {
    timeLeft--
  }

  fun getTimeLeft() = timeLeft

  fun addTime(time: Long) {
    timeLeft += time
  }

  fun reset() {
    timeLeft = initialTime
  }

  fun isTimeOver() = timeLeft == 0L
}
