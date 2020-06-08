package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.borysw.blitz.SafeDisposable
import net.borysw.blitz.game.UserAction.ActionButtonClicked
import net.borysw.blitz.game.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.UserAction.SitAtTable
import net.borysw.blitz.game.engine.UserActions
import net.borysw.blitz.game.engine.game.GameEngine
import net.borysw.blitz.game.status.GameInfo
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val userActions: UserActions,
    gameEngine: GameEngine
) : ViewModel() {

    private val timeDisposable by lazy { SafeDisposable() }

    val gameInfo by lazy { MutableLiveData<GameInfo>() }
    val dialog by lazy { MutableLiveData<Dialog>() }

    init {
        gameEngine.gameInfo
            .subscribe(gameInfo::postValue, ::e)
            .run(timeDisposable::set)
        // TODO handle error gracefully
        userActions.onUserAction(SitAtTable)
    }

    fun onPlayer1Clicked() = userActions.onUserAction(ClockClickedPlayer1)

    fun onPlayer2Clicked() = userActions.onUserAction(ClockClickedPlayer2)

    fun onActionButtonClicked() = userActions.onUserAction(ActionButtonClicked)

    fun onResetConfirmClicked() = userActions.onUserAction(ActionButtonClicked)

    fun onResetConfirmationDialogDismissed() {
        dialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}