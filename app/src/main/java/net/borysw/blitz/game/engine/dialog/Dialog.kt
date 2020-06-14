package net.borysw.blitz.game.engine.dialog

sealed class Dialog(var isDismissed: Boolean = false) {
    object ResetConfirmation : Dialog()
}