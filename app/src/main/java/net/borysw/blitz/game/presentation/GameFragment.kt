package net.borysw.blitz.game.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
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
import net.borysw.blitz.R.layout.fragment_game_initial
import net.borysw.blitz.game.engine.dialog.Dialog
import net.borysw.blitz.game.status.GameInfo
import net.borysw.blitz.game.status.GameInfo.Status
import net.borysw.blitz.game.status.GameInfo.Status.Finished
import net.borysw.blitz.game.status.GameInfo.Status.InProgress
import net.borysw.blitz.game.status.GameInfo.Status.Paused
import net.borysw.blitz.game.status.GameInfo.Status.Unstarted
import javax.inject.Inject

class GameFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<GameViewModel> { viewModelFactory }

    @Inject
    lateinit var constraintSetProvider: ConstraintSetProvider

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

        observe()
    }

    private fun observe() {
        viewModel.gameInfo
            .observe(viewLifecycleOwner, Observer { gameStatus ->
                showGameInfo(gameStatus)
            })
        // viewModel.dialog
        //     .observe(viewLifecycleOwner, Observer { dialog ->
        //         if (!dialog.isDismissed) showResetConfirmationDialog()
        //     })
    }

    private fun showResetConfirmationDialog(dialog: Dialog.ResetConfirmation) {
        AlertDialog.Builder(requireContext()).setTitle(R.string.reset_confirmation_title)
            .setPositiveButton(R.string.reset) { _, _ -> viewModel.onResetConfirmClicked() }
            .setNegativeButton(R.string.cancel, null)
            .setOnDismissListener { dialog.isDismissed = true }
            .show()
    }

    private fun showGameInfo(gameInfo: GameInfo) {
        timerViewA.setTime(gameInfo.remainingTimePlayer1)
        timerViewB.setTime(gameInfo.remainingTimePlayer2)

        when (gameInfo.status) {
            Unstarted -> showGameInitial()
            Paused -> showGamePaused()
            InProgress.Player1Turn, InProgress.Player2Turn -> showGameInProgress(gameInfo.status)
            Finished.Player1Won, Finished.Player2Won -> showGameFinished(gameInfo.status)
        }

        when (val dialog = gameInfo.dialog) {
            is Dialog.ResetConfirmation -> if (!dialog.isDismissed) showResetConfirmationDialog(
                dialog
            )
        }
    }

    private fun showGameInitial() {
        timerViewA.setActive(false)
        timerViewB.setActive(false)

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 500
        })
        constraintSetProvider.get(Unstarted, requireContext()).applyTo(root)
    }

    private fun showGamePaused() {
        // could be in view model
        start.setImageResource(R.drawable.ic_replay_black_24dp)

        timerViewA.setActive(false)
        timerViewB.setActive(false)

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 500
        })
        constraintSetProvider.get(Paused, requireContext()).applyTo(root)
    }

    private fun showGameInProgress(gameStatus: Status) {
        start.setImageResource(R.drawable.ic_pause_black_24dp)

        when (gameStatus) {
            InProgress.Player1Turn -> {
                timerViewA.setActive(true)
                timerViewB.setActive(false)
            }
            InProgress.Player2Turn -> {
                timerViewA.setActive(false)
                timerViewB.setActive(true)
            }
        }

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = OvershootInterpolator(1f)
            duration = 250
        })
        constraintSetProvider.get(gameStatus, requireContext()).applyTo(root)
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
        // TODO could be in view model
        constraintSetProvider.get(gameStatus, requireContext()).applyTo(root)
    }
}
