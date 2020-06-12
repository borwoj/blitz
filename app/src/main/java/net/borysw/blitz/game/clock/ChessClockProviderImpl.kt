package net.borysw.blitz.game.clock

import io.reactivex.Observable
import net.borysw.blitz.game.clock.type.BasicChessClockImpl
import net.borysw.blitz.game.clock.type.BronsteinDelayDecorator
import net.borysw.blitz.game.clock.type.ChessClock
import net.borysw.blitz.game.clock.type.FischerDecorator
import net.borysw.blitz.game.clock.type.SimpleDelayDecorator
import net.borysw.blitz.settings.GameType.BronsteinDelay
import net.borysw.blitz.settings.GameType.Fischer
import net.borysw.blitz.settings.GameType.SimpleDelay
import net.borysw.blitz.settings.GameType.Standard
import net.borysw.blitz.settings.Settings
import javax.inject.Inject

class ChessClockProviderImpl @Inject constructor(
    settings: Settings,
    basicChessClockImpl: BasicChessClockImpl,
    fischerChessClock: FischerDecorator,
    simpleDelayChessClock: SimpleDelayDecorator,
    bronsteinChessClock: BronsteinDelayDecorator
) :
    ChessClockProvider {
    override val chessClock: Observable<ChessClock> = settings.gameSettings.map { gameSettings ->
        when (val gameType = gameSettings.type) {
            is Standard -> basicChessClockImpl
            is SimpleDelay -> simpleDelayChessClock.apply { delay = gameType.delay }
            is BronsteinDelay -> bronsteinChessClock.apply { delayAndIncrement = gameType.delay }
            is Fischer -> fischerChessClock.apply { incrementBy = gameType.incrementBy }
        }.apply {
            initialTime = gameSettings.duration
        }
    }
}