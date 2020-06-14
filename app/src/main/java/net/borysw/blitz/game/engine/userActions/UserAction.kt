package net.borysw.blitz.game.engine.userActions

sealed class UserAction {
    object ClockClickedPlayer1 : UserAction()
    object ClockClickedPlayer2 : UserAction()
    object ActionButtonClicked : UserAction()
    object ResetConfirmed : UserAction()
    object SitAtTable : UserAction()
}