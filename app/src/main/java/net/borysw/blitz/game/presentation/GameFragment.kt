package net.borysw.blitz.game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
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
import net.borysw.blitz.app.ViewModelFactory
import net.borysw.blitz.game.presentation.PlayerMove.PLAYER_A
import net.borysw.blitz.game.presentation.PlayerMove.PLAYER_B
import timber.log.Timber
import javax.inject.Inject

class GameFragment : Fragment() {

  private lateinit var viewModel: GameViewModel
  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_game, container, false)
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
      timerA.setTime(gameStatus.timeA)
      timerB.setTime(gameStatus.timeB)
      if (gameStatus.isFinished) {
        showGameFinished()
      } else {
        showPlayerMove(gameStatus.playerMove)
      }
    })
  }

  private fun showPlayerMove(playerMove: PlayerMove) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(context, getConstraintSet(playerMove))
    val transition = ChangeBounds()
    transition.interpolator = OvershootInterpolator(1f)
    transition.duration = 250
    TransitionManager.beginDelayedTransition(root, transition)
    constraintSet.applyTo(root)
  }

  private fun getConstraintSet(playerMove: PlayerMove) = when (playerMove) {
    PLAYER_A -> R.layout.fragment_game_player_a
    PLAYER_B -> R.layout.fragment_game_player_b
  }

  private fun showGameFinished() {
    Snackbar.make(timerA, "Game finished", Snackbar.LENGTH_INDEFINITE).show()
    val constraintSet = ConstraintSet()
    constraintSet.clone(context, R.layout.fragment_game_player_finish)
    val transition = ChangeBounds()
    transition.interpolator = AnticipateOvershootInterpolator()
    transition.duration = 1000
    TransitionManager.beginDelayedTransition(root, transition)
    constraintSet.applyTo(root)
  }
}