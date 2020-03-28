package net.borysw.blitz.game

data class GameStatus(val status: Status, val timeA: String, val timeB: String) {
    enum class Status {
        INITIAL, PAUSED, IN_PROGRESS_PLAYER_A, IN_PROGRESS_PLAYER_B, FINISHED_PLAYER_A, FINISHED_PLAYER_B
    }
}