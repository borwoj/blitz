package net.borysw.blitz.app.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import net.borysw.blitz.game.presentation.GameFragment

@Module
abstract class BindModule {
    @ContributesAndroidInjector
    abstract fun bindGameFragment(): GameFragment
}