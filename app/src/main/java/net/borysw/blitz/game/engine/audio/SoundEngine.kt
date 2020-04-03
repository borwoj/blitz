package net.borysw.blitz.game.engine.audio

import io.reactivex.ObservableTransformer
import net.borysw.blitz.game.UserAction

interface SoundEngine : ObservableTransformer<UserAction, Unit> {
}