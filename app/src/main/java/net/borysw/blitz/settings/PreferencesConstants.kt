package net.borysw.blitz.settings

object PreferencesConstants {
    const val KEY_GAME_DURATION = "game_duration"
    const val KEY_GAME_TYPE = "game_type"
    const val KEY_SOUND_ENABLED = "sound_enabled"
    const val KEY_TIME_UNIT = "time_unit"
    const val KEY_DELAY = "delay"
    const val KEY_INCREMENT_BY = "increment_by"

    const val TIME_UNIT_SECONDS = "0"
    const val TIME_UNIT_MINUTES = "1"
    const val TIME_UNIT_HOURS = "2"

    const val GAME_TYPE_STANDARD = "0"
    const val GAME_TYPE_SIMPLE_DELAY = "1"
    const val GAME_TYPE_BRONSTEIN_DELAY = "2"
    const val GAME_TYPE_FISCHER = "3"
}