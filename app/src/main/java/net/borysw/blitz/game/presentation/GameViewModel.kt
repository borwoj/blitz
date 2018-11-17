package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.borysw.blitz.game.Clock
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class GameViewModel @Inject constructor(private val timeFormatter: TimeFormatter) : ViewModel() {
  val gameStatus = MutableLiveData<GameStatus>()

  private val disposables = CompositeDisposable()
  private var gameStatusDisposable: Disposable? = null

  private val initialTime: Long = SECONDS.toMillis(2)

  private val clock = Clock(initialTime)

  init {
    gameStatusDisposable = clock.gameStatus.subscribeOn(Schedulers.computation()).observeOn(
      AndroidSchedulers.mainThread()
    ).subscribe {
      gameStatus.value = GameStatus(
        it.timeA == 0L || it.timeB == 0L, timeFormatter.format(it.timeA), timeFormatter.format(it.timeB)
      )
    }
  }

  fun onStartClicked() {
    if (!clock.isRunning()) {
      clock.start()
    }
  }

  fun onSwitchClicked() {
    clock.switch()
  }

  override fun onCleared() {
    super.onCleared()
    disposables.dispose()
  }
}