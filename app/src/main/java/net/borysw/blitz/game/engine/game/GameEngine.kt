package net.borysw.blitz.game.engine.game

import io.reactivex.Observable
import net.borysw.blitz.game.status.GameInfo

interface GameEngine {
    val gameInfo: Observable<GameInfo>
}