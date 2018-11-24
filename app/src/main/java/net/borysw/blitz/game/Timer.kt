package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit.MILLISECONDS

class Timer(private val initialTime: Long) {
  private var timeLeft = initialTime
  private var isPaused = true
  val timeLeftSub: Subject<Long> = BehaviorSubject.create()

  init {
    Observable.interval(1, MILLISECONDS).filter { isRunning() }.map {
      --timeLeft
    }.doOnNext {
      if (timeLeft == 0L) {
        stop()
      }
    }.startWith(timeLeft).subscribe(timeLeftSub)
  }

  private fun publishTimeLeft() {
    timeLeftSub.onNext(timeLeft)
  }

  fun isRunning(): Boolean = !isPaused && !hasFinished()

  fun hasFinished(): Boolean = timeLeft == 0L

  fun start() {
    isPaused = false
  }

  fun stop() {
    isPaused = true
    publishTimeLeft()
  }

  fun reset() {
    timeLeft = initialTime
    publishTimeLeft()
  }
}