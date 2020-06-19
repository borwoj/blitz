package net.borysw.blitz.game.engine.dialog

sealed class Dialog(var isDismissed: Boolean = false) {
    class ResetConfirmation : Dialog()
}