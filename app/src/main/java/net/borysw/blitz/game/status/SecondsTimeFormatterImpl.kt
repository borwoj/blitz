package net.borysw.blitz.game.status

import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject

class SecondsTimeFormatterImpl @Inject constructor() :
    TimeFormatter {
    override fun format(time: Long) = String.format(
        "%02d:%02d:%02d",
        MILLISECONDS.toHours(time),
        MILLISECONDS.toMinutes(time) % HOURS.toMinutes(1),
        MILLISECONDS.toSeconds(time) % MINUTES.toSeconds(1)
    )
}