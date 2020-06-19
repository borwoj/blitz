package net.borysw.blitz.game.engine.audio

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.schedulers.Schedulers.trampoline
import io.reactivex.subjects.PublishSubject
import net.borysw.blitz.R
import net.borysw.blitz.game.engine.userActions.UserAction
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer1
import net.borysw.blitz.game.engine.userActions.UserAction.ClockClickedPlayer2
import net.borysw.blitz.game.engine.userActions.UserActions
import net.borysw.blitz.settings.Settings
import net.borysw.blitz.settings.Settings.AppSettings
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class SoundEngineImplTest {

    @Test
    @DisplayName("when sound is enabled, should play sounds")
    fun getSound() {
        // given
        val soundPlayer = mock<SoundPlayer>()
        val userActionsSubject = PublishSubject.create<UserAction>()
        val userActions = mock<UserActions> {
            on(it.userActions).thenReturn(userActionsSubject)
        }
        val appSettings = PublishSubject.create<AppSettings>()
        val settings = mock<Settings> {
            on(it.appSettings).thenReturn(appSettings)
        }
        val testedObj = SoundEngineImpl(soundPlayer, userActions, settings, trampoline())

        // when
        testedObj.sound.test()
        appSettings.onNext(AppSettings(soundEnabled = false))
        userActionsSubject.onNext(ClockClickedPlayer1)
        userActionsSubject.onNext(ClockClickedPlayer2)

        appSettings.onNext(AppSettings(soundEnabled = true))
        userActionsSubject.onNext(ClockClickedPlayer1)
        userActionsSubject.onNext(ClockClickedPlayer2)

        // then
        verify(soundPlayer).playSound(R.raw.clock_press_1)
        verify(soundPlayer).playSound(R.raw.clock_press_2)
    }
}