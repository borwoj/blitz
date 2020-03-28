package net.borysw.blitz.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import net.borysw.blitz.game.presentation.GameViewModel

@Module
abstract class ViewModelBuilderModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindViewModel(viewmodel: GameViewModel): ViewModel
}