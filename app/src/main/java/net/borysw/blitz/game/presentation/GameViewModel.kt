package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import net.borysw.blitz.game.engine.dialog.Dialog
import net.borysw.blitz.game.engine.dialog.Dialogs
import net.borysw.blitz.game.engine.game.GameEngine
import net.borysw.blitz.game.engine.userActions.UserAction.ActionButtonClicked
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.userActions.UserAction.ResetConfirmed
import net.borysw.blitz.game.engine.userActions.UserActions
import net.borysw.blitz.game.status.GameInfo
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val userActions: UserActions,
    gameEngine: GameEngine,
    dialogs: Dialogs
) : ViewModel() {

    private val disposables by lazy { CompositeDisposable() }

    val gameInfo by lazy { MutableLiveData<GameInfo>() }
    val dialog by lazy { MutableLiveData<Dialog>() }

    init {
        gameEngine.gameInfo
            .doOnNext { d("Game info: $it") }
            .subscribe(gameInfo::postValue, ::e)
            .run(disposables::add)

        dialogs.dialogs
            .subscribe(dialog::postValue, ::e)
            .run(disposables::add)
    }

    fun onPlayer1Clicked() = userActions.onUserAction(ClockClickedPlayer1)

    fun onPlayer2Clicked() = userActions.onUserAction(ClockClickedPlayer2)

    fun onActionButtonClicked() = userActions.onUserAction(ActionButtonClicked)

    fun onResetConfirmClicked() = userActions.onUserAction(ResetConfirmed)

    override fun onCleared() = disposables.dispose()
}