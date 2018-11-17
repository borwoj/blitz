package net.borysw.blitz.app

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.borysw.blitz.MainActivity
import net.borysw.blitz.game.presentation.GameFragment

@Module
abstract class BindModule {
  @ContributesAndroidInjector()
  abstract fun bindMainActivity(): MainActivity

  @ContributesAndroidInjector
  abstract fun bindClockFragment(): GameFragment
}