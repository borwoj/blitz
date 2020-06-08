package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import net.borysw.blitz.game.engine.clock.type.BronsteinDelayDecorator
import net.borysw.blitz.game.engine.clock.type.ChessClockImpl
import net.borysw.blitz.game.engine.clock.type.FischerDecorator
import net.borysw.blitz.game.engine.clock.type.SimpleDelayDecorator
import net.borysw.blitz.settings.GameType.BronsteinDelay
import net.borysw.blitz.settings.GameType.Fischer
import net.borysw.blitz.settings.GameType.SimpleDelay
import net.borysw.blitz.settings.GameType.Standard
import net.borysw.blitz.settings.Settings
import javax.inject.Inject

class ChessClockProviderImpl @Inject constructor(
    settings: Settings,
    chessClockImpl: ChessClockImpl,
    fischerChessClock: FischerDecorator,
    simpleDelayChessClock: SimpleDelayDecorator,
    bronsteinChessClock: BronsteinDelayDecorator
) :
    ChessClockProvider {
    override val chessClock: Observable<ChessClock> = settings.gameSettings.map { gameSettings ->
        when (val gameType = gameSettings.type) {
            is Standard -> chessClockImpl
            is SimpleDelay -> simpleDelayChessClock.apply { delay = gameType.delay }
            is BronsteinDelay -> bronsteinChessClock.apply { delayAndIncrement = gameType.delay }
            is Fischer -> fischerChessClock.apply { incrementBy = gameType.incrementBy }
        }.apply {
            initialTime = gameSettings.duration
        }
    }
}