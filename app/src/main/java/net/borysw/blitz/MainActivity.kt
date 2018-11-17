package net.borysw.blitz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  private lateinit var viewModel: ClockViewModel
  @Inject
  lateinit var viewModelFactory: ViewModelFactory

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_clock)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(ClockViewModel::class.java)
  }
}
