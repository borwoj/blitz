package net.borysw.blitz.game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager.beginDelayedTransition
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.root
import kotlinx.android.synthetic.main.fragment_game.start
import kotlinx.android.synthetic.main.fragment_game.timerViewA
import kotlinx.android.synthetic.main.fragment_game.timerViewB
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
    return inflater.inflate(fragment_game, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

    start.setOnClickListener { viewModel.onStartClicked() }
    timerViewA.setOnClickListener { viewModel.onTimerAClicked() }
    timerViewB.setOnClickListener { viewModel.onTimerBClicked() }
    subscribe()
  }

  private fun subscribe() {
    viewModel.gameStatus.observe(viewLifecycleOwner, Observer { gameStatus ->
      Timber.d("Game status: $gameStatus")
      showGameStatus(gameStatus)
    })
  }

  private fun showGameStatus(gameStatus: GameStatus) {
    when (gameStatus.status) {
      INITIAL -> showGameInitial()
      IN_PROGRESS_PLAYER_A, IN_PROGRESS_PLAYER_B -> showGameInProgress(gameStatus.status)
      FINISHED_PLAYER_A, FINISHED_PLAYER_B -> showGameFinished(gameStatus.status)
    }
    timerViewA.setTime(gameStatus.timeA)
    timerViewB.setTime(gameStatus.timeB)
  }

  private fun showGameInitial() {
    //start.setImageResource(R.drawable.ic_play_arrow_black_24dp)
    beginDelayedTransition(root, ChangeBounds().apply {
      interpolator = AnticipateOvershootInterpolator()
      duration = 500
    })
    getConstraintSet(INITIAL).applyTo(root)
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
      INITIAL -> fragment_game
      IN_PROGRESS_PLAYER_A -> fragment_game_player_a
      IN_PROGRESS_PLAYER_B -> fragment_game_player_b
      FINISHED_PLAYER_A, FINISHED_PLAYER_B -> fragment_game_finish
    }
    return ConstraintSet().apply { clone(context, layoutResId) }
  }
}
