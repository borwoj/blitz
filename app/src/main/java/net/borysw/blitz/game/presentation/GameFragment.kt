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
import net.borysw.blitz.game.status.GameStatus.Status.FINISHED_PLAYER_A
import net.borysw.blitz.game.status.GameStatus.Status.FINISHED_PLAYER_B
import net.borysw.blitz.game.status.GameStatus.Status.INITIAL
import net.borysw.blitz.game.status.GameStatus.Status.IN_PROGRESS_PLAYER_A
import net.borysw.blitz.game.status.GameStatus.Status.IN_PROGRESS_PLAYER_B
import net.borysw.blitz.game.status.GameStatus.Status.PAUSED
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

        start.setOnClickListener { viewModel.onPauseClicked() }
        settings.setOnClickListener { findNavController().navigate(R.id.action_clockFragment_to_settingsFragment) }
        timerViewA.setOnClickListener { viewModel.onTimerAClicked() }
        timerViewB.setOnClickListener { viewModel.onTimerBClicked() }
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
        timerViewA.setTime(gameStatus.timeA)
        timerViewB.setTime(gameStatus.timeB)

        when (gameStatus.status) {
            INITIAL -> showGameInitial()
            PAUSED -> showGamePaused()
            IN_PROGRESS_PLAYER_A, IN_PROGRESS_PLAYER_B -> showGameInProgress(gameStatus.status)
            FINISHED_PLAYER_A, FINISHED_PLAYER_B -> showGameFinished(gameStatus.status)
        }
    }

    private fun showGameInitial() {
        timerViewA.setActive(false)
        timerViewB.setActive(false)

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 500
        })
        getConstraintSet(INITIAL).applyTo(root)
    }

    private fun showGamePaused() {
        start.setImageResource(R.drawable.ic_replay_black_24dp)

        timerViewA.setActive(false)
        timerViewB.setActive(false)

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 500
        })
        getConstraintSet(PAUSED).applyTo(root)
    }

    private fun showGameInProgress(gameStatus: GameStatus.Status) {
        start.setImageResource(R.drawable.ic_pause_black_24dp)

        if (gameStatus == IN_PROGRESS_PLAYER_A) {
            timerViewA.setActive(true)
            timerViewB.setActive(false)
        } else if (gameStatus == IN_PROGRESS_PLAYER_B) {
            timerViewA.setActive(false)
            timerViewB.setActive(true)
        }

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = OvershootInterpolator(1f)
            duration = 250
        })
        getConstraintSet(gameStatus).applyTo(root)
    }

    private fun showGameFinished(gameStatus: GameStatus.Status) {
        start.setImageResource(R.drawable.ic_replay_black_24dp)

        if (gameStatus == FINISHED_PLAYER_A) {
            timerViewA.setLoser()
            timerViewB.setWinner()
        } else if (gameStatus == FINISHED_PLAYER_B) {
            timerViewA.setWinner()
            timerViewB.setLoser()
        }

        beginDelayedTransition(root, ChangeBounds().apply {
            interpolator = AnticipateOvershootInterpolator()
            duration = 1000
        })
        getConstraintSet(gameStatus).applyTo(root)
    }

    private fun getConstraintSet(gameStatus: GameStatus.Status): ConstraintSet {
        val layoutResId = when (gameStatus) {
            INITIAL -> fragment_game_initial
            PAUSED -> fragment_game_paused
            IN_PROGRESS_PLAYER_A -> fragment_game_player_a
            IN_PROGRESS_PLAYER_B -> fragment_game_player_b
            FINISHED_PLAYER_A, FINISHED_PLAYER_B -> fragment_game_finish
        }
        return ConstraintSet().apply { clone(context, layoutResId) }
    }
}
