package net.borysw.blitz

import io.reactivex.Observable
import java.util.concurrent.TimeUnit.MILLISECONDS

class Timer(initialTime: Long) {
  private var timeLeft = initialTime

  private var isPaused = true

  fun isRunning(): Boolean {
    return !isPaused
  }

  fun reset() {
  }

  fun start(): Observable<Long> {
    return Observable.interval(1, MILLISECONDS).map {
      --timeLeft
    }.takeUntil { timeLeft == 0L }.startWith(timeLeft)
  }

  fun stop() {
  }
}