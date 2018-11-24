package net.borysw.blitz.game

import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS

class Timer(private val initialTime: Long) {
  private var timeLeft = initialTime
  private var isPaused = true

  fun isRunning(): Boolean = !isPaused

  fun isFinished(): Boolean = timeLeft == 0L

  fun start() {
    isPaused = false
  }

  fun stop() {
    isPaused = true
  }

  fun reset() {
    timeLeft = initialTime
  }

  val timeLeftOsb: Observable<Long> = Observable.interval(1, MILLISECONDS).filter { !isPaused }.map {
    --timeLeft
  }.takeUntil { isFinished() }.startWith(timeLeft)
}