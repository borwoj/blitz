package net.borysw.blitz.game.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.borysw.blitz.SafeDisposable
import net.borysw.blitz.game.Game
import net.borysw.blitz.game.GameController
import net.borysw.blitz.game.status.GameStatus
import timber.log.Timber.e
import javax.inject.Inject

class GameViewModel @Inject constructor(private val gameController: GameController) : ViewModel() {

    val gameStatus by lazy { MutableLiveData<GameStatus>() }
    val showDialog by lazy { MutableLiveData<ShowDialog>() }

    private val timeDisposable by lazy { SafeDisposable() }

    init {
        gameController.game = Game(2000)
    }

    private fun subscribe() {
        gameController.gameStatus.subscribe(gameStatus::postValue, ::e).run(timeDisposable::set)
    }

    fun onTimerAClicked() {
        gameController.onPlayer1Clicked()
        subscribe()
    }

    fun onTimerBClicked() {
        gameController.onPlayer2Clicked()
        subscribe()
    }

    fun onActionButtonClicked() {
        if (gameController.isGamePaused) showDialog.postValue(ShowDialog())
        else {
            gameController.onPauseClicked()
            timeDisposable.dispose()
        }
    }

    fun onResetConfirmClicked() = gameController.onResetClicked()

    fun onResetConfirmationDialogDismissed() {
        showDialog.value?.isDismissed = true
    }

    override fun onCleared() = timeDisposable.dispose()
}