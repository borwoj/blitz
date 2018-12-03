package net.borysw.blitz.game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager.beginDelayedTransition
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game_initial.root
import kotlinx.android.synthetic.main.fragment_game_initial.start
import kotlinx.android.synthetic.main.fragment_game_initial.timerViewA
import kotlinx.android.synthetic.main.fragment_game_initial.timerViewB
import net.borysw.blitz.R
import net.borysw.blitz.R.layout.*
import net.borysw.blitz.app.ViewModelFactory
import net.borysw.blitz.game.GameStatus
import net.borysw.blitz.game.GameStatus.Status.*
import timber.log.Timber
import javax.inject.Inject

class GameFragment : Fragment() {
  @Inject
  lateinit var viewModelFactory: ViewModelFactory
  private lateinit var viewModel: GameViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(fragment_game_initial, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

    start.setOnClickListener { viewModel.onPauseClicked() }
    timerViewA.setOnClickListener { viewModel.onTimerAClicked() }
    timerViewB.setOnClickListener { viewModel.onTimerBClicked() }
    subscribe()
  }

  private fun subscribe() {
    viewModel.gameStatus.observe(viewLifecycleOwner, Observer { gameStatus ->
      Timber.d("Game status: $gameStatus")
      showGameStatus(gameStatus)
    })
    viewModel.showDialog.observe(viewLifecycleOwner, Observer {
      it?.let { showResetConfirmationDialog() }
    })
  }

  private fun showResetConfirmationDialog() {
    AlertDialog.Builder(context!!).setTitle(R.string.reset_confirmation_title)
      .setPositiveButton(R.string.reset) { _, _ -> viewModel.onResetConfirmClicked() }
      .setNegativeButton(R.string.cancel, null).show()
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
