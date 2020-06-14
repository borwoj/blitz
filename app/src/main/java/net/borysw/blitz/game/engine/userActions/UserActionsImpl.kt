package net.borysw.blitz.game.engine.userActions

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class UserActionsImpl @Inject constructor() :
    UserActions {
    private val userActionsSubject: Subject<UserAction> = PublishSubject.create()

    override val userActions: Observable<UserAction> =
        userActionsSubject.startWith(UserAction.SitAtTable)

    override fun onUserAction(userAction: UserAction) = userActionsSubject.onNext(userAction)
}