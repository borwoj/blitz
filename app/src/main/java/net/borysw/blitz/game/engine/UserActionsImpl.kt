package net.borysw.blitz.game.engine

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import net.borysw.blitz.game.UserAction
import javax.inject.Inject

class UserActionsImpl @Inject constructor() : UserActions {
    private val userActionsSubject: Subject<UserAction> = BehaviorSubject.create()
    override val userActions: Observable<UserAction> = userActionsSubject

    override fun onUserAction(userAction: UserAction) = userActionsSubject.onNext(userAction)
}