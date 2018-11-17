package net.borysw.blitz

import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit.MILLISECONDS

class Timer(private val initialTime: Long) {
  private var timeLeft = initialTime

  fun isRunning(): Boolean {
    return false
  }

  fun reset() {
  }

  fun start(): Observable<Long> = Observable.interval(1, MILLISECONDS).map { timePassed ->
    timeLeft = initialTime - timePassed
    timeLeft
  }.takeUntil { timeLeft == 0L }.startWith(timeLeft)

  fun stop() {
  }
}