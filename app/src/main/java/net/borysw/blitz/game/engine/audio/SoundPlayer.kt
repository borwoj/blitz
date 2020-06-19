package net.borysw.blitz.game.engine.audio

interface SoundPlayer {
    fun playSound(resId: Int)
    fun release()
}