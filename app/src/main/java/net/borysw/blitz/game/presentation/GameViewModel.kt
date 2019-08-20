package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import net.borysw.blitz.game.ChessClock2
import net.borysw.blitz.game.GameStatus
import net.borysw.blitz.game.GameStatusFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.SECONDS

class GameViewModel(
    private val chessClock: ChessClock2,
    private val gameStatusFactory: GameStatusFactory,
    private val scheduler: Scheduler
) : ViewModel() {
    val gameStatus = MutableLiveData<GameStatus>()
    val showDialog = MutableLiveData<ShowDialog>()

    private var clockDisposable: Disposable? = null

    init {
        chessClock.initialTime = SECONDS.toMillis(3)
    }

    private fun startClock() {
        if (clockDisposable == null || clockDisposable!!.isDisposed) clockDisposable =
            Observable.interval(1, TimeUnit.MILLISECONDS, scheduler)
                .doOnNext { chessClock.advanceTime() }
                .map {
                    gameStatusFactory.getStatus(
                        chessClock.initialTime,
                        chessClock.remainingTimeA,
                        chessClock.remainingTimeB,
                        chessClock.getCurrent()
                    )
                }
                .distinctUntilChanged()
                .doOnNext { gameStatus.postValue(it) }
                .takeUntil { chessClock.isTimeOver() }
                .subscribe()
    }

    fun onPauseClicked() = clockDisposable?.dispose()

    fun onResetConfirmClicked() = chessClock.reset()

    fun onTimerAClicked() {
        chessClock.onPressedA()
        startClock()
    }

    fun onTimerBClicked() {
        chessClock.onPressedB()
        startClock()
    }

    override fun onCleared() {
        clockDisposable?.dispose()
    }

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }
}