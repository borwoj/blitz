package net.borysw.blitz.game

import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

class TimeFormatter {

    fun format(time: Long) = String.format(
        "%02d:%02d:%02d",
        MILLISECONDS.toHours(time),
        MILLISECONDS.toMinutes(time) % HOURS.toMinutes(1),
        MILLISECONDS.toSeconds(time) % MINUTES.toSeconds(1)
    )

    /*
    TODO inject
     */
    fun formatWithMs(time: Long) = String.format(
        "%02d:%02d:%02d:%02d",
        MILLISECONDS.toHours(time),
        MILLISECONDS.toMinutes(time) % HOURS.toMinutes(1),
        MILLISECONDS.toSeconds(time) % MINUTES.toSeconds(1),
        MILLISECONDS.toMillis(time) % SECONDS.toMillis(1)
    )
}