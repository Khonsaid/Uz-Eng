package uz.gita.latizx.uz_eng.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

fun View.animateScaleAndRotate(duration: Long = 300L, scaleFactor: Float = 1.2f) {
    val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat(View.SCALE_X, scaleFactor),
        PropertyValuesHolder.ofFloat(View.SCALE_Y, scaleFactor)
    )
    scaleUp.duration = duration

    val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
    )
    scaleDown.duration = duration

    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(scaleUp, scaleDown)
    animatorSet.start()
}