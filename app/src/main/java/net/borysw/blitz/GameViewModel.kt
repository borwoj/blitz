package net.borysw.blitz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameViewModel @Inject constructor(private val timeFormatter: TimeFormatter) : ViewModel() {
  val aTime = MutableLiveData<String>()
  val bTime = MutableLiveData<String>()

  private val disposables = CompositeDisposable()
  private var gameStatusDisposable: Disposable? = null

  private val initialTime: Long = TimeUnit.SECONDS.toMillis(60)

  private val clock = Clock(initialTime)

  init {
    gameStatusDisposable =
        clock.gameStatus.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe {
          aTime.value = timeFormatter.format(it.timeA)
          bTime.value = timeFormatter.format(it.timeB)
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