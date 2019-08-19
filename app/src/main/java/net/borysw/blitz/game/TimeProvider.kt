package net.borysw.blitz.game

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeProvider @Inject constructor() {
    fun currentTimeMillis(): Long = System.currentTimeMillis()
}
