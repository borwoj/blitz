package net.borysw.blitz.game

class Timer(var initialTime: Long) {
  var timeLeft = initialTime
  fun reset() {
    timeLeft = initialTime
  }
}
