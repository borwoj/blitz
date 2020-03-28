package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable.interval
import io.reactivex.rxjava3.core.Scheduler
import net.borysw.blitz.app.SafeDisposable
import net.borysw.blitz.app.clock.ChessClock
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatusFactory
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
        chessClock.initialTime = SECONDS.toMillis(2)
    }

    private fun startTime() =
        interval(1, MILLISECONDS)
            .subscribeOn(scheduler)
            .takeUntil { chessClock.isTimeOver }
            .doOnNext { chessClock.advanceTime() }
            .map { getGameStatus() }
            .distinctUntilChanged()
            .doOnNext(gameStatus::postValue)
            .doOnDispose { gameStatus.postValue(getGameStatus()) }
            .subscribe({}, ::e)
            .run(timeDisposable::set)

    private fun getGameStatus(): GameStatus = gameStatusFactory.getStatus(
        chessClock.initialTime,
        chessClock.remainingTimePlayer1,
        chessClock.remainingTimePlayer2,
        chessClock.currentPlayer
    )

    private fun pauseTime() {
        chessClock.onPaused()
        timeDisposable.dispose()
    }

    fun onTimerAClicked() {
        if (chessClock.currentPlayer == null)
            startTime()
        chessClock.changeTurn(ChessClock.Player.PLAYER_2)
    }

    fun onTimerBClicked() {
        if (chessClock.currentPlayer == null)
            startTime()
        chessClock.changeTurn(ChessClock.Player.PLAYER_1)
    }

    fun onPauseClicked() = pauseTime()

    fun onResetConfirmClicked() = chessClock.reset()

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}