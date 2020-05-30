package net.borysw.blitz.game.engine

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject.create
import io.reactivex.subjects.Subject
import net.borysw.blitz.game.UserAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserActions @Inject constructor() {

    private val userActionsSubject: Subject<UserAction> = create()
    val userActions: Observable<UserAction> = userActionsSubject

    fun onUserAction(userAction: UserAction) = userActionsSubject.onNext(userAction)
}