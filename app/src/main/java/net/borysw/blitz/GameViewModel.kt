package net.borysw.blitz

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameViewModel @Inject constructor(private val timeFormatter: TimeFormatter) : ViewModel() {
  val aTime = MutableLiveData<String>()
  val bTime = MutableLiveData<String>()

  private val disposables = CompositeDisposable()

  private val initialTime: Long = TimeUnit.SECONDS.toMillis(60)

  private val clock = Clock(initialTime)

  fun onStartClicked() {
    clock.start().subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe {
      aTime.value = TimeUnit.MILLISECONDS.toSeconds(it.timeA).toString()
      bTime.value = TimeUnit.MILLISECONDS.toSeconds(it.timeB).toString()
    }.apply { disposables.add(this) }
  }

  fun onSwitchClicked() {
    clock.switch()
  }

  override fun onCleared() {
    super.onCleared()
    disposables.dispose()
  }
}