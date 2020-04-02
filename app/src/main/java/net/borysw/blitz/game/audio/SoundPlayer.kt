package net.borysw.blitz.game.audio

interface SoundPlayer {
    fun playSound(resId: Int)
    fun release()
}