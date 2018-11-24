package net.borysw.blitz.game.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getColor
import kotlinx.android.synthetic.main.view_time.view.time
import net.borysw.blitz.R

class TimerView : ConstraintLayout {

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
        animateActiveStatus(field)
      }
    }

  fun setLoser() {
    animateColorChange(getColor(context, R.color.player_active), getColor(context, R.color.player_loser))
  }

  fun setWinner() {
    animateColorChange(getColor(context, R.color.player_inactive), getColor(context, R.color.player_active))
  }

  init {
    inflate(context, R.layout.view_time, this)
  }

  private fun animateActiveStatus(active: Boolean) {
    val fromColor = if (active) getColor(context, R.color.player_inactive) else getColor(context, R.color.player_active)
    val toColor = if (active) getColor(context, R.color.player_active) else getColor(context, R.color.player_inactive)
    animateColorChange(fromColor, toColor)
  }

  private fun animateColorChange(fromColor: Int, toColor: Int) {
    ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor).apply {
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