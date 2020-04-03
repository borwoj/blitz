package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.borysw.blitz.SafeDisposable
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.engine.game.GameEngine
import net.borysw.blitz.game.status.GameStatus
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(private val gameEngine: GameEngine) : ViewModel() {

    val gameStatus by lazy { MutableLiveData<GameStatus>() }
    val showDialog by lazy { MutableLiveData<ShowDialog>() }

    private val timeDisposable by lazy { SafeDisposable() }

    init {
        gameEngine.gameStatus.subscribe(gameStatus::postValue, ::e).run(timeDisposable::set)
        gameEngine.userActions.onNext(UserAction.SitAtTable)
    }

    fun onPlayer1Clicked() {
        gameEngine.userActions.onNext(UserAction.ClockClickedPlayer1)
    }

    fun onPlayer2Clicked() {
        gameEngine.userActions.onNext(UserAction.ClockClickedPlayer2)
    }

    fun onActionButtonClicked() {
        gameEngine.userActions.onNext(UserAction.ActionButtonClicked)
    }

    fun onResetConfirmClicked() {
        gameEngine.userActions.onNext(UserAction.ActionButtonClicked)
    }

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}