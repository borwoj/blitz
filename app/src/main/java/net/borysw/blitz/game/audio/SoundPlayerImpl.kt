package net.borysw.blitz.game.audio

import android.app.Application
import android.media.MediaPlayer
import timber.log.Timber
import javax.inject.Inject

class SoundPlayerImpl @Inject constructor(private val app: Application) : SoundPlayer {
    private val mediaPlayerList = mutableListOf<MediaPlayer>()

    override fun playSound(resId: Int) {
        MediaPlayer.create(app, resId).run {
            mediaPlayerList.add(this)
            start()
            setOnCompletionListener {
                release()
                mediaPlayerList.remove(this)
            }
            setOnErrorListener { _, what, extra ->
                release()
                mediaPlayerList.remove(this)
                Timber.e("Failed to play sound. Error type: $what. Extras: $extra")
                true
            }
        }
    }
}