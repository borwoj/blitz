package net.borysw.blitz.game

data class ClockStatus(val timeA: Long, val timeB: Long, val activeTimer: ActiveTimer)

enum class ActiveTimer{
  A,B
}
