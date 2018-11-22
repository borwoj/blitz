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
import kotlinx.android.synthetic.main.fragment_game.timerA
import kotlinx.android.synthetic.main.fragment_game.timerB
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
    timerA.setOnClickListener { viewModel.timerAClicked() }
    timerB.setOnClickListener { viewModel.timberBClicked() }
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
      FINISHED -> showGameFinished()
      PLAYER_A, PLAYER_B -> showPlayerActive(gameStatus.status)
    }
    timerA.setTime(gameStatus.timeA)
    timerB.setTime(gameStatus.timeB)
  }

  private fun showGameInitial() {
    start.setImageResource(R.drawable.ic_play_arrow_black_24dp)
  }

  private fun showPlayerActive(gameStatus: GameStatus.Status) {
    if (gameStatus == PLAYER_A) {
      timerA.isActive = true
    } else if (gameStatus == PLAYER_B) {
      timerA.isActive = false
    }
    timerB.isActive = !timerA.isActive

    beginDelayedTransition(root, ChangeBounds().apply {
      interpolator = OvershootInterpolator(1f)
      duration = 250
    })
    getConstraintSet(gameStatus).applyTo(root)
  }

  private fun showGameFinished() {
    Snackbar.make(timerA, "Game finished", Snackbar.LENGTH_INDEFINITE).show()
    start.setImageResource(R.drawable.ic_replay_black_24dp)

    beginDelayedTransition(root, ChangeBounds().apply {
      interpolator = AnticipateOvershootInterpolator()
      duration = 1000
    })
    getConstraintSet(FINISHED).applyTo(root)
  }

  private fun getConstraintSet(gameStatus: GameStatus.Status): ConstraintSet {
    val layoutResId = when (gameStatus) {
      INITIAL -> fragment_game
      FINISHED -> fragment_game_player_finish
      PLAYER_A -> fragment_game_player_a
      PLAYER_B -> fragment_game_player_b
    }
    return ConstraintSet().apply { clone(context, layoutResId) }
  }
}
