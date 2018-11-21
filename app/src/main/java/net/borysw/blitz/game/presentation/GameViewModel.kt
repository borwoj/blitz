package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.computation
import net.borysw.blitz.game.Clock
import net.borysw.blitz.game.ClockStatus
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class GameViewModel @Inject constructor(private val gameStatusFactory: GameStatusFactory) : ViewModel() {
  val gameStatus = MutableLiveData<GameStatus>()

  private val disposables = CompositeDisposable()
  private var gameStatusDisposable: Disposable? = null

  private val initialTime: Long = SECONDS.toMillis(10)

  private val clock = Clock(initialTime)

  init {
    subscribeToClockStatus()
  }

  private fun subscribeToClockStatus() {
    gameStatusDisposable?.dispose()
    gameStatusDisposable = clock.gameStatus.map { clockStatus ->
      Timber.d("Clock status: $clockStatus")
      gameStatusFactory.getGameStatus(clockStatus, initialTime)
    }.distinctUntilChanged().subscribeOn(computation()).observeOn(mainThread()).subscribe { gameStatus ->
      this.gameStatus.value = gameStatus
    }
  }

  fun onStartClicked() {
    if (!clock.isRunning()) {
      clock.startA()
    } else {
      clock.stop()
      clock.reset()
      subscribeToClockStatus()
    }
  }

  override fun onCleared() {
    super.onCleared()
    disposables.dispose()
  }

  fun timerAClicked() {
    if (clock.isRunning()) {
      clock.switch()
    } else {
      clock.startA()
    }
  }

  fun timberBClicked() {
    if (clock.isRunning()) {
      clock.switch()
    } else {
      clock.startB()
    }
  }
}