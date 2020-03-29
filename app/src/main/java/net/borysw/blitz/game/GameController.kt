package net.borysw.blitz.game

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.Subject
import net.borysw.blitz.game.status.GameStatus

interface GameController {
    var game: Game
    val gameStatus: Observable<GameStatus>
    var userActions: Subject<UserAction>
}