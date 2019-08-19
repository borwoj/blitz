package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.computation
import net.borysw.blitz.game.ChessClock
import net.borysw.blitz.game.GameStatus
import java.util.concurrent.TimeUnit.SECONDS

class GameViewModel(private val chessClock: ChessClock) : ViewModel() {
    val gameStatus = MutableLiveData<GameStatus>()
    val showDialog = MutableLiveData<ShowDialog>()

    private var clockStatusDisposable: Disposable? = null

    init {
        chessClock.initialTime = SECONDS.toMillis(20)
        subscribeToClockStatus()
    }

    private fun subscribeToClockStatus() {
        clockStatusDisposable?.dispose()
        clockStatusDisposable =
            chessClock.gameStatus.distinctUntilChanged().subscribeOn(computation()).observeOn(mainThread())
                .subscribe { gameStatus ->
                    this.gameStatus.value = gameStatus
                }
    }

    fun onPauseClicked() = if (chessClock.isRunning()) {
        chessClock.pause()
    } else if (!chessClock.isTimeOver()) {
        showDialog.value = ShowDialog()
    } else {
        chessClock.reset()
    }

    fun onResetConfirmClicked() = chessClock.reset()

    fun onTimerAClicked() = chessClock.onClockAPressed()

    fun onTimerBClicked() = chessClock.onClockBPressed()

    override fun onCleared() {
        clockStatusDisposable?.dispose()
        chessClock.dispose()
    }

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }
}