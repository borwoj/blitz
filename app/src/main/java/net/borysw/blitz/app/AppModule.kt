package net.borysw.blitz.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [(ViewModelModule::class)])
class AppModule {

  @Provides
  @Singleton
  fun provideContext(application: Application): Context = application

  @Provides
  @Singleton
  fun provideSharedPreferences(context: Context): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(context)
}