package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.borysw.blitz.SafeDisposable
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.engine.game.GameEngine
import net.borysw.blitz.game.status.GameInfo
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(private val gameEngine: GameEngine) : ViewModel() {

    val gameInfo by lazy { MutableLiveData<GameInfo>() }
    val dialog by lazy { MutableLiveData<Dialog>() }

    private val timeDisposable by lazy { SafeDisposable() }

    init {
        gameEngine.gameInfo.subscribe(gameInfo::postValue, ::e).run(timeDisposable::set)
        gameEngine.userActions.onNext(UserAction.SitAtTable)
    }

    fun onPlayer1Clicked() = gameEngine.userActions.onNext(UserAction.ClockClickedPlayer1)

    fun onPlayer2Clicked() = gameEngine.userActions.onNext(UserAction.ClockClickedPlayer2)

    fun onActionButtonClicked() = gameEngine.userActions.onNext(UserAction.ActionButtonClicked)

    fun onResetConfirmClicked() = gameEngine.userActions.onNext(UserAction.ActionButtonClicked)

    fun onResetConfirmationDialogDismissed() {
        dialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}