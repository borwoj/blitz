package net.borysw.blitz.game.engine.dialog

import io.reactivex.Observable

interface Dialogs {
    val dialogs: Observable<Dialog>
    fun showDialog(dialog: Dialog)
}