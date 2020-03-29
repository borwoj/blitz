package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.borysw.blitz.SafeDisposable
import net.borysw.blitz.game.Game
import net.borysw.blitz.game.GameController
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.status.GameStatus
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(private val gameController: GameController) : ViewModel() {

    val gameStatus by lazy { MutableLiveData<GameStatus>() }
    val showDialog by lazy { MutableLiveData<ShowDialog>() }

    private val timeDisposable by lazy { SafeDisposable() }

    init {
        gameController.game = Game(10000)
        gameController.gameStatus.subscribe(gameStatus::postValue, ::e).run(timeDisposable::set)
    }

    fun onTimerAClicked() {
        gameController.userActions.onNext(UserAction.ClockClickedPlayer1)
    }

    fun onTimerBClicked() {
        gameController.userActions.onNext(UserAction.ClockClickedPlayer2)
    }

    fun onActionButtonClicked() {
        gameController.userActions.onNext(UserAction.ActionButtonClicked)
    }

    fun onResetConfirmClicked() {
        gameController.userActions.onNext(UserAction.ActionButtonClicked)
    }

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}