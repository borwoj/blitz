package net.borysw.blitz.game.engine.dialog

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

class DialogsImpl @Inject constructor() : Dialogs {
    private val dialogsSubject: Subject<Dialog> = BehaviorSubject.create()

    override val dialogs: Observable<Dialog> = dialogsSubject

    override fun showDialog(dialog: Dialog) = dialogsSubject.onNext(dialog)
}