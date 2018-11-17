package net.borysw.blitz

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BindModule {
  @ContributesAndroidInjector()
  abstract fun bindMainActivity(): MainActivity

  @ContributesAndroidInjector
  abstract fun bindClockFragment(): GameFragment
}