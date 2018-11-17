package net.borysw.blitz

import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class Clock(time: Long, private val initialTimeA: Long = 0, private val initialTimeB: Long = 0) {
  private val timerA = Timer(time)
  private val timerB = Timer(time)

  fun start(): Observable<ClockStatus> =
    Observable.combineLatest(timerA.start(), timerB.start(), BiFunction { timeLeftA, timeLeftB ->
      ClockStatus(timeLeftA, timeLeftB)
    })

  fun reset() {
    timerA.reset()
    timerB.reset()
  }

  fun switch() {
    if (timerA.isRunning()) {
      timerA.stop()
      timerB.start()
    } else {
      timerB.stop()
      timerA.start()
    }
  }

  fun isRunning() = timerA.isRunning() || timerB.isRunning()
}