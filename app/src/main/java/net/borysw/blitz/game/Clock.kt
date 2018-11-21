package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import net.borysw.blitz.game.ActiveTimer.*

class Clock(time: Long, private val initialTimeA: Long = 0, private val initialTimeB: Long = 0) {
  private val timerA = Timer(time)
  private val timerB = Timer(time)

  val gameStatus: Observable<ClockStatus> =
    Observable.combineLatest(timerA.timeLeftOsb, timerB.timeLeftOsb, BiFunction { timeLeftA, timeLeftB ->
      ClockStatus(timeLeftA, timeLeftB, if (timerA.isRunning()) A else B)
    })

  fun startA() {
    timerA.start()
  }

  fun startB() {
    timerB.start()
  }

  fun stop() {
    timerA.stop()
    timerB.stop()
  }

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