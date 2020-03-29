package net.borysw.blitz.game

sealed class UserAction {
    object ClockClickedPlayer1 : UserAction()
    object ClockClickedPlayer2 : UserAction()
    object ActionButtonClicked : UserAction()
    object SitAtTable : UserAction()
}