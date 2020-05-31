package net.borysw.blitz.game.engine.clock

import io.reactivex.Observable
import net.borysw.blitz.game.engine.clock.type.BronsteinChessClockImpl
import net.borysw.blitz.game.engine.clock.type.FischerChessClockImpl
import net.borysw.blitz.game.engine.clock.type.SimpleDelayChessClockImpl
import net.borysw.blitz.game.engine.clock.type.StandardChessClockImpl
import net.borysw.blitz.settings.GameType
import net.borysw.blitz.settings.Settings
import javax.inject.Inject

class ChessClockProviderImpl @Inject constructor(
    settings: Settings,
    standardChessClockImpl: StandardChessClockImpl,
    fischerChessClockImpl: FischerChessClockImpl,
    simpleDelayChessClockImpl: SimpleDelayChessClockImpl,
    bronsteinChessClockImpl: BronsteinChessClockImpl
) :
    ChessClockProvider {
    override val chessClock: Observable<ChessClock> = settings.gameSettings.map {
        val clock = when (val gameType = it.type) {
            GameType.Standard -> standardChessClockImpl
            is GameType.SimpleDelay -> simpleDelayChessClockImpl.apply { delay = gameType.delay }
            is GameType.BronsteinDelay -> bronsteinChessClockImpl.apply { delay = gameType.delay }
            is GameType.Increment -> fischerChessClockImpl.apply {
                incrementBy = gameType.incrementBy
            }
        }
        clock.initialTime = it.duration
        clock
    }
}