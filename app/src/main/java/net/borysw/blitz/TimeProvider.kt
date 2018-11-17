package net.borysw.blitz

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeProvider @Inject constructor() {
  fun currentTimeMillis(): Long = System.currentTimeMillis()
}
