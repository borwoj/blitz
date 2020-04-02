package net.borysw.blitz.settings

import java.util.concurrent.TimeUnit

object DefaultSettings {
    const val soundEnabled = true

    val duration = TimeUnit.MINUTES.toMillis(5)
    val type = GameType.Standard
}