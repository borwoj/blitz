package net.borysw.blitz.game.presentation

import android.content.Context
import androidx.constraintlayout.widget.ConstraintSet
import net.borysw.blitz.R
import net.borysw.blitz.game.status.GameInfo.Status
import net.borysw.blitz.game.status.GameInfo.Status.Finished
import net.borysw.blitz.game.status.GameInfo.Status.InProgress
import net.borysw.blitz.game.status.GameInfo.Status.Paused
import net.borysw.blitz.game.status.GameInfo.Status.Unstarted
import javax.inject.Inject

class ConstraintSetProvider @Inject constructor() {
    fun get(gameStatus: Status, context: Context): ConstraintSet {
        val layoutResId = when (gameStatus) {
            Unstarted -> R.layout.fragment_game_initial
            Paused -> R.layout.fragment_game_paused
            InProgress.Player1Turn -> R.layout.fragment_game_player_a
            InProgress.Player2Turn -> R.layout.fragment_game_player_b
            Finished.Player1Won, Finished.Player2Won -> R.layout.fragment_game_finish
        }
        return ConstraintSet().apply { clone(context, layoutResId) }
    }
}