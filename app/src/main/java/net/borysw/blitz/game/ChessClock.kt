package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import net.borysw.blitz.game.ChessClock.ActiveClock.*
import java.util.concurrent.TimeUnit

class ChessClock(private val initialTime: Long, private val gameStatusFactory: GameStatusFactory) {
  private val clockA = Timer(initialTime)
  private val clockB = Timer(initialTime)

  val gameStatus = BehaviorSubject.create<GameStatus>()
  private var timerDisposable: Disposable? = null
  private var activeClock = NONE

  init {
    publishGameStatus()
  }

  private fun start() {
    subscribeToTimer()
  }

  fun reset() {
    clockA.reset()
    clockB.reset()
    activeClock = NONE
    publishGameStatus()
  }

  private fun subscribeToTimer() {
    timerDisposable?.dispose()
    timerDisposable = Observable.interval(1, TimeUnit.MILLISECONDS).doOnNext {
      when (activeClock) {
        CLOCK_A -> clockA.decrementTimeLeft()
        CLOCK_B -> clockB.decrementTimeLeft()
        NONE -> throw IllegalStateException("No clock is active")
      }
      publishGameStatus()
      if (isTimeOver()) {
        activeClock = NONE
      }
    }.takeUntil { isTimeOver() }.subscribe()
  }

  private fun publishGameStatus() {
    val gameStatus = gameStatusFactory.getStatus(initialTime, clockA.timeLeft, clockB.timeLeft, activeClock)
    this.gameStatus.onNext(gameStatus)
  }

  fun pause() {
    timerDisposable?.dispose()
    activeClock = NONE
    publishGameStatus()
  }

  fun onClockAPressed() {
    activeClock = CLOCK_B
    if (!isTimeOver()) {
      start()
    }
  }

  fun onClockBPressed() {
    activeClock = CLOCK_A
    if (!isTimeOver()) {
      start()
    }
  }

  fun dispose() {
    timerDisposable?.dispose()
  }

  fun isRunning(): Boolean {
    return timerDisposable != null && !timerDisposable!!.isDisposed
  }

  fun isTimeOver() = clockA.isTimeOver() || clockB.isTimeOver()

  enum class ActiveClock {
    CLOCK_A, CLOCK_B, NONE
  }
}