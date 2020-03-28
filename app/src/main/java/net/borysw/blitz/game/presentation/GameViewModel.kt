package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable.interval
import io.reactivex.rxjava3.core.Scheduler
import net.borysw.blitz.app.SafeDisposable
import net.borysw.blitz.game.GameStatus
import net.borysw.blitz.game.GameStatusFactory
import timber.log.Timber.e
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val chessClock: ChessClock,
    private val gameStatusFactory: GameStatusFactory,
    private val scheduler: Scheduler
) : ViewModel() {
    val gameStatus by lazy { MutableLiveData<GameStatus>() }
    val showDialog by lazy { MutableLiveData<ShowDialog>() }

    private val timeDisposable by lazy { SafeDisposable() }

    init {
        chessClock.initialTime = SECONDS.toMillis(3000)
    }

    private fun startTime() =
        interval(1, MILLISECONDS)
            .subscribeOn(scheduler)
            .takeUntil { !chessClock.isTimeOver }
            .doOnNext { chessClock.advanceTime() }
            .map {
                gameStatusFactory.getStatus(
                    chessClock.initialTime,
                    chessClock.remainingTimePlayer1,
                    chessClock.remainingTimePlayer2,
                    chessClock.currentPlayer
                )
            }
            .subscribe({}, ::e)
            .run(timeDisposable::set)

    private fun pauseTime() = timeDisposable.dispose()

    fun onTimerAClicked() {
        if (!chessClock.isTimeOver)
            startTime()
        chessClock.changeTurn(ChessClock.Player.SECOND)
    }

    fun onTimerBClicked() {
        if (!chessClock.isTimeOver)
            startTime()
        chessClock.changeTurn(ChessClock.Player.FIRST)
    }

    fun onPauseClicked() = pauseTime()

    fun onResetConfirmClicked() = chessClock.reset()

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}