package net.borysw.blitz.game

import io.reactivex.rxjava3.core.Observable
import net.borysw.blitz.game.status.GameStatus

interface GameController {
    var game: Game
    val gameStatus: Observable<GameStatus>
    val isGamePaused: Boolean
    fun onPlayer1Clicked()
    fun onPlayer2Clicked()
    fun onPauseClicked()
    fun onResetClicked()
}