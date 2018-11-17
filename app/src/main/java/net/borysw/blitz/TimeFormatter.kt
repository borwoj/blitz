package net.borysw.blitz

import java.util.concurrent.TimeUnit.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeFormatter @Inject constructor() {

  fun format(time: Long) = String.format(
    "%02d:%02d:%02d:%02d",
    MILLISECONDS.toHours(time),
    MILLISECONDS.toMinutes(time) % HOURS.toMinutes(1),
    MILLISECONDS.toSeconds(time) % MINUTES.toSeconds(1),
    MILLISECONDS.toMillis(time) % SECONDS.toMillis(1)
  )
}