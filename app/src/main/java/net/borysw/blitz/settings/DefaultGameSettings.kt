package net.borysw.blitz.settings

import java.util.concurrent.TimeUnit

object DefaultGameSettings {
    val duration = TimeUnit.MINUTES.toMillis(5)
    val type = GameType.Standard
}