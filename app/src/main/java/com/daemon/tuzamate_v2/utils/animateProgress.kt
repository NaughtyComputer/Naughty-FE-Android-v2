package com.daemon.tuzamate_v2.utils

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar

fun ProgressBar.animateProgress(
    targetProgress: Int,
    duration: Long = 800,
    interpolator: TimeInterpolator = DecelerateInterpolator()
) {
    val animator = ObjectAnimator.ofInt(this, "progress", this.progress, targetProgress)
    animator.duration = duration
    animator.interpolator = interpolator
    animator.start()
}