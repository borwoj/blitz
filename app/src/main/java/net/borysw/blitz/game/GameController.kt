package net.borysw.blitz.game

import io.reactivex.Observable
import io.reactivex.subjects.Subject
import net.borysw.blitz.game.status.GameStatus

interface GameController {
    val gameStatus: Observable<GameStatus>
    val userActions: Subject<UserAction>
}