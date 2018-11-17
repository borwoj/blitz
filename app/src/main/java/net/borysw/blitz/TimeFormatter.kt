package net.borysw.blitz

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeFormatter @Inject constructor() {

  fun format(timeMs: Long) = String.format(
    "%02d:%02d:%02d:%02d",
    TimeUnit.MILLISECONDS.toHours(timeMs),
    TimeUnit.MILLISECONDS.toMinutes(timeMs) % TimeUnit.HOURS.toMinutes(1),
    TimeUnit.MILLISECONDS.toSeconds(timeMs) % TimeUnit.MINUTES.toSeconds(1),
    TimeUnit.MILLISECONDS.toMillis(timeMs) % TimeUnit.SECONDS.toMillis(1)
  )
}