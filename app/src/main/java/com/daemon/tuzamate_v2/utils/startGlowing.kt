package com.daemon.tuzamate_v2.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.core.graphics.toColorInt

fun TextView.startGlowing(
    duration: Long = 1000,
    repeatCount: Int = 10,
    glowColor: Int? = null,
) {
    val originalColor = this.currentTextColor
    val targetColor = glowColor ?: "#FFD700".toColorInt()
    
    val colorAnimator = ValueAnimator.ofArgb(originalColor, targetColor, originalColor)
    colorAnimator.repeatCount = repeatCount
    val alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0.6f, 1f)
    alphaAnimator.repeatCount = repeatCount
    val shadowAnimator = ValueAnimator.ofFloat(0f, 12f, 0f)
    shadowAnimator.repeatCount = repeatCount
    
    colorAnimator.addUpdateListener { animation ->
        this.setTextColor(animation.animatedValue as Int)
    }
    
    shadowAnimator.addUpdateListener { animation ->
        val radius = animation.animatedValue as Float
        this.setShadowLayer(radius, 0f, 0f, targetColor)
    }
    
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(colorAnimator, alphaAnimator, shadowAnimator)
    animatorSet.duration = duration
    animatorSet.interpolator = AccelerateDecelerateInterpolator()
    
    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@startGlowing.setTextColor(originalColor)
            this@startGlowing.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
            this@startGlowing.alpha = 1f
        }
    })
    
    animatorSet.start()
}