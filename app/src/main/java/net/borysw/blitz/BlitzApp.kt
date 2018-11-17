package net.borysw.blitz

import android.app.Activity
import android.app.Application
import android.app.Service
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class BlitzApp : Application(), HasActivityInjector, HasSupportFragmentInjector, HasServiceInjector {

  @Inject
  lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  @Inject
  lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  @Inject
  lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

  override fun onCreate() {
    super.onCreate()
    init()
  }

  private fun init() {
    initTimber()
    initDagger()
  }

  private fun initDagger() {
    DaggerAppComponent.builder().application(this).build().inject(this)
  }

  private fun initTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

  override fun serviceInjector(): AndroidInjector<Service> = serviceDispatchingAndroidInjector

  override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector
}