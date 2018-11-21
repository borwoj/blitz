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
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.root
import kotlinx.android.synthetic.main.fragment_game.start
import kotlinx.android.synthetic.main.fragment_game.timerA
import kotlinx.android.synthetic.main.fragment_game.timerB
import net.borysw.blitz.R
import net.borysw.blitz.R.layout.*
import net.borysw.blitz.app.ViewModelFactory
import net.borysw.blitz.game.presentation.GameStatus.Status.*
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
  }

  private fun showPlayerActive(gameStatus: GameStatus.Status) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(context, getConstraintSet(gameStatus))
    val transition = ChangeBounds()
    transition.interpolator = OvershootInterpolator(1f)
    transition.duration = 250
    TransitionManager.beginDelayedTransition(root, transition)
    constraintSet.applyTo(root)

    if (gameStatus == PLAYER_A) {
      timerA.isActive = true
    } else if (gameStatus == PLAYER_B) {
      timerA.isActive = false
    }
    timerB.isActive = !timerA.isActive
  }

  private fun getConstraintSet(gameStatus: GameStatus.Status) = when (gameStatus) {
    INITIAL -> fragment_game
    FINISHED -> fragment_game_player_finish
    PLAYER_A -> fragment_game_player_a
    PLAYER_B -> fragment_game_player_b
  }

  private fun showGameFinished() {
    Snackbar.make(timerA, "Game finished", Snackbar.LENGTH_INDEFINITE).show()
    val constraintSet = ConstraintSet()
    constraintSet.clone(context, getConstraintSet(FINISHED))
    val transition = ChangeBounds()
    transition.interpolator = AnticipateOvershootInterpolator()
    transition.duration = 1000
    TransitionManager.beginDelayedTransition(root, transition)
    constraintSet.applyTo(root)

    start.setImageResource(R.drawable.ic_replay_black_24dp)
  }
}