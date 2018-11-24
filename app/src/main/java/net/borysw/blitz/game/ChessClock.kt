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

  private fun isTimeOver() = clockA.timeLeft == 0L || clockB.timeLeft == 0L

  private fun start() {
    subscribeToTimer()
  }

  fun reset() {
    clockA.reset()
    clockB.reset()
    publishGameStatus()
  }

  private fun subscribeToTimer() {
    timerDisposable?.dispose()
    timerDisposable = Observable.interval(1, TimeUnit.MILLISECONDS).doOnNext {
      when (activeClock) {
        CLOCK_A -> clockA.timeLeft--
        CLOCK_B -> clockB.timeLeft--
        NONE -> throw IllegalStateException("No clock is active")
      }
      publishGameStatus()
    }.takeUntil { clockA.timeLeft == 0L || clockB.timeLeft == 0L }.subscribe()
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

  enum class ActiveClock {
    CLOCK_A, CLOCK_B, NONE
  }
}