package net.borysw.blitz.game.engine

import io.reactivex.Observable
import net.borysw.blitz.game.UserAction

interface UserActions {
    val userActions: Observable<UserAction>
    fun onUserAction(userAction: UserAction)
}