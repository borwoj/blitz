package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.computation
import net.borysw.blitz.game.Clock
import net.borysw.blitz.game.GameStatus
import net.borysw.blitz.game.GameStatusFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class GameViewModel @Inject constructor(private val gameStatusFactory: GameStatusFactory) : ViewModel() {
  val gameStatus = MutableLiveData<GameStatus>()

  private var clockStatusDisposable: Disposable? = null

  private val initialTime: Long = SECONDS.toMillis(5)

  private val clock = Clock(initialTime)

  init {
    subscribeToClockStatus()
  }

  private fun subscribeToClockStatus() {
    clockStatusDisposable?.dispose()
    clockStatusDisposable = clock.clockStatus.map { clockStatus ->
      Timber.d("Clock status: $clockStatus")
      gameStatusFactory.getGameStatus(clockStatus, initialTime)
    }.distinctUntilChanged().doOnNext {}.subscribeOn(computation()).observeOn(mainThread()).subscribe { gameStatus ->
      this.gameStatus.value = gameStatus
    }
  }

  fun onPauseClicked() {
    /* Timber.d("Timer A time left: ${clock.timerA.timeLeft}")
     Timber.d("Timer B time left: ${clock.timerB.timeLeft}")*/
    if (clock.isRunning()) {
      clock.stop()
    } else {
      clock.reset()
    }
    //subscribeToClockStatus()
  }

  fun onTimerAClicked() {
    if (!clock.hasFinished()) {
      clock.startB()
    }
  }

  fun onTimerBClicked() {
    if (!clock.hasFinished()) {
      clock.startA()
    }
  }

  override fun onCleared() {
    super.onCleared()
    clockStatusDisposable?.dispose()
  }
}