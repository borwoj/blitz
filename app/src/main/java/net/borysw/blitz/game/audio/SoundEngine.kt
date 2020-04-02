package net.borysw.blitz.game.audio

import io.reactivex.ObservableTransformer
import net.borysw.blitz.game.UserAction

interface SoundEngine : ObservableTransformer<UserAction, UserAction> {
}