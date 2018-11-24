package net.borysw.blitz.game.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.view_timer.view.time
import net.borysw.blitz.R
import net.borysw.blitz.R.color.player_active
import net.borysw.blitz.R.color.player_inactive
import net.borysw.blitz.R.layout.view_timer

class TimerView : ConstraintLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  companion object {
    const val FADE_DURATION_MS = 500L
  }

  fun setActive(active: Boolean) {
    animateActiveStatus(active)
  }

  fun setLoser() {
    animateColorChange(getColor(context, R.color.player_loser))
  }

  fun setWinner() {
    animateColorChange(getColor(context, player_active))
  }

  init {
    inflate(context, view_timer, this)
  }

  private fun animateActiveStatus(active: Boolean) {
    val endColor = if (active) getColor(context, player_active) else getColor(context, player_inactive)
    animateColorChange(endColor)
  }

  private fun animateColorChange(endColor: Int) {
    ValueAnimator.ofObject(ArgbEvaluator(), (background as ColorDrawable).color, endColor).apply {
      duration = 250
      addUpdateListener { animator -> setBackgroundColor(animator.animatedValue as Int) }
    }.start()
  }

  /*private fun setupTextSwitcher() {
    with(text_switcher) {
      setFactory {
        createTimeTextView()
      }
      inAnimation = AlphaAnimation(0f, 1f).apply { duration =
          FADE_DURATION_MS
      }
      outAnimation = AlphaAnimation(1f, 0f).apply { duration =
          FADE_DURATION_MS
      }
    }
  }

  private fun createTimeTextView() = TextView(context).apply {
    gravity = BOTTOM or CENTER_HORIZONTAL
    TextViewCompat.setTextAppearance(this, android.R.style.TextAppearance_Material_Display2)
    setTextColor(ContextCompat.getColor(context, R.color.white))
    with(16.px) { setPadding(this, this, this, this) }
  }*/

  fun setTime(time: String) {
    this.time.text = time
  }
}