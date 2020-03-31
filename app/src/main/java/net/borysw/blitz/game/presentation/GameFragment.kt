package net.borysw.blitz.game.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager.beginDelayedTransition
import dagger.android.support.AndroidSupportInjection.inject
import kotlinx.android.synthetic.main.fragment_game_initial.*
import net.borysw.blitz.R
import net.borysw.blitz.R.layout.fragment_game_finish
import net.borysw.blitz.R.layout.fragment_game_initial
import net.borysw.blitz.R.layout.fragment_game_paused
import net.borysw.blitz.R.layout.fragment_game_player_a
import net.borysw.blitz.R.layout.fragment_game_player_b
import net.borysw.blitz.game.status.GameStatus
import net.borysw.blitz.game.status.GameStatus.Status
import net.borysw.blitz.game.status.GameStatus.Status.Finished
import net.borysw.blitz.game.status.GameStatus.Status.InProgress
import net.borysw.blitz.game.status.GameStatus.Status.Paused
import net.borysw.blitz.game.status.GameStatus.Status.Unstarted
import timber.log.Timber
import javax.inject.Inject

class GameFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<GameViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(fragment_game_initial, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start.setOnClickListener { viewModel.onActionButtonClicked() }
        settings.setOnClickListener { findNavController().navigate(R.id.action_clockFragment_to_settingsFragment) }
        timerViewA.setOnClickListener { viewModel.onPlayer1Clicked() }
        timerViewB.setOnClickListener { viewModel.onPlayer2Clicked() }
        subscribe()
    }

    private fun subscribe() {
        viewModel.gameStatus.observe(viewLifecycleOwner, Observer { gameStatus ->
            Timber.d("Game status: $gameStatus")
            showGameStatus(gameStatus)
        })
        viewModel.showDialog.observe(viewLifecycleOwner, Observer { showDialog ->
            if (!showDialog.isDismissed) showResetConfirmationDialog()
        })
    }

    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(requireContext()).setTitle(R.string.reset_confirmation_title)
            .setPositiveButton(R.string.reset) { _, _ -> viewModel.onResetConfirmClicked() }
            .setNegativeButton(R.string.cancel, null)
            .setOnDismissListener { viewModel.onResetConfirmationDialogDismissed() }
            .show()
    }

    private fun showGameStatus(gameStatus: GameStatus) {
        timerViewA.setTime(gameStatus.remainingTimePlayer1)
        timerViewB.setTime(gameStatus.remainingTimePlayer2)

        when (gameStatus.status) {
            Unstarted -> showGameInitial()
            Paused -> showGamePaused()
            InProgress.Player1, InProgress.Player2 -> showGameInProgress(gameStatus.status)
            Finished.Player1Won, Finished.Player2Won -> showGameFinished(gameStatus.status)
        }
    }

    private fun showGameInitial() {
        timerViewA.setActive(false)
        timerViewB.setActive(false)

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 500
        })
        getConstraintSet(Unstarted).applyTo(root)
    }

    private fun showGamePaused() {
        start.setImageResource(R.drawable.ic_replay_black_24dp)

        timerViewA.setActive(false)
        timerViewB.setActive(false)

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 500
        })
        getConstraintSet(Paused).applyTo(root)
    }

    private fun showGameInProgress(gameStatus: Status) {
        start.setImageResource(R.drawable.ic_pause_black_24dp)

        when (gameStatus) {
            InProgress.Player1 -> {
                timerViewA.setActive(true)
                timerViewB.setActive(false)
            }
            InProgress.Player2 -> {
                timerViewA.setActive(false)
                timerViewB.setActive(true)
            }
        }

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = OvershootInterpolator(1f)
            duration = 250
        })
        getConstraintSet(gameStatus).applyTo(root)
    }

    private fun showGameFinished(gameStatus: Status) {
        start.setImageResource(R.drawable.ic_replay_black_24dp)

        when (gameStatus) {
            Finished.Player1Won -> {
                timerViewA.setLoser()
                timerViewB.setWinner()
            }
            Finished.Player2Won -> {
                timerViewA.setWinner()
                timerViewB.setLoser()
            }
        }

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 1000
        })
        getConstraintSet(gameStatus).applyTo(root)
    }

    private fun getConstraintSet(gameStatus: Status): ConstraintSet {
        val layoutResId = when (gameStatus) {
            Unstarted -> fragment_game_initial
            Paused -> fragment_game_paused
            InProgress.Player1 -> fragment_game_player_a
            InProgress.Player2 -> fragment_game_player_b
            Finished.Player1Won, Finished.Player2Won -> fragment_game_finish
        }
        return ConstraintSet().apply { clone(context, layoutResId) }
    }
}
