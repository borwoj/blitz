package net.borysw.blitz.game.status

import javax.inject.Inject

class TimeProvider @Inject constructor() {
    val currentTimeMillis: Long
        get() = System.currentTimeMillis()
}
