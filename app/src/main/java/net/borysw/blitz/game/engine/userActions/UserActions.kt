package net.borysw.blitz.game.engine.userActions

import io.reactivex.Observable

interface UserActions {
    val userActions: Observable<UserAction>
    fun onUserAction(userAction: UserAction)
}