package net.borysw.blitz.game.engine

import io.reactivex.Observable
import io.reactivex.Observable.interval
import io.reactivex.Scheduler
import net.borysw.blitz.game.engine.Schedulers.COMPUTATION
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Named

class TimeEngineImpl @Inject constructor(@Named(COMPUTATION) computationScheduler: Scheduler) :
    TimeEngine {
    override val time: Observable<Long> = interval(1, MILLISECONDS, computationScheduler)
}