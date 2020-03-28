package net.borysw.blitz.app.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import net.borysw.blitz.app.App
import javax.inject.Singleton

@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        BindModule::class,
        ViewModelBuilderModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}