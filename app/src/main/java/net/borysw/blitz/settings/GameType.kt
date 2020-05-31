package net.borysw.blitz.settings

sealed class GameType {
    object Standard : GameType()
    data class SimpleDelay(val delay: Long) : GameType()
    data class BronsteinDelay(val delay: Long) : GameType()
    data class Increment(val incrementBy: Long) : GameType()
}