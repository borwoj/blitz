package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.computation
import net.borysw.blitz.game.ChessClock
import net.borysw.blitz.game.GameStatus
import net.borysw.blitz.game.GameStatusFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class GameViewModel @Inject constructor(gameStatusFactory: GameStatusFactory) : ViewModel() {
  val gameStatus = MutableLiveData<GameStatus>()
  val showDialog = MutableLiveData<ShowDialog?>()

  private var clockStatusDisposable: Disposable? = null

  private val initialTime: Long = SECONDS.toMillis(5)

  private val chessClock = ChessClock(initialTime, gameStatusFactory)

  init {
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

  fun onPauseClicked() {
    if (chessClock.isRunning()) {
      chessClock.pause()
    } else {
      showDialog.value = ShowDialog()
      showDialog.value=null
    }
  }

  fun onResetConfirmClicked() {
    chessClock.reset()
  }

  fun onTimerAClicked() {
    chessClock.onClockAPressed()
  }

  fun onTimerBClicked() {
    chessClock.onClockBPressed()
  }

  override fun onCleared() {
    super.onCleared()
    clockStatusDisposable?.dispose()
    chessClock.dispose()
  }
}