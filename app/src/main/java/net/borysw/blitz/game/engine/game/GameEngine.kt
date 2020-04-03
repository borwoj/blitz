package net.borysw.blitz.game.engine.game

import io.reactivex.Observable
import io.reactivex.subjects.Subject
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.status.GameStatus

interface GameEngine {
    val gameStatus: Observable<GameStatus>
    val userActions: Subject<UserAction>
}