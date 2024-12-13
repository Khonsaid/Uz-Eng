package uz.gita.latizx.uz_eng.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View

fun View.startScaleAnim(duration: Long = 300L, scaleFactor: Float = 1.2f) {
    // Agar bu view allaqachon animatsiyada bo'lsa, uni to'xtatish
    (this.tag as? AnimatorSet)?.cancel()

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

    // Animatsiya ketma-ketligi
    val animatorSet = AnimatorSet()
    animatorSet.playSequentially(scaleUp, scaleDown)

    // Takrorlashni sozlash
    animatorSet.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            if (tag == "animating") {
                animatorSet.start() // Animatsiyani qayta boshlash
            }
        }
    })

    this.tag = animatorSet // Animatsiya holatini belgilash
    animatorSet.start()
}

fun View.stopScaleAnim() {
    this.animate().cancel() // Har qanday davom etayotgan animatsiyani to'xtatish
    this.tag = null // Animatsiyani tugatish belgisi
    this.scaleX = 1f
    this.scaleY = 1f
}
