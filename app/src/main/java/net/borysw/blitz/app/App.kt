package net.borysw.blitz.app

import android.app.Application
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
        initCrashlytics()
        initDI()
        initAnalytics()
        setDefaultSettings()
    }

    private fun setDefaultSettings() {
        setOf(
            R.xml.app_settings,
            R.xml.game_time_settings,
            R.xml.game_type_settings
        ).forEach { settingsFileId ->
            PreferenceManager.setDefaultValues(this, settingsFileId, true)
        }
    }

    private fun initDI() {
        DaggerAppComponent.builder().application(this).build().inject(this)
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initCrashlytics() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    private fun initAnalytics() {
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}