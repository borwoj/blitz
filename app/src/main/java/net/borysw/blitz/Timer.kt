package net.borysw.blitz

import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS

class Timer(private val initialTime: Long) {
  private var timeLeft = initialTime

  private var isPaused = true

  fun isRunning(): Boolean {
    return !isPaused
  }

  fun reset() {
    timeLeft = initialTime
  }

  val timeLeftOsb = Observable.interval(1, MILLISECONDS).filter { !isPaused }.map {
    --timeLeft
  }.takeUntil { timeLeft == 0L }.startWith(timeLeft)

  fun start() {
    isPaused = false
  }

  fun stop() {
    isPaused = true
  }
}