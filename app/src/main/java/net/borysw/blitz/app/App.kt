package net.borysw.blitz.app

import android.app.Application
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import net.borysw.blitz.BuildConfig
import net.borysw.blitz.app.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    // val appComponent: AppComponent by lazy {
    //     initializeComponent()
    // }
    //
    // private fun initializeComponent(): AppComponent {
    //     return DaggerAppComponent.factory().create(applicationContext)
    // }

    override fun onCreate() {
        super.onCreate()
        init()
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    private fun init() {
        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}