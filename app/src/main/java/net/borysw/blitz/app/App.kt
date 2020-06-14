package net.borysw.blitz.app

import android.app.Application
import androidx.preference.PreferenceManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import net.borysw.blitz.BuildConfig
import net.borysw.blitz.R
import net.borysw.blitz.app.di.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initLogging()
        initDI()
        setDefaultSettings()
    }

    private fun setDefaultSettings() {
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);
        PreferenceManager.setDefaultValues(this, R.xml.game_time_settings, true);
        PreferenceManager.setDefaultValues(this, R.xml.game_type_settings, true);
    }

    private fun initDI() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}