package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import net.borysw.blitz.game.ChessClock.ActiveClock.CLOCK_A
import net.borysw.blitz.game.ChessClock.ActiveClock.CLOCK_B
import net.borysw.blitz.game.ChessClock.ActiveClock.NONE
import java.util.concurrent.TimeUnit

class ChessClock(
    private val gameStatusFactory: GameStatusFactory,
    private val timerA: Timer,
    private val timerB: Timer
) {
    var initialTime: Long = 0L
        set(value) {
            field = value
            timerA.initialTime = field
            timerB.initialTime = field
        }

    val gameStatus = BehaviorSubject.create<GameStatus>()

    private var timerDisposable: Disposable? = null
    private var activeClock = NONE

    init {
        publishGameStatus()
    }

    private fun start() = subscribeToTimer()

    fun reset() {
        timerA.reset()
        timerB.reset()
        activeClock = NONE
        publishGameStatus()
    }

    private fun subscribeToTimer() {
        timerDisposable?.dispose()
        timerDisposable = Observable.interval(1, TimeUnit.MILLISECONDS).doOnNext {
            when (activeClock) {
                CLOCK_A -> timerA.advance()
                CLOCK_B -> timerB.advance()
                NONE -> throw IllegalStateException("No clock is active")
            }
            publishGameStatus()
            if (isTimeOver()) {
                activeClock = NONE
            }
        }.takeUntil { isTimeOver() }.subscribe()
    }

    private fun publishGameStatus() {
        val gameStatus =
            gameStatusFactory.getStatus(initialTime, timerA.remainingTime, timerB.remainingTime, activeClock)
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

    fun isRunning(): Boolean = timerDisposable != null && !timerDisposable!!.isDisposed

    fun isTimeOver() = timerA.isTimeOver() || timerB.isTimeOver()

    enum class ActiveClock {
        CLOCK_A, CLOCK_B, NONE
    }
}