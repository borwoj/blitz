package net.borysw.blitz.game.status

import net.borysw.blitz.game.engine.dialog.Dialog

data class GameInfo(
    val remainingTimePlayer1: String,
    val remainingTimePlayer2: String,
    val status: Status,
    val dialog: Dialog
) {
    sealed class Status {
        object Unstarted : Status()

        object Paused : Status()

        sealed class InProgress : Status() {
            object Player1Turn : InProgress()
            object Player2Turn : InProgress()
        }

        sealed class Finished : Status() {
            object Player1Won : Finished()
            object Player2Won : Finished()
        }
    }
}