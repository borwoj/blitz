package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.computation
import net.borysw.blitz.game.Clock
import net.borysw.blitz.game.ClockStatus
import net.borysw.blitz.game.presentation.PlayerMove.PLAYER_A
import net.borysw.blitz.game.presentation.PlayerMove.PLAYER_B
import timber.log.Timber
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class GameViewModel @Inject constructor(private val timeFormatter: TimeFormatter) : ViewModel() {
  val gameStatus = MutableLiveData<GameStatus>()

  private val disposables = CompositeDisposable()
  private var gameStatusDisposable: Disposable? = null

  private val initialTime: Long = SECONDS.toMillis(5)

  private val clock = Clock(initialTime)

  private var lastClockStatus: ClockStatus? = null

  init {
    gameStatusDisposable =
        clock.gameStatus.subscribeOn(computation()).observeOn(mainThread()).subscribe { clockStatus ->
          Timber.d("Clock status: $clockStatus")
          val playerMove = getPlayerMove(clockStatus)
          gameStatus.value = GameStatus(
            clockStatus.timeA == 0L || clockStatus.timeB == 0L,
            timeFormatter.format(clockStatus.timeA),
            timeFormatter.format(clockStatus.timeB),
            playerMove
          )
        }
  }

  private fun getPlayerMove(clockStatus: ClockStatus): PlayerMove {
    val playerMove = when {
      lastClockStatus == null -> PLAYER_A
      lastClockStatus!!.timeA == clockStatus.timeA -> PLAYER_B
      else -> PLAYER_A
    }
    lastClockStatus = clockStatus
    return playerMove
  }

  fun onStartClicked() {
    if (!clock.isRunning()) {
      clock.startA()
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