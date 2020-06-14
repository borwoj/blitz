package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.borysw.blitz.SafeDisposable
import net.borysw.blitz.game.engine.game.GameEngine
import net.borysw.blitz.game.engine.userActions.UserAction.ActionButtonClicked
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.userActions.UserAction.ResetConfirmed
import net.borysw.blitz.game.engine.userActions.UserAction.SitAtTable
import net.borysw.blitz.game.engine.userActions.UserActions
import net.borysw.blitz.game.status.GameInfo
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val userActions: UserActions,
    gameEngine: GameEngine
) : ViewModel() {

    private val timeDisposable by lazy { SafeDisposable() }

    val gameInfo by lazy { MutableLiveData<GameInfo>() }
    //val dialog by lazy { MutableLiveData<Dialog>() }

    init {
        // TODO handle error gracefully
        gameEngine.gameInfo
            .doOnNext { d("Game info: $it") }
            .subscribe(gameInfo::postValue, ::e)
            .run(timeDisposable::set)

        userActions.onUserAction(SitAtTable)
    }

    fun onPlayer1Clicked() = userActions.onUserAction(ClockClickedPlayer1)

    fun onPlayer2Clicked() = userActions.onUserAction(ClockClickedPlayer2)

    fun onActionButtonClicked() = userActions.onUserAction(ActionButtonClicked)

    fun onResetConfirmClicked() = userActions.onUserAction(ResetConfirmed)

    override fun onCleared() = timeDisposable.dispose()
}