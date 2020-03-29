package net.borysw.blitz.game.settings

import java.util.concurrent.TimeUnit

object DefaultSettings {
    val gameDuration = TimeUnit.MINUTES.toMillis(5)
    val type = GameType.Standard
}