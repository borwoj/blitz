package net.borysw.blitz.game.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.view_time.view.time
import net.borysw.blitz.R

class TimeView : ConstraintLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  companion object {
    const val FADE_DURATION_MS = 500L
  }

  var isActive: Boolean = false
    set(value) {
      if (value != field) {
        field = value
        animateColorChange(field)
      }
    }

  init {
    inflate(context, R.layout.view_time, this)
  }

  private fun animateColorChange(active: Boolean) {
    val colorFrom =
      if (active) ContextCompat.getColor(context, R.color.b) else ContextCompat.getColor(context, R.color.a)
    val colorTo = if (active) ContextCompat.getColor(context, R.color.a) else ContextCompat.getColor(context, R.color.b)
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
    colorAnimation.duration = 250
    colorAnimation.addUpdateListener { animator -> setBackgroundColor(animator.animatedValue as Int) }
    colorAnimation.start()
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