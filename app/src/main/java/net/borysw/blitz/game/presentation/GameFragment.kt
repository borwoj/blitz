package net.borysw.blitz.game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_game.start
import kotlinx.android.synthetic.main.fragment_game.timerA
import kotlinx.android.synthetic.main.fragment_game.timerB
import net.borysw.blitz.R
import net.borysw.blitz.app.ViewModelFactory
import javax.inject.Inject

class GameFragment : Fragment() {

  private lateinit var viewModel: GameViewModel
  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidSupportInjection.inject(this)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_game, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

    start.setOnClickListener { viewModel.onStartClicked() }
    timerA.setOnClickListener { viewModel.onSwitchClicked() }
    timerB.setOnClickListener { viewModel.onSwitchClicked() }
    subscribe()
  }

  private fun subscribe() {
    viewModel.gameStatus.observe(viewLifecycleOwner, Observer { gameStatus ->
      timerA.setTime(gameStatus.timeA)
      timerB.setTime(gameStatus.timeB)
      if (gameStatus.isFinished) {
        showGameFinished()
      }
    })
  }

  private fun showGameFinished() {
    Snackbar.make(timerA, "Game finished", Snackbar.LENGTH_INDEFINITE).show()
  }
}