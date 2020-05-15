package net.borysw.blitz.game.engine.game

import io.reactivex.Observable
import io.reactivex.subjects.Subject
import net.borysw.blitz.game.UserAction
import net.borysw.blitz.game.status.GameInfo

interface GameEngine {
    val gameInfo: Observable<GameInfo>
    val userActions: Subject<UserAction>
}